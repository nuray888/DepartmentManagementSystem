package com.example.basicauth.dto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AuthRequest(
        @Email @NotBlank @Size(min = 3,max = 30) String email,
        @NotBlank String password
) {
}
