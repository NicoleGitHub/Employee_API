package com.example.restapi.controller;

import com.example.restapi.mapper.EmployeeMapper;
import com.example.restapi.object.dto.EmployeeRequest;
import com.example.restapi.object.entity.Employee;
import com.example.restapi.service.EmployeeService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private EmployeeService employeeService;
    private EmployeeMapper employeeMapper;

    public EmployeeController(EmployeeService employeeService, EmployeeMapper employeeMapper) {
        this.employeeService = employeeService;
        this.employeeMapper = employeeMapper;
    }

    @GetMapping
    public List<Employee> getAllEmployees() {
        return employeeService.findAll();
    }

    @GetMapping("/{id}")
    public Employee getEmployeesByID(@PathVariable String id) {
        return employeeService.getById(id);
    }

    @GetMapping(params = {"gender"})
    public List<Employee> getEmployeesByGender(@RequestParam String gender) {
        return employeeService.getByGender(gender);
    }

    @GetMapping(params = {"page", "pageSize"})
    public List<Employee> getEmployeeByPage(@RequestParam Integer page, @RequestParam Integer pageSize) {
        return employeeService.findByPage(page, pageSize);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Employee addEmployee(@RequestBody EmployeeRequest employeeRequest) {
        return employeeService.create(employeeMapper.toEntity(employeeRequest));
    }

    @PutMapping("/{id}")
    public Employee editEmployee(@PathVariable String id, @RequestBody EmployeeRequest employeeRequest) {
        return employeeService.edit(id, employeeMapper.toEntity(employeeRequest)) ;
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteEmployee(@PathVariable String id) {
        employeeService.delete(id);
    }
}
