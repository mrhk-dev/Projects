package com.rahmath.springboot.spring_boot_project.api;

import com.rahmath.springboot.spring_boot_project.models.Employee;
import com.rahmath.springboot.spring_boot_project.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/Employees")
public class EmployeeResource {

    @Autowired
    private EmployeeService empService;


    @PostMapping
    public Employee addEmployee(@RequestBody Employee emp) {
        return empService.addEmployee(emp);
    }

    @GetMapping
    public List<Employee> getCustomers() {
        return empService.getEmployeeList();
    }

    @GetMapping(value = "/{employeeId}")
    public Employee getEmployee(@PathVariable("employeeId") int id) {
        return empService.getEmployee(id);
    }

    @PutMapping("/{empId}")
    public Employee updateEmployee(@PathVariable(value = "empId") int id, @RequestBody Employee emp) {
        return empService.updateEmployee(id, emp);
    }

    @DeleteMapping("/{empId}")
    public void deleteEmp(@PathVariable(value = "empId") int id) {
        empService.deleteEmp(id);
    }
}
