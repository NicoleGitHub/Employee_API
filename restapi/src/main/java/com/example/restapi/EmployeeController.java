package com.example.restapi;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    EmployeeRepository employeeRepository;

    public EmployeeController(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @GetMapping
    public List<Employee> getAllEmployees() {
        return employeeRepository.getAll();
    }

    @GetMapping("/{id}")
    public Employee getEmployeeByID(@PathVariable Integer id) {
        return employeeRepository.getEmployeesByID(id);
    }
}
