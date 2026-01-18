package com.example.basicauth.controller;

import com.example.basicauth.dao.model.UserRole;
import com.example.basicauth.dto.user.UserPageResponse;
import com.example.basicauth.dto.user.UserResponseDto;
import com.example.basicauth.dto.user.UserUpdateRequest;
import com.example.basicauth.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService service;

//    @Operation(summary = "Create a new user")
//    @PostMapping("/create")
//    @PreAuthorize("hasRole('ROLE_MANAGER,ROLE_ADMIN')")
//    public ResponseEntity<UserResponseDto> create(@Valid @RequestBody UserRequest request) {
//        return ResponseEntity.ok(service.createUser(request));
//    }
    @Operation(summary = "Shows all users")
    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<UserPageResponse> getAllUsers(@RequestParam(defaultValue = "0") Integer pageNumber,
                                        @RequestParam(defaultValue = "10") Integer pageSize,
                                        @RequestParam(defaultValue = "id") String sortBy,
                                        @RequestParam(defaultValue = "desc") String orderBy) {
        return ResponseEntity.ok(service.getUsers(pageNumber,pageSize,sortBy,orderBy));
    }

    @Operation(summary = "Show user by id")
    @GetMapping("/get/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<UserResponseDto> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(service.getUser(id));
    }


    @Operation(summary = "Update a user")
    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ROLE_MANAGER,ROLE_ADMIN')")
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable Long id, @Valid @RequestBody UserUpdateRequest userRequest) {
        return ResponseEntity.ok(service.updateUser(id, userRequest));
    }

    @Operation(summary = "Delete a user")
    @DeleteMapping("/delete/{userId}")
    @PreAuthorize("hasRole('ROLE_MANAGER,ROLE_ADMIN')")
    public ResponseEntity<String> deleteUser(@PathVariable Long userId) {
        return ResponseEntity.ok(service.deleteUser(userId));
    }
    @Operation(summary = "Change role")
    @PatchMapping("/change-role/{userId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void changeRole(@PathVariable Long userId,@RequestParam UserRole role) {
         service.changeRole(userId,role);
    }

    @Operation(summary = "Activate User")
    @PatchMapping("/activate/{userId}")
    @PreAuthorize("hasAnyRole('MANAGER','ADMIN')")
    public ResponseEntity<String> activateUser(@PathVariable Long userId) {
        return ResponseEntity.ok(service.activateUser(userId));
    }
    @Operation(summary = "Deactivate user")
    @PatchMapping("/deactivate/{userId}")
    @PreAuthorize("hasAnyRole('MANAGER','ADMIN')")
    public ResponseEntity<String> deactivateUser(@PathVariable Long userId) {
        return ResponseEntity.ok(service.deactivateUser(userId));
    }


}
