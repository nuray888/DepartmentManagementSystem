package com.example.basicauth.controller;

import com.example.basicauth.dto.department.DepartmentDto;
import com.example.basicauth.dto.user.UserResponseDto;
import com.example.basicauth.service.DepartmentService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/departments")
@RequiredArgsConstructor
public class DepartmentController {
    private final DepartmentService service;

    @Operation(summary = "Show all departments")
    @GetMapping("/all")
    public ResponseEntity<List<DepartmentDto>> getAllDepartments() {
        return ResponseEntity.ok(service.findAll());
    }

    @Operation(summary = "Get employees by department")
    @GetMapping("/users/{departmentId}")
    public ResponseEntity<Set<UserResponseDto>> getUsersInDepartment(@PathVariable Long departmentId){
        return ResponseEntity.ok(service.getEmployeesByDepartment(departmentId));
    }

    @Operation(summary = "show department by id")
    @GetMapping("/get/{departmentId}")
    public ResponseEntity<DepartmentDto> getDepartment(@PathVariable Long departmentId) {
        return ResponseEntity.ok(service.findById(departmentId));
    }

    @Operation(summary = "Create new department")
    @PostMapping("/create")
    public ResponseEntity<DepartmentDto> createDepartment(@Valid @RequestBody DepartmentDto departmentDto) {
        return ResponseEntity.ok(service.create(departmentDto));
    }

    @Operation(summary = "Update department")
    @PutMapping("/update/{id}")
    public ResponseEntity<DepartmentDto> updateDepartment(@PathVariable Long id, @Valid @RequestBody DepartmentDto departmentDto) {
        return ResponseEntity.ok(service.update(id, departmentDto));
    }

    @Operation(summary = "Delete department")
    @DeleteMapping("/delete/{departmentId}")
    public ResponseEntity<String> deleteDepartment(@PathVariable Long departmentId) {
        return ResponseEntity.ok(service.delete(departmentId));
    }

}
