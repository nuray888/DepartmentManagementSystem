package com.example.basicauth.service.impl;

import com.example.basicauth.dao.model.Department;
import com.example.basicauth.dao.model.UserInfo;
import com.example.basicauth.dao.model.UserRole;
import com.example.basicauth.dao.repo.DepartmentRepository;
import com.example.basicauth.dto.DepartmentDto;
import com.example.basicauth.dto.UserResponseDto;
import com.example.basicauth.exception.ResourceNotFoundException;
import com.example.basicauth.mapper.DepartmentMapper;
import com.example.basicauth.mapper.UserMapper;
import com.example.basicauth.service.DepartmentService;
import com.example.basicauth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {
    private final DepartmentRepository repository;
    private final DepartmentMapper mapper;
    private final UserMapper userMapper;
    private final UserService userService;

    public Set<UserResponseDto> getEmployeesByDepartment(Long departmentId){
        UserInfo userInfo = userService.getCurrentUser();
        if(userInfo.getRole().equals(UserRole.ROLE_MANAGER)) {
            userService.isValidManager(departmentId, userService.getCurrentUser());
        }
        Department department = repository.findById(departmentId).orElseThrow(() -> new ResourceNotFoundException("Department not found"));
        Set<UserInfo> users = department.getUsers();
        return users.stream().map(userMapper::userToDto).collect(Collectors.toSet());
    }
    public List<DepartmentDto> findAll() {
        List<Department> all = repository.findAll();
        return all.stream().map(mapper::departmentToDto).collect(Collectors.toList());
    }

    public DepartmentDto findById(Long id) {
        Department department = repository.findById(id).orElseThrow(()->new ResourceNotFoundException("Department not found with id " + id));
        return mapper.departmentToDto(department);
    }

    public DepartmentDto create(DepartmentDto departmentDto) {
        Department department = mapper.dtoToDepartment(departmentDto);
        Department savedDepartment = repository.save(department);
        return mapper.departmentToDto(savedDepartment);
    }

    public DepartmentDto update(Long id, DepartmentDto departmentDto) {
        Department department = repository.findById(id).orElseThrow(()->new ResourceNotFoundException("Department not found with id " + id));
        mapper.updateDepartmentFromDto(departmentDto, department);
        Department savedDepartment = repository.save(department);
        return mapper.departmentToDto(savedDepartment);
    }

    public String delete(Long id) {
        Department department = repository.findById(id).orElseThrow(()->new ResourceNotFoundException("Department not found with id " + id));
        repository.delete(department);
        return "Department has been deleted";
    }

}
