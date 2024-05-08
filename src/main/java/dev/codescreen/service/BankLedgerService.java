package dev.codescreen.service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashMap;

import org.springframework.stereotype.Service;

import dev.codescreen.domain.TransactionStatus;
import dev.codescreen.domain.TransactionEvent;
import dev.codescreen.repository.*;
import dev.codescreen.schemas.Amount;
import dev.codescreen.schemas.AuthorizationRequest;
import dev.codescreen.schemas.AuthorizationResponse;
import dev.codescreen.schemas.DebitCredit;
import dev.codescreen.schemas.LoadRequest;
import dev.codescreen.schemas.LoadResponse;
import dev.codescreen.schemas.ResponseCode;
import dev.codescreen.schemas.ServerError;

/*
 * This is the service responsible for providing the core functionality of
 * the API, handling requests to load and authorize assets (primarily money) for 
 * different users.
 */

@Service
public class BankLedgerService {

    /*
     * Instances of utilities for the service, refer to {@TransactionEventRepository}
     * and {@link UserService to learn more about them}
     */
    private final TransactionEventRepository transactionEventRepository;
    private UserService userService;

    public BankLedgerService(TransactionEventRepository transactionEventRepository, UserService userService)
    {
        this.transactionEventRepository = transactionEventRepository;
        this.userService = userService;
    }

    /**
     * @param authorizationRequest
     * @return AuthorizationResponse or ServerError, result of trying to process
     * the request.
     * 
     * This function handles what it means to process an authorization request.
     * It breaks down the request and determines if it is possible to authorize
     * the requested funds for a user, checking if they have enough money to do
     * so. If successful in checking, returns a response indicating if it happened
     * or not, and if there is an error then returns the error.
     */
    public Object authorize(AuthorizationRequest authorizationRequest)
    {
        try
        {
            // Get the specifics of the requested authorization.
            Amount authAmount = authorizationRequest.getTransactionAmount();

            if (authAmount.getDebitOrCredit() != DebitCredit.DEBIT)
            {
                throw new Exception("Invalid DebitCredit Input");
            }

            // Get the current balance of the user for the specific currency
            HashMap<String, BigDecimal> currentBalances = userService.getBalances(authorizationRequest.getUserId());

            // Convert into more manageable types
            String targetCurrency = authAmount.getCurrency();
            BigDecimal targetBalance = new BigDecimal(authAmount.getAmount());

            /*
             * Checks if the user has enough money in the specified currency. If it doesn't then
             * saves as a failed authorization event, and returns as a failed authorization
             */
            if (currentBalances.containsKey(targetCurrency) == false || currentBalances.get(targetCurrency).compareTo(targetBalance) < 0)
            {
                saveEvent(authorizationRequest.getUserId(),
                authorizationRequest.getMessageId(),
                TransactionStatus.AUTH_FAIL,
                authAmount.getDebitOrCredit(),
                authAmount.getCurrency(),
                authAmount.getAmount(),
                Instant.now().toString());

                String newAmt = currentBalances.containsKey(targetCurrency) ? currentBalances.get(targetCurrency).toString() : "0";
                authAmount.setAmount(newAmt);

                AuthorizationResponse authResp = new AuthorizationResponse(authorizationRequest.getUserId(), authorizationRequest.getMessageId(), 
                ResponseCode.DECLINED, authAmount);

                return authResp;
            }

            // Applies authorization and withdraws the specified money from the user.
            currentBalances.put(targetCurrency, currentBalances.get(targetCurrency).subtract(targetBalance));
            userService.updateBalances(authorizationRequest.getUserId(), currentBalances);

            /*
             * Recording the event and its status, since it went through it is
             * success.
             */ 
            saveEvent(authorizationRequest.getUserId(),
            authorizationRequest.getMessageId(),
            TransactionStatus.AUTH_SUCCESS,
            authAmount.getDebitOrCredit(),
            authAmount.getCurrency(),
            authAmount.getAmount(),
            Instant.now().toString());

            /*
             * Update to reflect the current balance after authorization and
             * returns required information for the endpoint as specified in the
             * service specification.
             */
            authAmount.setAmount(currentBalances.get(targetCurrency).toString());

            AuthorizationResponse authResp = new AuthorizationResponse(authorizationRequest.getUserId(), authorizationRequest.getMessageId(), 
            ResponseCode.APPROVED, authAmount);

            return authResp;
        }
        catch (Exception e)
        {
            /*
             * If there is an issue when trying to process the request, catch
             * the error and save the event as a failed authorization and then
             * return the error.
             */

            Amount authAmount = authorizationRequest.getTransactionAmount();

            saveEvent(authorizationRequest.getUserId(),
            authorizationRequest.getMessageId(),
            TransactionStatus.AUTH_FAIL,
            authAmount.getDebitOrCredit(),
            authAmount.getCurrency(),
            authAmount.getAmount(),
            Instant.now().toString());

            return new ServerError(e.getMessage());
        }
        
    }

    public Object load(LoadRequest loadRequest)
    {
        try
        {              
            // Get the specifics of the requested load.
            Amount loadAmount = loadRequest.getTransactionAmount();

            if (loadAmount.getDebitOrCredit() != DebitCredit.CREDIT)
            {
                throw new Exception("Invalid DebitCredit Input");
            }

            // Retrieve the current balances of the user.
            HashMap<String, BigDecimal> currentBalances = userService.getBalances(loadRequest.getUserId());

            // Get what the requested amount to load is and what currency.
            String targetCurrency = loadAmount.getCurrency();
            BigDecimal targetBalance = new BigDecimal(loadAmount.getAmount());

            /*
             * If the user already has funds in the currency, add to it. If they
             * dont then add as a new entry to their balances.
             */
            if (currentBalances.containsKey(loadAmount.getCurrency()))
            {
                currentBalances.put(targetCurrency, currentBalances.get(targetCurrency).add(targetBalance));
            }
            else
            {
                currentBalances.put(targetCurrency, targetBalance);
            }

            // Push changes to in-memory object holding current user balances.
            userService.updateBalances(loadRequest.getUserId(), currentBalances);
            
            // Save the event as a successful load.
            saveEvent(loadRequest.getUserId(),
            loadRequest.getMessageId(),
            TransactionStatus.LOAD_SUCCESS,
            loadAmount.getDebitOrCredit(),
            loadAmount.getCurrency(),
            loadAmount.getAmount(),
            Instant.now().toString());

            // Return required format of response, with updated balance.
            loadAmount.setAmount(currentBalances.get(targetCurrency).toString());
            LoadResponse resp = new LoadResponse(loadRequest.getUserId(), loadRequest.getMessageId(), loadAmount);

            return resp;
        }
        catch (Exception e)
        {
            /*
             * If there is an issue when trying to process the request, catch
             * the error and save the event as a failed load, then return the error.
             */

            Amount loadAmount = loadRequest.getTransactionAmount();

            saveEvent(loadRequest.getUserId(),
            loadRequest.getMessageId(),
            TransactionStatus.LOAD_FAIL,
            loadAmount.getDebitOrCredit(),
            loadAmount.getCurrency(),
            loadAmount.getAmount(),
            Instant.now().toString());
            
            return new ServerError(e.getMessage());
        }
    }
    
    
    /**
     * @param userId
     * @param messageId
     * @param transactionStatus
     * @param debitOrCredit
     * @param currency
     * @param amount
     * @param timeOfEvent
     * 
     * @return No return
     * 
     * A function that will take in all the information that is required to be
     * kept for an event and proceed to construct the event and write it to the
     * database.
     */
    public void saveEvent(String userId, String messageId, TransactionStatus transactionStatus, DebitCredit debitOrCredit, 
    String currency, String amount, String timeOfEvent)
    {
        TransactionEvent event = new TransactionEvent(userId, messageId, transactionStatus, 
        debitOrCredit, currency, amount, timeOfEvent);

        transactionEventRepository.saveAndFlush(event);
    }
}