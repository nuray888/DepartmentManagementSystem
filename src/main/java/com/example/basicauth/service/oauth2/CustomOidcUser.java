package com.example.basicauth.service.oauth2;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public record CustomOidcUser(OidcUser delegate, Long id, String username, String email,
                             String role) implements OidcUser {

    @Override
    public Map<String, Object> getAttributes() {
        return delegate.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return delegate.getAuthorities();
    }

    @Override
    public String getName() {
        return username;
    }

    @Override
    public Map<String, Object> getClaims() {
        Map<String, Object> claims = new HashMap<>(delegate.getClaims());
        claims.put("dbUserId", id);
        claims.put("role", role);
        claims.put("name", username);
        claims.put("email", email);
        return claims;
    }

    @Override
    public OidcUserInfo getUserInfo() {
        return delegate.getUserInfo();
    }

    @Override
    public OidcIdToken getIdToken() {
        return delegate.getIdToken();
    }
}