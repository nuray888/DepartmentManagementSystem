package com.example.basicauth.service.oauth2;

import com.example.basicauth.model.RefreshToken;
import com.example.basicauth.service.JwtService;
import com.example.basicauth.service.RefreshTokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {
    private final JwtService jwtService;

    private final RefreshTokenService refreshTokenService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        OidcUser oidcUser = (OidcUser) authentication.getPrincipal();
        String name = oidcUser.getName();

        String accessToken = jwtService.generateToken(name);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(name);

        String jsonResponse = String.format(
                "{\"accessToken\": \"%s\", \"refreshToken\": \"%s\"}",
                accessToken,
                refreshToken.getToken()
        );
        response.getWriter().write(jsonResponse);
        response.getWriter().flush();
    }
}