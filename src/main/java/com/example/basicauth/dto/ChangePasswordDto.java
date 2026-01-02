package com.example.basicauth.dto;

import jakarta.validation.constraints.NotBlank;

public record ChangePasswordDto(
        @NotBlank String oldPassword,
        @NotBlank String newPassword

) {
}