package com.example.basicauth.service.oauth2;

import com.example.basicauth.model.UserInfo;
import com.example.basicauth.model.UserRole;
import com.example.basicauth.repo.UserInfoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends OidcUserService {
    private final UserInfoRepository userInfoRepository;

    @Override
    @Transactional
    public OidcUser loadUser(OidcUserRequest userRequest) {
        OidcUser oidcUser = super.loadUser(userRequest);

        String email = oidcUser.getAttribute("email");
        String name = oidcUser.getAttribute("name");
        String googleId = oidcUser.getAttribute("sub");

        Optional<UserInfo> byEmail = userInfoRepository.findByEmail(email);

        UserInfo user;

        if (byEmail.isPresent()) {
            user = byEmail.get();
            user.setGoogleId(googleId);
            userInfoRepository.save(user);
        } else {
            user = UserInfo.builder()
                    .email(email)
                    .name(name)
                    .googleId(googleId)
                    .role(UserRole.ROLE_USER)
                    .build();
            userInfoRepository.save(user);
        }

        return new CustomOidcUser(
                oidcUser,
                user.getId(),
                name,
                email,
                user.getRole().name());
    }
}