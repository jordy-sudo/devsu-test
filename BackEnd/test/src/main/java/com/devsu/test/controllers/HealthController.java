package com.devsu.test.controllers;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class HealthController {

    private final JdbcTemplate jdbcTemplate;

    public HealthController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping("/health")
    public Map<String, Object> health() {
        String dbTime = jdbcTemplate.queryForObject("select now()::text", String.class);

        return Map.of(
                "status", "OK",
                "database", "CONNECTED",
                "dbTime", dbTime
        );
    }
}
