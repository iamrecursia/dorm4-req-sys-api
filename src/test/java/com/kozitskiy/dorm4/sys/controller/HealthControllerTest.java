package com.kozitskiy.dorm4.sys.controller;

import com.kozitskiy.dorm4.sys.service.GreetingService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = HealthController.class)
@ActiveProfiles("test")
class HealthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GreetingService greetingService;

    @Test
    @DisplayName("GET /api/health returns UP")
    void healthReturnsUp() throws Exception {
        mockMvc.perform(get("/api/health"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status", is("UP")));
    }

    @Test
    @DisplayName("GET /api/hello returns greeting from service")
    void helloReturnsGreeting() throws Exception {
        when(greetingService.getGreeting("Jane")).thenReturn("Hello, Jane!");

        mockMvc.perform(get("/api/hello").param("name", "Jane"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message", is("Hello, Jane!")));
    }
}