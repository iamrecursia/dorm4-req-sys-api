package com.kozitskiy.dorm4.sys.controller;

import com.kozitskiy.dorm4.sys.service.GreetingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping(path = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Health", description = "Health check and greeting endpoints")
public class HealthController {

    private final GreetingService greetingService;

    public HealthController(GreetingService greetingService) {
        this.greetingService = greetingService;
    }

    @GetMapping("/health")
    @Operation(summary = "Health check", description = "Returns application health status")
    public Map<String, String> health() {
        return Map.of("status", "UP");
    }

    @GetMapping("/hello")
    @Operation(summary = "Greeting", description = "Returns a greeting message")
    public Map<String, String> hello(@RequestParam(name = "name", required = false, defaultValue = "World") String name) {
        return Map.of("message", greetingService.getGreeting(name));
    }
}