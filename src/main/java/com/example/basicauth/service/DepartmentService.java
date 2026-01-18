package com.example.basicauth.service;

import com.example.basicauth.dto.department.DepartmentDto;
import com.example.basicauth.dto.user.UserResponseDto;

import java.util.List;
import java.util.Set;

public interface DepartmentService {
    List<DepartmentDto> findAll();
    DepartmentDto findById(Long id);
    DepartmentDto create(DepartmentDto departmentDto);
    DepartmentDto update(Long id, DepartmentDto departmentDto);
    String delete(Long id);
    Set<UserResponseDto> getEmployeesByDepartment(Long departmentId);

}
