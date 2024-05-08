package dev.codescreen.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import dev.codescreen.schemas.Amount;
import dev.codescreen.schemas.AuthorizationRequest;
import dev.codescreen.schemas.AuthorizationResponse;
import dev.codescreen.schemas.DebitCredit;
import dev.codescreen.schemas.LoadRequest;
import dev.codescreen.schemas.LoadResponse;
import dev.codescreen.schemas.ResponseCode;
import dev.codescreen.schemas.ServerError;
import dev.codescreen.service.BankLedgerService;

@WebMvcTest(BankLedgerController.class)
public class BankLedgerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    private BankLedgerService bankLedgerService;


    @Test
    void userIdIsNull() throws Exception {
        String input = "{\"messageId\": 12, \"userId\": null, \"transactionAmount\": { \"amount\": \"500\", \"currency\": \"USD\"}, \"debitOrCredit\": \"CREDIT\"}}";
    
        this.mockMvc.perform(put("/load")
        .contentType(MediaType.APPLICATION_JSON)
        .content(input))
        .andExpect(status().isBadRequest());
    }

    @Test
    void userIdIsEmpty() throws Exception {
        String input = "{\"messageId\": 12, \"userId\": \"\", \"transactionAmount\": { \"amount\": \"500\", \"currency\": \"USD\"}, \"debitOrCredit\": \"CREDIT\"}}";
    
        this.mockMvc.perform(put("/load")
        .contentType(MediaType.APPLICATION_JSON)
        .content(input))
        .andExpect(status().isBadRequest());
    }

    @Test
    void messateIdIsNull() throws Exception {
        String input = "{\"messageId\": null, \"userId\": 12, \"transactionAmount\": { \"amount\": \"500\", \"currency\": \"USD\"}, \"debitOrCredit\": \"CREDIT\"}}";
    
        this.mockMvc.perform(put("/load")
        .contentType(MediaType.APPLICATION_JSON)
        .content(input))
        .andExpect(status().isBadRequest());
    }

    @Test
    void messageIdIsEmpty() throws Exception {
        String input = "{\"messageId\": \"\", \"userId\": 12, \"transactionAmount\": { \"amount\": \"500\", \"currency\": \"USD\"}, \"debitOrCredit\": \"CREDIT\"}}";
    
        this.mockMvc.perform(put("/load")
        .contentType(MediaType.APPLICATION_JSON)
        .content(input))
        .andExpect(status().isBadRequest());
    }

    @Test
    void amountIsEmpty() throws Exception {
        String input = "{\"messageId\": 12, \"userId\": \"abcd\", \"transactionAmount\": { \"amount\": \"\", \"currency\": \"USD\"}, \"debitOrCredit\": \"CREDIT\"}}";
    
        this.mockMvc.perform(put("/load")
        .contentType(MediaType.APPLICATION_JSON)
        .content(input))
        .andExpect(status().isBadRequest());
    }

    @Test
    void amountIsNull() throws Exception {
        String input = "{\"messageId\": 12, \"userId\": \"abcd\", \"transactionAmount\": { \"amount\": null, \"currency\": \"USD\"}, \"debitOrCredit\": \"CREDIT\"}}";
    
        this.mockMvc.perform(put("/load")
        .contentType(MediaType.APPLICATION_JSON)
        .content(input))
        .andExpect(status().isBadRequest());
    }

    @Test
    void currencyIsNull() throws Exception {
        String input = "{\"messageId\": 12, \"userId\": \"abcd\", \"transactionAmount\": { \"amount\": \"500\", \"currency\": null}, \"debitOrCredit\": \"CREDIT\"}}";
    
        this.mockMvc.perform(put("/load")
        .contentType(MediaType.APPLICATION_JSON)
        .content(input))
        .andExpect(status().isBadRequest());
    }

    @Test
    void currencyIsEmpty() throws Exception {
        String input = "{\"messageId\": 12, \"userId\": \"abcd\", \"transactionAmount\": { \"amount\": \"500\", \"currency\": \"\"}, \"debitOrCredit\": \"CREDIT\"}}";
    
        this.mockMvc.perform(put("/load")
        .contentType(MediaType.APPLICATION_JSON)
        .content(input))
        .andExpect(status().isBadRequest());
    }

    @Test
    void debitOrCreditIsEmpty() throws Exception {
        String input = "{\"messageId\": 12, \"userId\": \"abcd\", \"transactionAmount\": { \"amount\": \"500\", \"currency\": \"USD\"}, \"debitOrCredit\": \"\"}}";
    
        this.mockMvc.perform(put("/load")
        .contentType(MediaType.APPLICATION_JSON)
        .content(input))
        .andExpect(status().isBadRequest());
    }

    @Test
    void debitOrCreditIsNull() throws Exception {
        String input = "{\"messageId\": 12, \"userId\": \"abcd\", \"transactionAmount\": { \"amount\": \"500\", \"currency\": \"USD\"}, \"debitOrCredit\": null}}";
    
        this.mockMvc.perform(put("/load")
        .contentType(MediaType.APPLICATION_JSON)
        .content(input))
        .andExpect(status().isBadRequest());
    }

    @Test
    void debitOrCreditIsWrong() throws Exception {
        String input = "{\"messageId\": 12, \"userId\": \"abcd\", \"transactionAmount\": { \"amount\": \"500\", \"currency\": \"USD\"}, \"debitOrCredit\": \"DEBIT\"}}";
    
        this.mockMvc.perform(put("/load")
        .contentType(MediaType.APPLICATION_JSON)
        .content(input))
        .andExpect(status().isBadRequest());
    }

    @Test
    void loadHandlesError() throws Exception {
        ServerError e = new ServerError("Test error message");
        when(bankLedgerService.load(any())).thenReturn(e);


        Amount data = new Amount("500", "USD", DebitCredit.DEBIT);
        LoadRequest request = new LoadRequest("a", "b", data);

        mockMvc.perform(put("/load")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("Test error message"));

    }

    @Test 
    void loadHandlesNormal() throws Exception {

        Amount data = new Amount("1000", "USD", DebitCredit.CREDIT);
        LoadResponse response = new LoadResponse("a", "b", data);

        when(bankLedgerService.load(any())).thenReturn(response);

        Amount e_data = new Amount("500", "USD", DebitCredit.CREDIT);
        LoadRequest request = new LoadRequest("a", "b", e_data);

        mockMvc.perform(put("/load")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
        .andDo(print())
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.userId").value(request.getUserId()))
        .andExpect(jsonPath("$.messageId").value(request.getMessageId()))
        .andExpect(jsonPath("$.balance.amount").value(data.getAmount()))
        .andExpect(jsonPath("$.balance.currency").value(data.getCurrency()))
        .andExpect(jsonPath("$.balance.debitOrCredit").value("CREDIT"));
    }

    @Test
    void authorizationHandlesError() throws Exception {
        ServerError e = new ServerError("Test error message");
        when(bankLedgerService.authorize(any())).thenReturn(e);


        Amount data = new Amount("500", "USD", DebitCredit.DEBIT);
        AuthorizationRequest request = new AuthorizationRequest("a", "b", data);

        mockMvc.perform(put("/authorization")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("Test error message"));

    }

    @Test 
    void authorizationHandlesNormal() throws Exception {

        Amount data = new Amount("500", "USD", DebitCredit.DEBIT);
        AuthorizationResponse response = new AuthorizationResponse("a", "b", ResponseCode.APPROVED, data);

        when(bankLedgerService.authorize(any())).thenReturn(response);

        Amount e_data = new Amount("500", "USD", DebitCredit.DEBIT);
        AuthorizationRequest request = new AuthorizationRequest("a", "b", e_data);

        mockMvc.perform(put("/authorization")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
        .andDo(print())
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.userId").value(request.getUserId()))
        .andExpect(jsonPath("$.messageId").value(request.getMessageId()))
        .andExpect(jsonPath("$.response").value("APPROVED"))
        .andExpect(jsonPath("$.balance.amount").value(data.getAmount()))
        .andExpect(jsonPath("$.balance.currency").value(data.getCurrency()))
        .andExpect(jsonPath("$.balance.debitOrCredit").value("DEBIT"));
    }
}
