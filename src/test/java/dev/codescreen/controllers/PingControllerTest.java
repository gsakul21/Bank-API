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


@WebMvcTest(PingController.class)
public class PingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PingService service;

    @Test
    void shouldGiveTimestamp() throws Exception {
        when(service.ping()).thenReturn(new Ping(Instant.now()));
        this.mockMvc.perform(MockMvcRequestBuilders.get("/ping"))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().string(not("")));
    }


}
