package com.example.basicauth.dto;

import jakarta.validation.constraints.Size;

public record UserUpdateRequest(
        @Size(min = 3, max = 30) String name,
        @Size(min = 3, max = 30) String surname,
        String address, Double salary, Long departmentId) {
}
