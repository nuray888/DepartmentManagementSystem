package com.example.basicauth.dto;

import jakarta.persistence.Column;

public record SignUpRequest(
        @Column(unique = true) String name,
        String email,
        String password
) {
}
