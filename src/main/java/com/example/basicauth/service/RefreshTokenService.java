package com.example.basicauth.service;

import com.example.basicauth.model.RefreshToken;
import com.example.basicauth.model.UserInfo;
import com.example.basicauth.repo.RefreshTokenRepository;
import com.example.basicauth.repo.UserInfoRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    private final UserInfoRepository userInfoRepository;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, UserInfoRepository userInfoRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userInfoRepository = userInfoRepository;
    }


    @Transactional
    public RefreshToken createRefreshToken(String username) {
        UserInfo user = userInfoRepository.findByName(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        RefreshToken refreshToken;
        if (refreshTokenRepository.findByUserInfo(user).isEmpty())
            refreshToken = RefreshToken.builder()
                    .userInfo(user)
                    .token(UUID.randomUUID().toString())
                    .expiryDate(Instant.now().plusSeconds(600))
                    .build();
        else {
            refreshToken = refreshTokenRepository.findByUserInfo(user).get();
            refreshToken.setExpiryDate(Instant.now().plusSeconds(600));
        }
        return refreshTokenRepository.save(refreshToken);
    }

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }



    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new RuntimeException(token.getToken() + " Refresh token was expired. Please make a new signin request");
        }
        return token;
    }

}