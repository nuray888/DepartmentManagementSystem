package com.example.basicauth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRequest(
        @NotBlank @Size(min = 3,max = 30) String name,
        @NotBlank @Size(min = 3,max = 30) String surname,
        String address,
        Double salary,
        @NotBlank @Email String email,
        @NotBlank String password,
        Long departmentId) {
}
