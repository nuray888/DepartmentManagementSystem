package com.example.basicauth.service;

import com.example.basicauth.dao.model.UserInfo;
import com.example.basicauth.dao.model.UserRole;
import com.example.basicauth.dto.UserResponseDto;
import com.example.basicauth.dto.UserUpdateRequest;

import java.util.List;

public interface UserService {
//    UserResponseDto createUser(UserRequest userRequest);
    List<UserResponseDto> getUsers();
    UserResponseDto getUser(Long id);
    UserResponseDto updateUser(Long id, UserUpdateRequest userRequest);
    String deleteUser(Long id);
    void changeRole(Long userId, UserRole role);
    void isValidManager(Long departmentId, UserInfo manager);
    UserInfo getCurrentUser();
    String activateUser(Long id);
    String deactivateUser(Long id);
    UserInfo findByEmail(String email);

}
