package com.example.basicauth.dto;

public record ResetPasswordRequest(
        String token,
        String newPassword
) {

}
