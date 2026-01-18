package com.example.basicauth.dto.auth;

import lombok.Builder;

@Builder
public record JwtResponse(
        String accessToken,
        String token
) {
}