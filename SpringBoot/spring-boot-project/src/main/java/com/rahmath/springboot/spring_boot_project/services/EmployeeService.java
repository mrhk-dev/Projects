package com.rahmath.springboot.spring_boot_project.services;

import com.rahmath.springboot.spring_boot_project.dao.EmployeeDao;
import com.rahmath.springboot.spring_boot_project.models.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.beans.Customizer;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class EmployeeService {

    private List<Employee> employeeList = new CopyOnWriteArrayList<>();

    @Autowired
    private EmployeeDao dao;

    public Employee addEmployee(Employee emp) {
//        employeeList.add(emp);
//        return emp.getEmpId();

        return dao.save(emp);
    }

    public List<Employee> getEmployeeList() {
//        return employeeList;
        return (List<Employee>) dao.findAll();
    }

    public Employee getEmployee(int id) {
//        Employee emp = null;
//        for (int i = 0; i < employeeList.size(); i++) {
//            if (Objects.equals(employeeList.get(i).getEmpId(), id)) {
//                emp = employeeList.get(i);
//                break;
//            }
//        }
//        return emp;


//        return employeeList.stream().filter(c -> Objects.equals(c.getEmpId(), id)).findFirst().get();

        return dao.findById(String.valueOf(id)).get();
    }

    public Employee updateEmployee(int id, Employee emp) {
//        employeeList.stream().forEach(e -> {
//                    if (e.getEmpId() == id) {
//                        e.setEmpEmail(emp.getEmpEmail());
//                        e.setEmpDepartment(emp.getEmpDepartment());
//                        e.setEmpName(emp.getEmpName());
//                        e.setEmpSalary(emp.getEmpSalary());
//                    }
//                }
//        );
//
//        return employeeList.stream().filter(e ->
//                e.getEmpId() == id).findFirst().get();

        emp.setEmpId(id);
        return dao.save(emp);
    }

    public void deleteEmp(int id){
        dao.deleteById(String.valueOf(id));
    }


}
