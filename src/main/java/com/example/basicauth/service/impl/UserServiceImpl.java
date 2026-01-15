package com.example.basicauth.service.impl;

import com.example.basicauth.dao.model.UserRole;
import com.example.basicauth.dao.repo.DepartmentRepository;
import com.example.basicauth.dto.UserRequest;
import com.example.basicauth.dto.UserResponseDto;
import com.example.basicauth.dto.UserUpdateRequest;
import com.example.basicauth.exception.NotValidException;
import com.example.basicauth.exception.ResourceNotFoundException;
import com.example.basicauth.exception.UserNotLoginException;
import com.example.basicauth.mapper.UserMapper;
import com.example.basicauth.dao.model.UserInfo;
import com.example.basicauth.dao.repo.UserInfoRepository;
import com.example.basicauth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

    public void isValidManager(Long departmentId, UserInfo manager){
        departmentRepository.findById(departmentId).orElseThrow(()->new ResourceNotFoundException("Department not found with id " + departmentId));
        if(!manager.getDepartment().getId().equals(departmentId)){
            throw  new NotValidException("You can't add user to another department");
        };
    }

    public List<UserResponseDto> getUsers() {
        List<UserInfo> all = repository.findAll();
        return all.stream().map(mapper::userToDto).collect(Collectors.toList());
    }
    public UserResponseDto getUser(Long id){
        UserInfo userInfo = repository.findById(id).orElseThrow();
        return mapper.userToDto(userInfo);
    }

    //Helelik silmeyek tutaqki admin yeni user yarada bilir.


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

        return (UserInfo) authentication.getPrincipal();
    }

    public String activateUser(Long id){
        UserInfo userInfo = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id));
        isValidManager(userInfo.getDepartment().getId(), getCurrentUser());
        userInfo.setIsActive(true);
        repository.save(userInfo);
        return  "User marked as activated";
    }
    public String deactivateUser(Long id){
        UserInfo userInfo = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id));
        isValidManager(userInfo.getDepartment().getId(), getCurrentUser());
        userInfo.setIsActive(false);
        repository.save(userInfo);
        return   "User marked as deactivated";
    }

    public UserInfo findByEmail(String email){
        return repository.findByEmail(email).orElseThrow(()->new ResourceNotFoundException("User not found with email " + email));

    }






}
