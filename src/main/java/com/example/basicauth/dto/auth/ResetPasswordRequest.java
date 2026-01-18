package com.example.basicauth.dto.auth;

import jakarta.validation.constraints.NotBlank;

public record ResetPasswordRequest(
        @NotBlank String newPassword,
        @NotBlank String confirmPassword
) {

}
