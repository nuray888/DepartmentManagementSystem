package com.example.basicauth.dao.repo;

import com.example.basicauth.dao.model.UserInfo;
import com.example.basicauth.dao.model.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {
    Optional<UserInfo> findByName(String username);


    Optional<UserInfo> findByEmail(String email);

    Optional<UserInfo> findByResetToken(String token);

    List<UserInfo> findByRole(UserRole role);


    UserInfo findByVerificationToken(String token);

    boolean existsByEmail(@NotBlank @Email String email);
}
