package dev.codescreen.controllers;

import static org.hamcrest.Matchers.not;
import static org.mockito.Mockito.when;

import java.time.Instant;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.*;
import org.springframework.test.web.servlet.result.*;

import dev.codescreen.schemas.Ping;
import dev.codescreen.service.PingService;

/*
 * This class contains the tests pertaining to the PingController of the service.
 * Since it handles one thing and Instant does not throw errors, there is 
 * just one test.
 */

@WebMvcTest(PingController.class)
public class PingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PingService service;

    @Test
    void shouldGiveTimestamp() throws Exception {
        // Mock response for a normal ping call.

        when(service.ping()).thenReturn(new Ping(Instant.now()));

        //Evaluate GET request and make sure format and information are present.

        this.mockMvc.perform(MockMvcRequestBuilders.get("/ping"))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().string(not("")));
    }


}
