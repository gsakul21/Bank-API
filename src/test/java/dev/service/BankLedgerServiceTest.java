package dev.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.HashMap;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import dev.domain.TransactionEvent;
import dev.domain.TransactionStatus;
import dev.repository.TransactionEventRepository;
import dev.schemas.Amount;
import dev.schemas.AuthorizationRequest;
import dev.schemas.AuthorizationResponse;
import dev.schemas.DebitCredit;
import dev.schemas.LoadRequest;
import dev.schemas.LoadResponse;
import dev.schemas.ResponseCode;
import dev.schemas.ServerError;

/*
 * This test suite focuses on testing the functionality and integration of
 * the BankLedgerService.
 */

@SpringBootTest
public class BankLedgerServiceTest {

    @Autowired
    private BankLedgerService bankLedgerService;

    @Autowired
    private TransactionEventRepository transactionEventRepository;

    @MockBean
    private UserService userService;

    /*
     * Test the case where given a certain AuthorizationRequest, the
     * authorization could not go through and was declined for some reason.
     * Checking to make sure that the correct information and balances were
     * returned as well as the event being stored correctly in the database.
     */

    @Test
    void testAuthorizeDenied() throws Exception
    {

        // Artifical data creation, balances of a some user.

        HashMap<String, BigDecimal> testBalances = new HashMap<>();
        
        testBalances.put("USD", new BigDecimal("450"));
        testBalances.put("INR", new BigDecimal("350"));

        when(userService.getBalances(any())).thenReturn(testBalances);

        String original_asked_amount = "500";

        // Building the request

        Amount amt = new Amount(original_asked_amount, "USD", DebitCredit.DEBIT);
        AuthorizationRequest request = new AuthorizationRequest("a", "kjlfj'4029-tj0[jow[rs'jhg]]", amt);
        
        // Calling the appropriate function in the service.
        
        Object response = bankLedgerService.authorize(request);

        // In this case, it should still return an AuthorizationResponse.

        assertEquals(AuthorizationResponse.class, response.getClass());

        AuthorizationResponse resp = (AuthorizationResponse) response;

        //Ensure that all of the fields contain the correct information.

        assertEquals(request.getUserId(), resp.getUserId());
        assertEquals(request.getMessageId(), resp.getMessageId());
        assertEquals(testBalances.get("USD").toString(), resp.getBalance().getAmount());
        assertEquals(request.getTransactionAmount().getCurrency(), resp.getBalance().getCurrency());
        assertEquals(request.getTransactionAmount().getDebitOrCredit(), resp.getBalance().getDebitOrCredit());
        assertEquals(ResponseCode.DECLINED, resp.getResponse());

        // Check that the event was indeed generated and saved correctly.

        TransactionEvent savedEvent = transactionEventRepository.findById(request.getMessageId()).orElse(null);

        assertNotNull(savedEvent);

        // Check that the saved event has all of the correct information.

        assertEquals(request.getUserId(), savedEvent.getUserId());
        assertEquals(request.getMessageId(), savedEvent.getMessageId());
        assertEquals(original_asked_amount, savedEvent.getAmount().toString());
        assertEquals(request.getTransactionAmount().getCurrency(), savedEvent.getCurrency());
        assertEquals(request.getTransactionAmount().getDebitOrCredit(), savedEvent.getDebitOrCredit());
        assertEquals(TransactionStatus.AUTH_FAIL, savedEvent.getTransactionStatus());
    }

    /*
     * Test the case where given a certain AuthorizationRequest, the
     * authorization was successful and went through. Checking to make sure that t
     * money was withdrawn correctly and returned information and balances were
     * were accurate as well. Also verify that the successful transaction was stored
     * in the database.
     */

    @Test
    void testAuthorizeApproved() throws Exception 
    {
        // Artifical data, balances of some user

        HashMap<String, BigDecimal> testBalances = new HashMap<>();
        
        testBalances.put("USD", new BigDecimal("450"));
        testBalances.put("INR", new BigDecimal("350"));

        when(userService.getBalances(any())).thenReturn(testBalances);

        String original_requested_amount = "350";

        // Building the request

        Amount amt = new Amount(original_requested_amount, "INR", DebitCredit.DEBIT);
        AuthorizationRequest request = new AuthorizationRequest("luka", "jfa9q9f-23jfoiajfapoe39", amt);
        
        Object response = bankLedgerService.authorize(request);

        // Successful response results in AuthorizationResponse, verifying this here.

        assertEquals(AuthorizationResponse.class, response.getClass());

        AuthorizationResponse resp = (AuthorizationResponse) response;

        // Check that all the required fields are there and have the correct information with the updated balances.

        assertEquals(request.getUserId(), resp.getUserId());
        assertEquals(request.getMessageId(), resp.getMessageId());
        assertEquals(testBalances.get("INR").toString(), resp.getBalance().getAmount());
        assertEquals(request.getTransactionAmount().getCurrency(), resp.getBalance().getCurrency());
        assertEquals(request.getTransactionAmount().getDebitOrCredit(), resp.getBalance().getDebitOrCredit());
        assertEquals(ResponseCode.APPROVED, resp.getResponse());

        // Check that the event was indeed generated and saved correctly.

        TransactionEvent savedEvent = transactionEventRepository.findById(request.getMessageId()).orElse(null);

        assertNotNull(savedEvent);

        // Check that the saved event has all of the correct information.

        assertEquals(request.getUserId(), savedEvent.getUserId());
        assertEquals(request.getMessageId(), savedEvent.getMessageId());
        assertEquals(original_requested_amount, savedEvent.getAmount().toString());
        assertEquals(request.getTransactionAmount().getCurrency(), savedEvent.getCurrency());
        assertEquals(request.getTransactionAmount().getDebitOrCredit(), savedEvent.getDebitOrCredit());
        assertEquals(TransactionStatus.AUTH_SUCCESS, savedEvent.getTransactionStatus());
    }

    /*
     * Make sure that in the event of an error from its dependent services in
     * the authorization function, the server propagates the error properly with
     * the correct information and structure while also recording the event
     * as an Authorization Declined event.
     */

    @Test
    void testAuthError() throws Exception
    {
        //Artifical data generation, balances for some user.

        HashMap<String, BigDecimal> testBalances = new HashMap<>();
        
        testBalances.put("USD", new BigDecimal("450"));
        testBalances.put("INR", new BigDecimal("350"));

        when(userService.getBalances(any())).thenReturn(testBalances);

        Amount amt = new Amount("500", "USD", DebitCredit.CREDIT);
        AuthorizationRequest request = new AuthorizationRequest("c", "kjlfjgweiohjgwekognl,z.sfm", amt);
        
        Object response = bankLedgerService.authorize(request);

        //Should throw an error this time, verifying that it does.

        assertEquals(ServerError.class, response.getClass());

        // Checking that error is presented in correct format with correct information.

        ServerError resp = (ServerError) response;

        assertEquals("Invalid DebitCredit Input", resp.getMessage());

        // Check that event was generated and saved.

        TransactionEvent savedEvent = transactionEventRepository.findById(request.getMessageId()).orElse(null);

        assertNotNull(savedEvent);

        //Checking event information to make sure that it is correct.

        assertEquals(request.getUserId(), savedEvent.getUserId());
        assertEquals(request.getMessageId(), savedEvent.getMessageId());
        assertEquals(request.getTransactionAmount().getAmount(), savedEvent.getAmount().toString());
        assertEquals(request.getTransactionAmount().getCurrency(), savedEvent.getCurrency());
        assertEquals(request.getTransactionAmount().getDebitOrCredit(), savedEvent.getDebitOrCredit());
        assertEquals(TransactionStatus.AUTH_FAIL, savedEvent.getTransactionStatus());
    }

      /*
     * Make sure that in the event of an error from its dependent services in
     * the load function, the server propagates the error properly with
     * the correct information and structure while also recording the event
     * as a Load Declined event.
     */

    @Test
    void testLoadError() throws Exception
    {
        //Artifical data generation, balances for some user.

        HashMap<String, BigDecimal> testBalances = new HashMap<>();
        
        testBalances.put("USD", new BigDecimal("450"));
        testBalances.put("INR", new BigDecimal("350"));

        when(userService.getBalances(any())).thenReturn(testBalances);

        Amount amt = new Amount("500", "USD", DebitCredit.DEBIT);
        LoadRequest request = new LoadRequest("d", "kjjseht89243gn802434gi0n", amt);
        
        Object response = bankLedgerService.load(request);

        //Should throw an error this time, verifying that it does.

        assertEquals(ServerError.class, response.getClass());

        // Checking that error is presented in correct format with correct information.

        ServerError resp = (ServerError) response;

        assertEquals("Invalid DebitCredit Input", resp.getMessage());

        // Check that event was generated and saved.       

        TransactionEvent savedEvent = transactionEventRepository.findById(request.getMessageId()).orElse(null);

        assertNotNull(savedEvent);

        //Checking event information to make sure that it is correct.        

        assertEquals(request.getUserId(), savedEvent.getUserId());
        assertEquals(request.getMessageId(), savedEvent.getMessageId());
        assertEquals(request.getTransactionAmount().getAmount(), savedEvent.getAmount().toString());
        assertEquals(request.getTransactionAmount().getCurrency(), savedEvent.getCurrency());
        assertEquals(request.getTransactionAmount().getDebitOrCredit(), savedEvent.getDebitOrCredit());
        assertEquals(TransactionStatus.LOAD_FAIL, savedEvent.getTransactionStatus());
    }

    /*
     * Test the case where given a certain LoadRequest, the load was successful and 
     * went through. Checking to make sure that money was added correctly and returned 
     * information and balances are accurate as well. Also verify that the successful transaction 
     * was stored in the event database.
     */

    @Test
    void testLoadApproved() throws Exception 
    {
        //Artifical data generation, balances for some user.

        HashMap<String, BigDecimal> testBalances = new HashMap<>();
        
        testBalances.put("USD", new BigDecimal("450"));
        testBalances.put("INR", new BigDecimal("350"));

        when(userService.getBalances(any())).thenReturn(testBalances);

        Amount amt = new Amount("500", "USD", DebitCredit.CREDIT);
        LoadRequest request = new LoadRequest("e", "kln;sdgh8t0249jw0-egr", amt);
        
        Object response = bankLedgerService.load(request);

        // Went through successfully, should be a LoadResponse object.

        assertEquals(LoadResponse.class, response.getClass());

        LoadResponse resp = (LoadResponse) response;

        // Verify that the LoadResponse contains all the correct fields and values.

        assertEquals(request.getUserId(), resp.getUserId());
        assertEquals(request.getMessageId(), resp.getMessageId());
        assertEquals(testBalances.get("USD").toString(), resp.getBalance().getAmount());
        assertEquals(request.getTransactionAmount().getCurrency(), resp.getBalance().getCurrency());
        assertEquals(request.getTransactionAmount().getDebitOrCredit(), resp.getBalance().getDebitOrCredit());

        // Check that the event was generated and saved.

        TransactionEvent savedEvent = transactionEventRepository.findById(request.getMessageId()).orElse(null);

        assertNotNull(savedEvent);

        // Check that the event has all of the information that it should and is correct.

        assertEquals(request.getUserId(), savedEvent.getUserId());
        assertEquals(request.getMessageId(), savedEvent.getMessageId());
        assertEquals("500", savedEvent.getAmount().toString());
        assertEquals(request.getTransactionAmount().getCurrency(), savedEvent.getCurrency());
        assertEquals(request.getTransactionAmount().getDebitOrCredit(), savedEvent.getDebitOrCredit());
        assertEquals(TransactionStatus.LOAD_SUCCESS, savedEvent.getTransactionStatus());
    }
    
}
