package com.example.basicauth.dto;

public record ChangePasswordDto(
        String oldPassword,
        String newPassword

) {
}