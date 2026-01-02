package com.example.basicauth.dto;

public record UserResponseDto(
        String name,
        String surname,
        String address,
        Double salary,
        String email,
        Long departmentId,
        Boolean isDeleted,
        Boolean isActive) {
}