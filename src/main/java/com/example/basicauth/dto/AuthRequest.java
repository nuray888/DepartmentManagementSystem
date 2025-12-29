package com.example.basicauth.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record AuthRequest(
        @Column(unique = true)
        @NotNull @NotEmpty  String username,
        String password
) {
}
