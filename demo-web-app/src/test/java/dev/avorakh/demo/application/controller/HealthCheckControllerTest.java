package dev.avorakh.demo.application.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(HealthCheckController.class)
class HealthCheckControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldSuccessfullyReturnOkSCAndSuccessWithTrueInBody() throws Exception {

        var request = get("/status").accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").isBoolean())
                .andExpect(jsonPath("$.success").value(true));
    }
}
