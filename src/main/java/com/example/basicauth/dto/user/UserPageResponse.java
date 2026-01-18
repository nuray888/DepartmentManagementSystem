package com.example.basicauth.dto.user;

import java.util.List;

public record UserPageResponse(
        List<UserResponseDto> users,
         Integer pageNumber,
         Integer pageSize,
         Long totalElements,
         Integer totalPages,
         boolean lastPage
) {
}
