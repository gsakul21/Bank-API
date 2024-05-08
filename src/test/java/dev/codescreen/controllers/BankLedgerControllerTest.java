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

/*
 * This test suite focuses on evaluating and ensuring the reliability and
 * integration of the BankLedgerController which is responsible for routing 
 * and dealing with the/load and /authorization endpoints of the service. 
 */

@WebMvcTest(BankLedgerController.class)
public class BankLedgerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    private BankLedgerService bankLedgerService;

    /*
     * Tests that essentially verify that input validation to endpoints works. Check
     * title of test to see which aspect of input each test is verifying.
     */


    @Test
    void userIdIsNull() throws Exception {

        // Formulate request to load in the proper structure, but with invalid field.

        String input = "{\"messageId\": 12, \"userId\": null, \"transactionAmount\": { \"amount\": \"500\", \"currency\": \"USD\"}, \"debitOrCredit\": \"CREDIT\"}}";
    
        // Call endpoint and verify ServerError response.

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

    // INTEGRATION TESTS

    /*
     * Checking the case where load throws a server error, does the controller
     * return a server error with the appropriate fields.
     */ 

    @Test
    void loadHandlesError() throws Exception {

        // Mock error response from bankLedgerService for load function.

        ServerError e = new ServerError("Test error message");
        when(bankLedgerService.load(any())).thenReturn(e);

        // Format/build Load Request

        Amount data = new Amount("500", "USD", DebitCredit.DEBIT);
        LoadRequest request = new LoadRequest("a", "b", data);

        /*
         * Call endpoint and verify that a ServerError is given and has the
         * correct information
         */
        mockMvc.perform(put("/load")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("Test error message"));

    }

   /*
    * When the load is successful and the service returns the LoadResponse object,
    * does the controller format and return the appropriate structure with the
    * correct values in all of the required fields.
    */

    @Test 
    void loadHandlesNormal() throws Exception {

        // Synthesize service response for a successful load.

        Amount data = new Amount("1000", "USD", DebitCredit.CREDIT);
        LoadResponse response = new LoadResponse("a", "b", data);

        when(bankLedgerService.load(any())).thenReturn(response);

        // Formulate request that would lead to synthesized response.

        Amount e_data = new Amount("500", "USD", DebitCredit.CREDIT);
        LoadRequest request = new LoadRequest("a", "b", e_data);

        /*
         * Make call to /load endpoint and ensure proper LoadResponse is given
         * with the required fields and correct values.
         */

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
    
    /*
     * Does the controller return the appropriate ServerError structure if
     * the authorization function of the service throws an error and contains
     * the correct fields.
     */

    @Test
    void authorizationHandlesError() throws Exception {

        // Synthesize error response from authorization function.

        ServerError e = new ServerError("Test error message");
        when(bankLedgerService.authorize(any())).thenReturn(e);

        // Build authorization request for call.

        Amount data = new Amount("500", "USD", DebitCredit.DEBIT);
        AuthorizationRequest request = new AuthorizationRequest("a", "b", data);

        /*
         * Call endpoint and ensure that a ServerError is returned, containing
         * the required fields as well as correct information.
         */

        mockMvc.perform(put("/authorization")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("Test error message"));

    }

    /*
     * Testing if under authorization being passed, does the controller return
     * the correct structure. Checking to see if it returns a correctly formatted
     * AuthorizationResponse with the correct information in the required fields.
     */

    @Test 
    void authorizationHandlesNormal() throws Exception {

        // Formulate a response to a successful authorization.

        Amount data = new Amount("500", "USD", DebitCredit.DEBIT);
        AuthorizationResponse response = new AuthorizationResponse("a", "b", ResponseCode.APPROVED, data);

        when(bankLedgerService.authorize(any())).thenReturn(response);

        // Construct request that would lead to synthesized output.

        Amount e_data = new Amount("500", "USD", DebitCredit.DEBIT);
        AuthorizationRequest request = new AuthorizationRequest("a", "b", e_data);

        /*
         * Call endpoint, ensure that a AuthorizationResponse is returned with
         * all of the required fields and all of the correct values are there
         */
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
