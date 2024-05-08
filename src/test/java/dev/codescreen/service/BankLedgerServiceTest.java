package dev.codescreen.service;

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

import dev.codescreen.domain.TransactionEvent;
import dev.codescreen.domain.TransactionStatus;
import dev.codescreen.repository.TransactionEventRepository;
import dev.codescreen.schemas.Amount;
import dev.codescreen.schemas.AuthorizationRequest;
import dev.codescreen.schemas.AuthorizationResponse;
import dev.codescreen.schemas.DebitCredit;
import dev.codescreen.schemas.LoadRequest;
import dev.codescreen.schemas.LoadResponse;
import dev.codescreen.schemas.ResponseCode;
import dev.codescreen.schemas.ServerError;

@SpringBootTest
public class BankLedgerServiceTest {

    @Autowired
    private BankLedgerService bankLedgerService;

    @Autowired
    private TransactionEventRepository transactionEventRepository;

    @MockBean
    private UserService userService;


    @Test
    void testAuthorizeDenied() throws Exception
    {
        HashMap<String, BigDecimal> testBalances = new HashMap<>();
        
        testBalances.put("USD", new BigDecimal("450"));
        testBalances.put("INR", new BigDecimal("350"));

        when(userService.getBalances(any())).thenReturn(testBalances);

        String original_asked_amount = "500";

        Amount amt = new Amount(original_asked_amount, "USD", DebitCredit.DEBIT);
        AuthorizationRequest request = new AuthorizationRequest("a", "kjlfj'4029-tj0[jow[rs'jhg]]", amt);
        
        Object response = bankLedgerService.authorize(request);

        assertEquals(AuthorizationResponse.class, response.getClass());

        AuthorizationResponse resp = (AuthorizationResponse) response;

        assertEquals(request.getUserId(), resp.getUserId());
        assertEquals(request.getMessageId(), resp.getMessageId());
        assertEquals(testBalances.get("USD").toString(), resp.getBalance().getAmount());
        assertEquals(request.getTransactionAmount().getCurrency(), resp.getBalance().getCurrency());
        assertEquals(request.getTransactionAmount().getDebitOrCredit(), resp.getBalance().getDebitOrCredit());
        assertEquals(ResponseCode.DECLINED, resp.getResponse());

        TransactionEvent savedEvent = transactionEventRepository.findById(request.getMessageId()).orElse(null);

        assertNotNull(savedEvent);

        assertEquals(request.getUserId(), savedEvent.getUserId());
        assertEquals(request.getMessageId(), savedEvent.getMessageId());
        assertEquals(original_asked_amount, savedEvent.getAmount().toString());
        assertEquals(request.getTransactionAmount().getCurrency(), savedEvent.getCurrency());
        assertEquals(request.getTransactionAmount().getDebitOrCredit(), savedEvent.getDebitOrCredit());
        assertEquals(TransactionStatus.AUTH_FAIL, savedEvent.getTransactionStatus());
    }

    @Test
    void testAuthorizeApproved() throws Exception 
    {
        HashMap<String, BigDecimal> testBalances = new HashMap<>();
        
        testBalances.put("USD", new BigDecimal("450"));
        testBalances.put("INR", new BigDecimal("350"));

        when(userService.getBalances(any())).thenReturn(testBalances);

        String original_requested_amount = "350";

        Amount amt = new Amount(original_requested_amount, "INR", DebitCredit.DEBIT);
        AuthorizationRequest request = new AuthorizationRequest("luka", "jfa9q9f-23jfoiajfapoe39", amt);
        
        Object response = bankLedgerService.authorize(request);

        assertEquals(AuthorizationResponse.class, response.getClass());

        AuthorizationResponse resp = (AuthorizationResponse) response;

        assertEquals(request.getUserId(), resp.getUserId());
        assertEquals(request.getMessageId(), resp.getMessageId());
        assertEquals(testBalances.get("INR").toString(), resp.getBalance().getAmount());
        assertEquals(request.getTransactionAmount().getCurrency(), resp.getBalance().getCurrency());
        assertEquals(request.getTransactionAmount().getDebitOrCredit(), resp.getBalance().getDebitOrCredit());
        assertEquals(ResponseCode.APPROVED, resp.getResponse());

        TransactionEvent savedEvent = transactionEventRepository.findById(request.getMessageId()).orElse(null);

        assertNotNull(savedEvent);

        assertEquals(request.getUserId(), savedEvent.getUserId());
        assertEquals(request.getMessageId(), savedEvent.getMessageId());
        assertEquals(original_requested_amount, savedEvent.getAmount().toString());
        assertEquals(request.getTransactionAmount().getCurrency(), savedEvent.getCurrency());
        assertEquals(request.getTransactionAmount().getDebitOrCredit(), savedEvent.getDebitOrCredit());
        assertEquals(TransactionStatus.AUTH_SUCCESS, savedEvent.getTransactionStatus());
    }

    @Test
    void testAuthError() throws Exception
    {
        HashMap<String, BigDecimal> testBalances = new HashMap<>();
        
        testBalances.put("USD", new BigDecimal("450"));
        testBalances.put("INR", new BigDecimal("350"));

        when(userService.getBalances(any())).thenReturn(testBalances);

        Amount amt = new Amount("500", "USD", DebitCredit.CREDIT);
        AuthorizationRequest request = new AuthorizationRequest("c", "kjlfjgweiohjgwekognl,z.sfm", amt);
        
        Object response = bankLedgerService.authorize(request);

        assertEquals(ServerError.class, response.getClass());

        ServerError resp = (ServerError) response;

        assertEquals("Invalid DebitCredit Input", resp.getMessage());

        TransactionEvent savedEvent = transactionEventRepository.findById(request.getMessageId()).orElse(null);

        assertNotNull(savedEvent);

        assertEquals(request.getUserId(), savedEvent.getUserId());
        assertEquals(request.getMessageId(), savedEvent.getMessageId());
        assertEquals(request.getTransactionAmount().getAmount(), savedEvent.getAmount().toString());
        assertEquals(request.getTransactionAmount().getCurrency(), savedEvent.getCurrency());
        assertEquals(request.getTransactionAmount().getDebitOrCredit(), savedEvent.getDebitOrCredit());
        assertEquals(TransactionStatus.AUTH_FAIL, savedEvent.getTransactionStatus());
    }

    @Test
    void testLoadError() throws Exception
    {
        HashMap<String, BigDecimal> testBalances = new HashMap<>();
        
        testBalances.put("USD", new BigDecimal("450"));
        testBalances.put("INR", new BigDecimal("350"));

        when(userService.getBalances(any())).thenReturn(testBalances);

        Amount amt = new Amount("500", "USD", DebitCredit.DEBIT);
        LoadRequest request = new LoadRequest("d", "kjjseht89243gn802434gi0n", amt);
        
        Object response = bankLedgerService.load(request);

        assertEquals(ServerError.class, response.getClass());

        ServerError resp = (ServerError) response;

        assertEquals("Invalid DebitCredit Input", resp.getMessage());

        TransactionEvent savedEvent = transactionEventRepository.findById(request.getMessageId()).orElse(null);

        assertNotNull(savedEvent);

        assertEquals(request.getUserId(), savedEvent.getUserId());
        assertEquals(request.getMessageId(), savedEvent.getMessageId());
        assertEquals(request.getTransactionAmount().getAmount(), savedEvent.getAmount().toString());
        assertEquals(request.getTransactionAmount().getCurrency(), savedEvent.getCurrency());
        assertEquals(request.getTransactionAmount().getDebitOrCredit(), savedEvent.getDebitOrCredit());
        assertEquals(TransactionStatus.LOAD_FAIL, savedEvent.getTransactionStatus());
    }

    @Test
    void testLoadApproved() throws Exception 
    {
        HashMap<String, BigDecimal> testBalances = new HashMap<>();
        
        testBalances.put("USD", new BigDecimal("450"));
        testBalances.put("INR", new BigDecimal("350"));

        when(userService.getBalances(any())).thenReturn(testBalances);

        Amount amt = new Amount("500", "USD", DebitCredit.CREDIT);
        LoadRequest request = new LoadRequest("e", "kln;sdgh8t0249jw0-egr", amt);
        
        Object response = bankLedgerService.load(request);

        assertEquals(LoadResponse.class, response.getClass());

        LoadResponse resp = (LoadResponse) response;

        assertEquals(request.getUserId(), resp.getUserId());
        assertEquals(request.getMessageId(), resp.getMessageId());
        assertEquals(testBalances.get("USD").toString(), resp.getBalance().getAmount());
        assertEquals(request.getTransactionAmount().getCurrency(), resp.getBalance().getCurrency());
        assertEquals(request.getTransactionAmount().getDebitOrCredit(), resp.getBalance().getDebitOrCredit());

        TransactionEvent savedEvent = transactionEventRepository.findById(request.getMessageId()).orElse(null);

        assertNotNull(savedEvent);

        assertEquals(request.getUserId(), savedEvent.getUserId());
        assertEquals(request.getMessageId(), savedEvent.getMessageId());
        assertEquals("500", savedEvent.getAmount().toString());
        assertEquals(request.getTransactionAmount().getCurrency(), savedEvent.getCurrency());
        assertEquals(request.getTransactionAmount().getDebitOrCredit(), savedEvent.getDebitOrCredit());
        assertEquals(TransactionStatus.LOAD_SUCCESS, savedEvent.getTransactionStatus());
    }
    
}
