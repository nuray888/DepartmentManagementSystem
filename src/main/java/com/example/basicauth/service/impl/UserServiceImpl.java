package com.example.basicauth.service.impl;

import com.example.basicauth.config.UserInfoDetails;
import com.example.basicauth.dao.model.Department;
import com.example.basicauth.dao.model.UserInfo;
import com.example.basicauth.dao.model.UserRole;
import com.example.basicauth.dao.repo.DepartmentRepository;
import com.example.basicauth.dao.repo.UserInfoRepository;
import com.example.basicauth.dto.user.UserPageResponse;
import com.example.basicauth.dto.user.UserResponseDto;
import com.example.basicauth.dto.user.UserUpdateRequest;
import com.example.basicauth.exception.NotValidException;
import com.example.basicauth.exception.ResourceNotFoundException;
import com.example.basicauth.exception.UserNotLoginException;
import com.example.basicauth.mapper.UserMapper;
import com.example.basicauth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserInfoRepository repository;
    private final UserMapper mapper;
    private final DepartmentRepository departmentRepository;
//    public UserResponseDto createUser(UserRequest userRequest) {
//        if(getCurrentUser().getRole().equals(UserRole.ROLE_MANAGER)){
//            isValidManager(userRequest.departmentId(),getCurrentUser());
//        }
//        UserInfo userInfo = mapper.userDtoToUser(userRequest);
//        repository.save(userInfo);
//        return mapper.userToDto(userInfo);
//    }


    public UserPageResponse getUsers(Integer pageNumber,Integer pageSize,String sortBy,String orderBy) {
        Sort sortAndOrder=orderBy.equalsIgnoreCase("desc")?
                Sort.by(sortBy).descending():Sort.by(sortBy).ascending();

        Pageable pageDetails=PageRequest.of(pageNumber,pageSize,sortAndOrder);
        Page<UserInfo> userPage = repository.findAll(pageDetails);
        List<UserInfo> content = userPage.getContent();
        List<UserResponseDto> collect = content.stream().map(mapper::userToDto).toList();
        return new UserPageResponse(
                collect,pageNumber,pageSize,userPage.getTotalElements(),userPage.getTotalPages(), userPage.isLast()
        );
    }


    public UserResponseDto getUser(Long id){
        UserInfo userInfo = repository.findById(id).orElseThrow(()->new ResourceNotFoundException("User not found with id " + id));
        return mapper.userToDto(userInfo);
    }


    public UserResponseDto updateUser(Long id, UserUpdateRequest userRequest) {
        UserInfo userInfo = repository.findById(id).orElseThrow(()->new ResourceNotFoundException("User not found with id " + id));
        mapper.updateUserFromDto(userRequest,userInfo);
        UserInfo updatedStudent = repository.save(userInfo);
        return mapper.userToDto(updatedStudent);
    }

    public String deleteUser(Long id){
        UserInfo userInfo = repository.findById(id).orElseThrow(()->new ResourceNotFoundException("User not found with id " + id));
        repository.delete(userInfo);
        userInfo.setIsDeleted(true);
        return "User marked as deleted";
    }

    public void changeRole(Long userId, UserRole role){
        UserInfo userInfo = repository.findById(userId).orElseThrow(()->new ResourceNotFoundException("User not found with id " + userId));
        userInfo.setRole(role);

        repository.save(userInfo);
    }

    public UserInfo getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getPrincipal()))
            throw new UserNotLoginException("İstifadəçi Login olmayıb");

        UserInfoDetails userDetails = (UserInfoDetails) authentication.getPrincipal();
        return userDetails.getUserInfo();
    }


    public String activateUser(Long id){
        UserInfo userInfo = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id));
        isValidManager(userInfo.getDepartment().getId(), getCurrentUser());
        userInfo.setIsActive(true);
        repository.save(userInfo);
        return  "User marked as activated";
    }
    public String deactivateUser(Long id){
        UserInfo currentUser=getCurrentUser();
        UserInfo userInfo = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id));
        if(currentUser.getRole()!=UserRole.ROLE_ADMIN){
            isValidManager(userInfo.getDepartment().getId(), getCurrentUser());
        }
        userInfo.setIsActive(false);
        repository.save(userInfo);
        return   "User marked as deactivated";
    }
    public void isValidManager(Long departmentId, UserInfo manager) {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id " + departmentId));

        if (manager.getRole() == UserRole.ROLE_USER) {
            throw new NotValidException("User cannot manage users");
        }
        if (department.getManager() == null ||
                !department.getManager().getId().equals(manager.getId())) {
            throw new NotValidException("You are not the manager of this department");
        }
    }

    public UserInfo findByEmail(String email){
        return repository.findByEmail(email).orElseThrow(()->new ResourceNotFoundException("User not found with email " + email));

    }






}
