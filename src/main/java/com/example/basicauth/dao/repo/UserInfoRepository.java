package com.example.basicauth.repo;

import com.example.basicauth.model.UserInfo;
import com.example.basicauth.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {
    Optional<UserInfo> findByName(String username);


    Optional<UserInfo> findByEmail(String email);

    Optional<UserInfo> findByResetToken(String token);

    List<UserInfo> findByRole(UserRole role);
}
