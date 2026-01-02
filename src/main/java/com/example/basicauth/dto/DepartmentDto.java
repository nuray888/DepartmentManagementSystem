package com.example.basicauth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DepartmentDto(
        @NotBlank @NotNull String name, String description, Long managerId) {
}
