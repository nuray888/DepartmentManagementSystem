package com.example.basicauth.controller;

import com.example.basicauth.dto.UserRequest;
import com.example.basicauth.dto.UserResponseDto;
import com.example.basicauth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/create")
    public ResponseEntity<UserResponseDto> create(@RequestBody UserRequest request) {
        return ResponseEntity.ok(userService.createUser(request));
    }
}
