package com.example.basicauth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserRequest(
        @NotBlank String name,
        @NotBlank String surname,
        String address,
        Double salary,
        @NotBlank @Email String email,
        @NotBlank String password,
        Long departmentId) {
}
