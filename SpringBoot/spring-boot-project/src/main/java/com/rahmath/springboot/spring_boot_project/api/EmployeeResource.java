package com.rahmath.springboot.spring_boot_project.api;

import com.rahmath.springboot.spring_boot_project.models.Employee;
import com.rahmath.springboot.spring_boot_project.services.EmployeeService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.csrf.CsrfToken;
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


    /*
    * The post, put and delete method doesnt work if the csrf token is not provided or available.
    * So we need to get the token and then provide the token to modift the data.
    *
    * in postman we use the tag called "X-CSRF-TOKEN" in attributes along with the auth data
    * */
    @GetMapping("/csrf-token")
    public CsrfToken getCsrfToken(HttpServletRequest http){
        return (CsrfToken) http.getAttribute("_csrf");
    }

}
