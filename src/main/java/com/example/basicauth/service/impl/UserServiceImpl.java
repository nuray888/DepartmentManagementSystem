package com.example.basicauth.service.impl;

import com.example.basicauth.dto.UserRequest;
import com.example.basicauth.dto.UserResponseDto;
import com.example.basicauth.mapper.UserMapper;
import com.example.basicauth.model.UserInfo;
import com.example.basicauth.repo.UserInfoRepository;
import com.example.basicauth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserInfoRepository repository;
    private final UserMapper mapper;
    private final ModelMapper modelMapper;

    public List<UserResponseDto> getUsers() {
        List<UserInfo> all = repository.findAll();
        return all.stream().map(user->modelMapper.map(user,UserResponseDto.class)).toList();
    }
    public UserResponseDto getUser(Long id){
        UserInfo userInfo = repository.findById(id).orElseThrow();
        return mapper.userToDto(userInfo);
    }

    //Helelik silmeyek tutaqki admin yeni user yarada bilir.
    public UserResponseDto createUser(UserRequest userRequest) {
        UserInfo userInfo = mapper.userDtoToUser(userRequest);
        repository.save(userInfo);
        return mapper.userToDto(userInfo);
    }

    public UserResponseDto updateUser(Long id, UserRequest userRequest) {
        UserInfo userInfo = repository.findById(id).orElseThrow();
        modelMapper.map(userRequest, userInfo);
        UserInfo updatedStudent = repository.save(userInfo);
        return mapper.userToDto(updatedStudent);
    }

    public String deleteUser(Long id){
        UserInfo userInfo = repository.findById(id).orElseThrow();
        repository.delete(userInfo);
        return "User has been deleted";
    }


}
