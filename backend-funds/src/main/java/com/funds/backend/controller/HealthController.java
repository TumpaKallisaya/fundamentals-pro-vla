package com.funds.backend.controller;

import java.time.ZonedDateTime;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HealthController {

    @GetMapping("/health")
    public Map<String, Object> health() {
        return Map.of(
                "service", "backend-funds",
                "status", "UP",
                "timestamp", ZonedDateTime.now().toString()
        );
    }
}
