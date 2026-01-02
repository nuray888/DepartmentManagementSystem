package com.example.basicauth.service;

import com.example.basicauth.config.UserInfoDetails;
import com.example.basicauth.dto.ChangePasswordDto;
import com.example.basicauth.dto.ForgotPasswordRequest;
import com.example.basicauth.dto.ResetPasswordRequest;
import com.example.basicauth.dto.SignUpRequest;
import com.example.basicauth.model.UserInfo;
import com.example.basicauth.model.UserRole;
import com.example.basicauth.repo.RefreshTokenRepository;
import com.example.basicauth.repo.UserInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final PasswordEncoder passwordEncoder;
    private final UserInfoRepository repository;
    private final EmailService emailService;
    private final RefreshTokenRepository refreshTokenRepository;

    public UserInfo addUser(SignUpRequest request) {
        UserInfo userInfo = UserInfo.builder().name(request.name())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(UserRole.ROLE_USER).build();
        UserInfo save = repository.save(userInfo);
        System.out.println(save);
        return save;
    }

    public String changePassword(ChangePasswordDto request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserInfoDetails userDetails = (UserInfoDetails) auth.getPrincipal();

        UserInfo user = repository.findByName(userDetails.getUsername())
                .orElseThrow();

        String newHashedPassword = passwordEncoder.encode(request.newPassword());

        user.setPassword(newHashedPassword);
//        user.setPasswordLastChangedTime(LocalDateTime.now());

        repository.save(user);

        return "Password changed";
    }

    public String forgotPassword(ForgotPasswordRequest request) {
        UserInfo user = repository.findByEmail(request.email()).orElseThrow();
        String token = UUID.randomUUID().toString();
        user.setResetToken(token);
        user.setResetTokenExpiry(LocalDateTime.now().plusMinutes(15));
        repository.save(user);
        String link = "http://localhost:4043/reset-password?token=" + token;
        emailService.sendEmail(request.email(), "Parol deyismek", "Parolu dəyişmək üçün link: \n" + link);
        return "Reset password email sent";
    }

    public String resetPassword(ResetPasswordRequest request) {
        UserInfo user = repository.findByResetToken(request.token())
                .orElseThrow(() -> new RuntimeException("Invalid or used token"));

        if (user.getResetTokenExpiry().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token expired");
        }
        String newHashedPassword = passwordEncoder.encode(request.newPassword());
        user.setPassword(newHashedPassword);
        user.setResetToken(null);
        user.setResetTokenExpiry(null);
        repository.save(user);

        return "Password successfully changed";
    }
    @Transactional
    public void logout(String refreshToken){
        log.info("HELLO");
        if(refreshToken==null){
            throw new RuntimeException();
        }
        refreshTokenRepository.deleteByToken(refreshToken);
        log.info("User successfully signed out.");
    }

    @PreAuthorize(value = "ADMIN")
    public UserInfo getUserById(Long id) {
        return repository.findById(id).orElse(null);
    }


}
