package com.example.basicauth.mapper;

import com.example.basicauth.dto.DepartmentDto;
import com.example.basicauth.dao.model.Department;
import com.example.basicauth.dao.model.UserInfo;
import com.example.basicauth.dao.repo.UserInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DepartmentMapper {
    private final UserInfoRepository userRepository;

    public DepartmentDto departmentToDto(Department department) {
        return new DepartmentDto(
                department.getName(),
                department.getDescription(),
                department.getManager()!=null? department.getManager().getId() : null
        );
    }

    public Department dtoToDepartment(DepartmentDto departmentDto) {
        UserInfo manager = userRepository.findById(departmentDto.managerId()).orElse(null);
        return Department.builder()
                .name(departmentDto.name())
                .description(departmentDto.description())
                .manager(manager).build();
    }
    public void updateDepartmentFromDto(DepartmentDto dto, Department department) {
        if (dto.name() != null) department.setName(dto.name());
        if (dto.description() != null) department.setDescription(dto.description());
        if (dto.managerId() != null){
            userRepository.findById(dto.managerId()).ifPresent(department::setManager);
        }
    }



}
