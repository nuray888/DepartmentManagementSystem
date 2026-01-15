package com.example.basicauth.service.impl;

import com.example.basicauth.config.UserInfoDetails;
import com.example.basicauth.dao.model.Department;
import com.example.basicauth.dao.model.RefreshToken;
import com.example.basicauth.dao.repo.DepartmentRepository;
import com.example.basicauth.dto.*;
import com.example.basicauth.dao.model.UserInfo;
import com.example.basicauth.dao.model.UserRole;
import com.example.basicauth.dao.repo.RefreshTokenRepository;
import com.example.basicauth.dao.repo.UserInfoRepository;
import com.example.basicauth.exception.*;
import com.example.basicauth.mapper.UserMapper;
import com.example.basicauth.service.AuthService;
import com.example.basicauth.service.EmailService;
import com.example.basicauth.service.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.authentication.AuthenticationManager;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {
    private final PasswordEncoder passwordEncoder;
    private final UserInfoRepository repository;
    private final EmailService emailService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final DepartmentRepository departmentRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserMapper mapper;
    private UserDetails userDetails;
//    public UserResponseDto signup(SignUpRequest request) {
//
//        if (repository.existsByEmail(request.email())) {
//            throw new RuntimeException("Email already exists");
//        }
//
//        Department department = null;
//        if (request.departmentId() != null) {
//            department = departmentRepository
//                    .findById(request.departmentId())
//                    .orElseThrow(() -> new RuntimeException("Department not found"));
//        }
//
//        UserInfo userInfo = UserInfo.builder()
//                .name(request.name())
//                .surname(request.surname())
//                .address(request.address())
//                .salary(request.salary())
//                .email(request.email())
//                .password(passwordEncoder.encode(request.password()))
//                .department(department)
//                .role(UserRole.ROLE_USER)
//                .build();
//
//        UserInfo save = repository.save(userInfo);
//        return mapper.userToDto(save);
//    }

    public UserResponseDto signup(SignUpRequest request) {
        if (repository.existsByEmail(request.email())) {
            throw new APIException("Email already exists");
        }
        Department department;
            department=departmentRepository.findById(request.departmentId()).orElse(null);
        UserInfo userInfo = UserInfo.builder().name(request.name())
                .surname(request.surname())
                .address(request.address())
                .salary(request.salary())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .isEmailVerified(false)
                .department(department)
                .role(UserRole.ROLE_USER).build();
        UserInfo save = repository.save(userInfo);
        System.out.println(save);
        return mapper.userToDto(save);
    }

    public JwtResponse verifyEmail(String token) {
        UserInfo user = repository.findByVerificationToken(token);
        if (user == null) {
            throw new RuntimeException("Verification token is invalid");
        }
        user.setIsEmailVerified(true);
        user.setVerificationToken(null);
        repository.save(user);
        RefreshToken refreshToken = jwtService.createRefreshToken(user.getEmail());
        String jwt = jwtService.generateAccessToken(user.getEmail());

        return JwtResponse.builder()
                .accessToken(jwt)
                .token(refreshToken.getToken())
                .build();
    }

    public JwtResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        // DB-dən refresh token tap və yoxla
        RefreshToken refreshToken = jwtService.findByRefreshToken(refreshTokenRequest.token())
                .orElseThrow(() -> new RuntimeException("Refresh token is not in database!"));

        jwtService.verifyRefreshTokenExpiration(refreshToken);

        UserInfo user = refreshToken.getUserInfo();

        String accessToken = jwtService.generateAccessToken(user.getEmail());

        return JwtResponse.builder()
                .accessToken(accessToken)
                .token(refreshToken.getToken()) // eyni refresh token geri qaytarılır
                .build();
    }
    public JwtResponse login(AuthRequest authRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.email(), authRequest.password())
            );
        } catch (BadCredentialsException | UsernameNotFoundException ex) {
            // Burada artıq sənin exception handler-a düşəcək
            throw new InvalidCredentialsException("Email və ya şifrə yanlışdır!");
        }

        UserInfo userInfo = repository.findByEmail(authRequest.email())
                .orElseThrow(() -> new InvalidCredentialsException("Bu istifadəçi tapılmadı!"));

        validateUser(userInfo);

        RefreshToken refreshToken = jwtService.createRefreshToken(authRequest.email());
        String accessToken = jwtService.generateAccessToken(authRequest.email());

        return JwtResponse.builder()
                .accessToken(accessToken)
                .token(refreshToken.getToken())
                .build();
    }



//    public JwtResponse login(AuthRequest authRequest) {
//        try{
//        Authentication authentication = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(authRequest.email(), authRequest.password())
//        );}catch (InvalidCredentialsException ex){
//            throw new InvalidCredentialsException(ex.getMessage());
//        }
//
//        if (!authentication.isAuthenticated()) {
//            throw new ResourceNotFoundException("Invalid user request!");
//        }
//        UserInfo userInfo = repository.findByEmail(authRequest.email()).orElseThrow(()->new InvalidCredentialsException("This user doesn't exist in db."));
//        validateUser(authRequest, userInfo);
//
//        // Refresh token yarat
//        RefreshToken refreshToken = jwtService.createRefreshToken(authRequest.email());
//
//        // Access token yarat
//        String accessToken = jwtService.generateAccessToken(authRequest.email());
//
//        return JwtResponse.builder()
//                .accessToken(accessToken)
//                .token(refreshToken.getToken())
//                .build();
//    }

    private void validateUser( UserInfo user) {
        if (user.getIsDeleted())
            throw new ResourceNotFoundException("User is deleted");

        if (!user.getIsActive())
            throw new UserBlockedException("User is blocked");

        if (!user.getIsEmailVerified()) {
            throw new InvalidCredentialsException("User is not verified");
        }

        repository.save(user);
    }



//    @Transactional
//    public String changePassword(ChangePasswordDto request) {
//        UserInfo currentUser = getCurrentUser();
//
//        String newHashedPassword = passwordEncoder.encode(request.newPassword());
//
//        currentUser.setPassword(newHashedPassword);
////        user.setPasswordLastChangedTime(LocalDateTime.now());
//
//        repository.save(currentUser);
//
//        return "Password changed";
//    }

    public String changePassword(ChangePasswordDto request) {
        UserInfo user = getCurrentUser();
        if (!passwordEncoder.matches(request.oldPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Old password is wrong");
        }
        String newHashedPassword = passwordEncoder.encode(request.newPassword());

        user.setPassword(newHashedPassword);
//        user.setPasswordLastChangedTime(LocalDateTime.now());

        repository.save(user);

        return "Password changed";
    }

    @Transactional
    public String forgotPassword(ForgotPasswordRequest request) {
        UserInfo user = repository.findByEmail(request.email()).orElseThrow();
        String token = UUID.randomUUID().toString();
        user.setResetToken(token);
        user.setResetTokenExpiry(LocalDateTime.now().plusMinutes(15));
        repository.save(user);
        String link = "http://localhost:8080/reset-password?token=" + token;
        emailService.sendForgotPasswordEmail(request.email(),link);
        return "Reset password email sent";
    }
    @Transactional
    public void sendVerification(String email) {
        UserInfo user = repository.findByEmail(email).orElseThrow();
        String verificationToken = UUID.randomUUID().toString();
        String verificationLink="http://localhost:8080/verify-email?token=" + verificationToken;
        user.setVerificationToken(verificationToken);
        repository.save(user);
        emailService.sendVerificationEmail(email,verificationLink);
    }


    @Transactional
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

    public UserInfo getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserInfoDetails userDetails = (UserInfoDetails) auth.getPrincipal();

        return repository.findByEmail(userDetails.getEmail())
                .orElseThrow(()->new UserNotLoginException("User not login"));
    }



}
