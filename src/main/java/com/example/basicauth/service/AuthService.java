package com.example.basicauth.service;

import com.example.basicauth.dto.auth.*;
import com.example.basicauth.dto.user.UserResponseDto;

public interface AuthService {
    UserResponseDto signup(SignUpRequest request);
    JwtResponse verifyEmail(String token);
    JwtResponse refreshToken(RefreshTokenRequest refreshTokenRequest);
    JwtResponse login(AuthRequest authRequest);
    String changePassword(ChangePasswordDto request);
    String forgotPassword(ForgotPasswordRequest request);
    void sendVerification(VerificationRequest email);
    String resetPassword(String token, ResetPasswordRequest request);
    void logout(String refreshToken);

}
