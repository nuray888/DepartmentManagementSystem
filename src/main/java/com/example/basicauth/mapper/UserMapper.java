package com.example.basicauth.mapper;

import com.example.basicauth.dto.UserRequest;
import com.example.basicauth.dto.UserResponseDto;
import com.example.basicauth.dto.UserUpdateRequest;
import com.example.basicauth.exception.ResourceNotFoundException;
import com.example.basicauth.dao.model.Department;
import com.example.basicauth.dao.model.UserInfo;
import com.example.basicauth.dao.repo.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

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
                user.getIsActive() != null ? user.getIsActive() : false,
                user.getIsDeleted(),
                user.getRole()
        );
    }

    public void updateUserFromDto(UserUpdateRequest dto, UserInfo user) {
        if(dto.departmentId()!=null) departmentRepository.findById(dto.departmentId()).orElseThrow(()->new ResourceNotFoundException("Department not found with id " + dto.departmentId()));
        if (dto.name() != null) user.setName(dto.name());
        if (dto.surname() != null) user.setSurname(dto.surname());
        if (dto.address() != null) user.setAddress(dto.address());
        if (dto.salary() != null) user.setSalary(dto.salary());

    }

}
