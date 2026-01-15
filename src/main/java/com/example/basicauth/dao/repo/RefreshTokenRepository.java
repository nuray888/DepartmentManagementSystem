package com.example.basicauth.dao.repo;

import com.example.basicauth.dao.model.RefreshToken;
import com.example.basicauth.dao.model.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Integer> {
    Optional<RefreshToken> findByToken(String token);
    @Modifying
    @Query("delete from RefreshToken r where r.userInfo = :user")
    void deleteByUserInfo(UserInfo user);

    void deleteByToken(String token);

    Optional<RefreshToken> findByUserInfo(UserInfo userInfo);
}