package com.kozitskiy.dorm4.sys.service;

import org.springframework.stereotype.Service;

@Service
public class GreetingService {

    public String getGreeting(String name) {
        String safeName = (name == null || name.isBlank()) ? "World" : name.trim();
        return "Hello, " + safeName + "!";
    }
}