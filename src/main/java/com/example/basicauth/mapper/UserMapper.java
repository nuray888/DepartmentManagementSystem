package com.example.basicauth.mapper;

import com.example.basicauth.dto.UserRequest;
import com.example.basicauth.dto.UserResponseDto;
import com.example.basicauth.model.Department;
import com.example.basicauth.model.UserInfo;
import com.example.basicauth.repo.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserMapper {
    private final DepartmentRepository departmentRepository;
    private final PasswordEncoder passwordEncoder;

    public UserInfo userDtoToUser(UserRequest userDto) {
        Department department = departmentRepository.findById(userDto.departmentId()).orElse(null);
        return UserInfo.builder()
                .name(userDto.name())
                .surname(userDto.surname())
                .password(passwordEncoder.encode(userDto.password()))
                .salary(userDto.salary())
                .address(userDto.address())
                .email(userDto.email())
                .department(department)
                .build();
    }

    public UserResponseDto userToDto(UserInfo user) {
        return new UserResponseDto(
                user.getName(),
                user.getSurname(),
                user.getAddress(),
                user.getSalary(),
                user.getEmail(),
                user.getDepartment() != null ? user.getDepartment().getId() : null,
                user.getIsActive(),
                user.getIsDeleted()
        );
    }
}
