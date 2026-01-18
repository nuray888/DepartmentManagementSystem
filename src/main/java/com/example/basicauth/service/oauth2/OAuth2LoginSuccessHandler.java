package com.example.basicauth.service.oauth2;

import com.example.basicauth.dao.model.RefreshToken;
import com.example.basicauth.service.security.JwtService;
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
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        OidcUser oidcUser = (OidcUser) authentication.getPrincipal();
        String email = oidcUser.getEmail();

        String accessToken = jwtService.generateAccessToken(email);
        RefreshToken refreshToken = jwtService.createRefreshToken(email);

        String jsonResponse = String.format(
                "{\"accessToken\": \"%s\", \"refreshToken\": \"%s\"}",
                accessToken,
                refreshToken.getToken()
        );
        response.getWriter().write(jsonResponse);
        response.getWriter().flush();
    }
}