package com.example.basicauth.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record SignUpRequest(
        @NotBlank String name,
        @NotBlank String surname,
        String address,
        Double salary,
        @NotBlank @Email String email,
        @NotBlank String password,
        Long departmentId) {
}
