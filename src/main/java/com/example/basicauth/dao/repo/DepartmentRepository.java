package com.example.basicauth.dao.repo;

import com.example.basicauth.dao.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department,Long> {

}
