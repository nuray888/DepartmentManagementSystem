package com.example.basicauth.dto;

import jakarta.validation.constraints.NotBlank;

public record ResetPasswordRequest(
        String token,
        @NotBlank String newPassword
) {

}
