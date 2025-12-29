package com.example.basicauth.service;

import com.example.basicauth.config.UserInfoDetails;
import com.example.basicauth.model.UserInfo;
import com.example.basicauth.repo.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;


@Component
public class UserInfoUserDetailsService implements UserDetailsService {

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserInfo byName = userInfoRepository.findByName(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new UserInfoDetails(byName);
    }

}
