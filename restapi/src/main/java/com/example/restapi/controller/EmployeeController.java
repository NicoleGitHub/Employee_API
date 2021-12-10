package com.example.restapi.controller;

import com.example.restapi.mapper.EmployeeMapper;
import com.example.restapi.object.dto.EmployeeRequest;
import com.example.restapi.object.dto.EmployeeResponse;
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
    public List<EmployeeResponse> getAllEmployees() {
        return employeeMapper.toResponses(employeeService.findAll());
    }

    @GetMapping("/{id}")
    public EmployeeResponse getEmployeesByID(@PathVariable String id) {
        return employeeMapper.toResponse(employeeService.findById(id));
    }

    @GetMapping(params = {"gender"})
    public List<EmployeeResponse> getEmployeesByGender(@RequestParam String gender) {
        return employeeMapper.toResponses(employeeService.getByGender(gender));
    }

    @GetMapping(params = {"page", "pageSize"})
    public List<EmployeeResponse> getEmployeeByPage(@RequestParam Integer page, @RequestParam Integer pageSize) {
        return employeeMapper.toResponses(employeeService.findByPage(page, pageSize));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public EmployeeResponse addEmployee(@RequestBody EmployeeRequest employeeRequest) {
        return employeeMapper.toResponse(employeeService.create(employeeMapper.toEntity(employeeRequest)));
    }

    @PutMapping("/{id}")
    public EmployeeResponse editEmployee(@PathVariable String id, @RequestBody EmployeeRequest employeeRequest) {
        return employeeMapper.toResponse(employeeService.edit(id, employeeMapper.toEntity(employeeRequest)));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteEmployee(@PathVariable String id) {
        employeeService.delete(id);
    }

}
