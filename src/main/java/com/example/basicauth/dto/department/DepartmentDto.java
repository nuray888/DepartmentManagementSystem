package com.example.basicauth.dto.department;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DepartmentDto(
        @NotBlank @NotNull String name, String description, Long managerId) {
}
