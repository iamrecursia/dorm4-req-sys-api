package com.kozitskiy.dorm4.sys.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GreetingServiceTest {

    private final GreetingService greetingService = new GreetingService();

    @Test
    @DisplayName("Returns default greeting when name is null/blank")
    void defaultGreetingForNullOrBlank() {
        assertEquals("Hello, World!", greetingService.getGreeting(null));
        assertEquals("Hello, World!", greetingService.getGreeting("   "));
    }

    @Test
    @DisplayName("Trims name and returns personalized greeting")
    void trimsAndGreetsByName() {
        assertEquals("Hello, Alice!", greetingService.getGreeting(" Alice "));
        assertEquals("Hello, Bob!", greetingService.getGreeting("Bob"));
    }
}