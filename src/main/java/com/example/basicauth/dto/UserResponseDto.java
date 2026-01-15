package com.example.basicauth.dto;

import com.example.basicauth.dao.model.UserRole;

public record UserResponseDto(
        String name,
        String surname,
        String address,
        Double salary,
        String email,
        Long departmentId,
        Boolean isDeleted,
        Boolean isActive,
        UserRole role) {
}