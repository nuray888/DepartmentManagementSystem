package com.example.basicauth.controller;

import com.example.basicauth.dto.*;
import com.example.basicauth.model.RefreshToken;
import com.example.basicauth.model.UserInfo;
import com.example.basicauth.service.JwtService;
import com.example.basicauth.service.RefreshTokenService;
import com.example.basicauth.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;

    private final RefreshTokenService refreshTokenService;

    private final JwtService jwtService;
    private final AuthService service;

    @PostMapping("/signUp")
    public ResponseEntity<UserInfo> addNewUser(@RequestBody SignUpRequest request) {
        ResponseEntity<UserInfo> ok = ResponseEntity.ok(service.addUser(request));
        log.info("User {} has been added", request.name());
        return ok;
    }

    @PostMapping("/login")
    public JwtResponse authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.username(), authRequest.password()));
        if (authentication.isAuthenticated()) {
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(authRequest.username());

            return JwtResponse.builder()
                    .accessToken(jwtService.generateToken(authRequest.username()))
                    .token(refreshToken.getToken())
                    .build();
        } else {
            throw new UsernameNotFoundException("invalid user request !");
        }
    }


    @PostMapping("/refreshToken")
    public JwtResponse refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        return refreshTokenService.findByToken(refreshTokenRequest.token())
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUserInfo)
                .map(userInfo -> {
                    String accessToken = jwtService.generateToken(userInfo.getName());
                    return JwtResponse.builder()
                            .accessToken(accessToken)
                            .token(refreshTokenRequest.token()) //returns same token
                            .build();
                }).orElseThrow(() -> new RuntimeException(
                        "Refresh token is not in database!"));
    }

    @PatchMapping("/changePassword")
    public ResponseEntity<String> changePassword(@RequestBody ChangePasswordDto request) {
        return ResponseEntity.ok(service.changePassword(request));
    }

    @PostMapping("/forgotPassword")
    public ResponseEntity<String> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        return ResponseEntity.ok(service.forgotPassword(request));
    }

    @PostMapping("/resetPassword")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordRequest request) {
        return ResponseEntity.ok(service.resetPassword(request));
    }

    @PostMapping("/log-out")
    public ResponseEntity<Void> logout(@RequestParam String refreshToken) {
        log.info("HELLLLLLLLLLLLLLO");
        service.logout(refreshToken);
        log.info("User successfully signed out.");
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/login/google")
    @ResponseBody
    public String user(OAuth2AuthenticationToken principal) {
        return principal.getPrincipal().getAttribute("name");
//        return "Nuray Muxtarli";
    }


    @GetMapping("/success")
    public String dashboard() {
        return "OAuth2 login successful!";
    }


}
