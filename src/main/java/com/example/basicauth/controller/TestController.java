package com.example.basicauth.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
public class TestController {

    // USER roluna sahib endpoint
    @GetMapping("/user")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> userAccess() {
        return ResponseEntity.ok("Hello User! You have access.");
    }

    // ADMIN roluna sahib endpoint
    @GetMapping("/admin")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> adminAccess() {
        return ResponseEntity.ok("Hello Admin! You have access.");
    }

    // Hər kəs üçün açıq endpoint
    @GetMapping("/all")
    public ResponseEntity<String> publicAccess() {
        return ResponseEntity.ok("Hello! This endpoint is public.");
    }
}
