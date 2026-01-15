package com.example.basicauth.init;

import com.example.basicauth.dao.model.UserInfo;
import com.example.basicauth.dao.model.UserRole;
import com.example.basicauth.dao.repo.UserInfoRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Getter
@Setter
@Slf4j
public class AdminInitializer {
    private final UserInfoRepository repository;
    private final PasswordEncoder encoder;

    @Value("${admin.name}")
    private String name;
    @Value("${admin.surname}")
    private String surname;
    @Value("${admin.password}")
    private String password;
    @Value("${admin.email}")
    private String email;

    @EventListener(ApplicationReadyEvent.class)
    public void init(){
        repository.findByEmail(email).ifPresentOrElse(
                user -> log.info("Admin already exists with email: {}", email),
                () -> {
                    UserInfo admin = UserInfo.builder()
                            .name(name)
                            .surname(surname)
                            .email(email)
                            .password(encoder.encode(password))
                            .isActive(true)
                            .isDeleted(false)
                            .isEmailVerified(true)
                            .role(UserRole.ROLE_ADMIN)
                            .build();

                    repository.save(admin);
                    log.info("Admin created with email: {}", email);
                }
        );
    }



}
