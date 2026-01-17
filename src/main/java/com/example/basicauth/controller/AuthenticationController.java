package com.example.basicauth.controller;

import com.example.basicauth.dto.*;
import com.example.basicauth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/auth")
public class AuthenticationController {
    private final AuthService service;
    @Operation(summary = "user signUp")
    @PostMapping("/signUp")
    public ResponseEntity<UserResponseDto> addNewUser(@Valid @RequestBody SignUpRequest request) {
        return  ResponseEntity.ok(service.signup(request));
    }

    @Operation(summary = "User login")
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> authenticateAndGetToken(@Valid @RequestBody AuthRequest authRequest) {
         return ResponseEntity.ok(service.login(authRequest));
    }

    @PostMapping("/send-verification-link")
    @Operation(summary = "Send verification link to user's email")
    public ResponseEntity<ApiResponse<Void>> sendVerification(@RequestBody VerificationRequest verificationRequest) {
        service.sendVerification(verificationRequest);
        return ResponseEntity.ok(ApiResponse.build(
                HttpStatus.OK,
                "Verification link sent successfully",
                null));
    }

    @GetMapping("/verify-profile")
    @Operation(summary = "Used for verify email address when register first time")
    public ResponseEntity<ApiResponse<JwtResponse>> verifyProfile(@RequestParam String token) {
        JwtResponse tokenResponse = service.verifyEmail(token);
        return ResponseEntity.ok(ApiResponse.build(
                HttpStatus.OK,
                "Profile verified successfully",
                tokenResponse));
    }


    @PostMapping("/refreshToken")
    public ResponseEntity<JwtResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        return ResponseEntity.ok(service.refreshToken(refreshTokenRequest));
    }

    @PutMapping("/changePassword")
    public ResponseEntity<ApiResponse<String>> changePassword(@Valid @RequestBody ChangePasswordDto request) {
        return ResponseEntity.ok(ApiResponse.build(HttpStatus.OK,"Password changes successfully," ,
                service.changePassword(request)
                ));
    }
    @Operation(summary = "User forgot password for changing link will be send in gmail")
    @PostMapping("/forgotPassword")
    public ResponseEntity<ApiResponse<String>> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        return ResponseEntity.ok(ApiResponse.build(HttpStatus.OK,"Reset link sent",
                service.forgotPassword(request)));
    }
    @Operation(summary = "User can change password")
    @PostMapping("/resetPassword")
    public ResponseEntity<String> resetPassword(@RequestParam String token,@Valid @RequestBody ResetPasswordRequest request) {
        return ResponseEntity.ok(service.resetPassword(token,request));
    }
    @Operation(summary = "User logout")
    @PostMapping("/log-out")
    public ResponseEntity<Void> logout(@RequestParam String refreshToken) {
        log.info("HELLLLLLLLLLLLLLO");
        service.logout(refreshToken);
        log.info("User successfully signed out.");
        return ResponseEntity.noContent().build();
    }

//    @GetMapping("/login/google")
//    @ResponseBody
//    public String user(OAuth2AuthenticationToken principal) {
//        return principal.getPrincipal().getAttribute("name");
////        return "Nuray Muxtarli";
//    }


//    @GetMapping("/success")
//    public String dashboard() {
//        return "OAuth2 login successful!";
//    }


}
