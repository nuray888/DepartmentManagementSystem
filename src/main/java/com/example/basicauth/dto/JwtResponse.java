package com.example.basicauth.dto;

import lombok.Builder;

@Builder
public record JwtResponse(
        String accessToken,
        String token
) {
}