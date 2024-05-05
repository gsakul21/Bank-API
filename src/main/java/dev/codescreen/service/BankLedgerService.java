package dev.codescreen.service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.codescreen.domain.TransactionStatus;
import dev.codescreen.domain.User;
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

@Service
public class BankLedgerService {
    private final TransactionEventRepository transactionEventRepository;

    @Autowired
    private UserService userService;

    public BankLedgerService(TransactionEventRepository transactionEventRepository)
    {
        this.transactionEventRepository = transactionEventRepository;
    }

    public Object authorize(AuthorizationRequest authorizationRequest)
    {
        try
        {
            Amount authAmount = authorizationRequest.getTransactionAmount();
            User account = userService.findUser(authorizationRequest.getUserId());

            Map<String, BigDecimal> currentBalances = account.getBalances();

            String targetCurrency = authAmount.getCurrency();
            BigDecimal targetBalance = new BigDecimal(authAmount.getAmount());

            if (currentBalances.containsKey(targetCurrency) == false || currentBalances.get(targetCurrency).compareTo(targetBalance) < 0)
            {
                saveEvent(authorizationRequest.getUserId(),
                authorizationRequest.getMessageId(),
                TransactionStatus.AUTH_FAIL,
                authAmount.getDebitOrCredit(),
                authAmount.getCurrency(),
                authAmount.getAmount(),
                Instant.now().toString());

                AuthorizationResponse authResp = new AuthorizationResponse(account.getUserId(), authorizationRequest.getMessageId(), 
                ResponseCode.DECLINED, authAmount);

                return authResp;
            }
            
            currentBalances.put(targetCurrency, currentBalances.get(targetCurrency).subtract(targetBalance));
            authAmount.setAmount(currentBalances.get(targetCurrency).toString());

            saveEvent(authorizationRequest.getUserId(),
            authorizationRequest.getMessageId(),
            TransactionStatus.AUTH_SUCCESS,
            authAmount.getDebitOrCredit(),
            authAmount.getCurrency(),
            authAmount.getAmount(),
            Instant.now().toString());

            AuthorizationResponse authResp = new AuthorizationResponse(account.getUserId(), authorizationRequest.getMessageId(), 
            ResponseCode.APPROVED, authAmount);

            return authResp;
        }
        catch (Exception e)
        {
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
            User account = userService.findUser(loadRequest.getUserId());
            Amount loadAmount = loadRequest.getTransactionAmount();

            Map<String, BigDecimal> currentBalances = account.getBalances();

            String targetCurrency = loadAmount.getCurrency();
            BigDecimal targetBalance = new BigDecimal(loadAmount.getAmount());

            if (currentBalances.containsKey(loadAmount.getCurrency()))
            {
                currentBalances.put(targetCurrency, currentBalances.get(targetCurrency).add(targetBalance));
            }
            else
            {
                currentBalances.put(targetCurrency, targetBalance);
            }

            account.setBalance(currentBalances);
            userService.updateUser(account);
            
            saveEvent(loadRequest.getUserId(),
            loadRequest.getMessageId(),
            TransactionStatus.LOAD_SUCCESS,
            loadAmount.getDebitOrCredit(),
            loadAmount.getCurrency(),
            loadAmount.getAmount(),
            Instant.now().toString());

            loadAmount.setAmount(currentBalances.get(targetCurrency).toString());
            LoadResponse resp = new LoadResponse(loadRequest.getUserId(), loadRequest.getMessageId(), loadAmount);

            return resp;
        }
        catch (Exception e)
        {
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
    
    public void saveEvent(String userId, String messageId, TransactionStatus transactionStatus, DebitCredit debitOrCredit, 
    String currency, String amount, String timeOfEvent)
    {
        TransactionEvent event = new TransactionEvent(userId, messageId, transactionStatus, 
        debitOrCredit, currency, amount, timeOfEvent);

        transactionEventRepository.saveAndFlush(event);
    }
}