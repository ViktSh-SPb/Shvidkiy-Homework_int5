package org.example.gatewayservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class FallbackController {
    @GetMapping("/fallback/users")
    public ResponseEntity<Map<String, Object>> userServiceFallback(){
        return ResponseEntity
                .status(503)
                .body(Map.of("status", "Ошибка", "message", "user-service недоступен"));
    }

    @GetMapping("/fallback/employees")
    public ResponseEntity<Map<String, Object>> employeeServiceFallback(){
        return ResponseEntity
                .status(503)
                .body(Map.of("status", "Ошибка", "message", "employee-service недоступен"));
    }
}
