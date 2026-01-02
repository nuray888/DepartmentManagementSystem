package com.example.basicauth.service;

import com.example.basicauth.dto.UserRequest;
import com.example.basicauth.dto.UserResponseDto;

public interface UserService {
    UserResponseDto createUser(UserRequest userRequest);

}
