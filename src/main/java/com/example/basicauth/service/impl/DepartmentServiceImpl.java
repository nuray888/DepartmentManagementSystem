package com.example.basicauth.service.impl;

import com.example.basicauth.mapper.DepartmentMapper;
import com.example.basicauth.repo.DepartmentRepository;
import com.example.basicauth.repo.UserInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl {
    private final DepartmentRepository repository;
    private final DepartmentMapper mapper;

}
