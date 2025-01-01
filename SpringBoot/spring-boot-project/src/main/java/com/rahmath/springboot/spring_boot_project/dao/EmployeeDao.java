package com.rahmath.springboot.spring_boot_project.dao;

import com.rahmath.springboot.spring_boot_project.models.Employee;
import org.springframework.data.repository.CrudRepository;

public interface EmployeeDao extends CrudRepository<Employee, String> {
}
