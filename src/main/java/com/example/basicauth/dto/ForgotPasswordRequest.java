package com.example.basicauth.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@JsonIgnoreProperties(ignoreUnknown = true)
public record ForgotPasswordRequest(
        String email
) {
}
