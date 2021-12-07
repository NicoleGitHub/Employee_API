package com.example.restapi;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public Employee getEmployeesByID(@PathVariable Integer id) {
        return employeeRepository.getById(id);
    }

    @GetMapping(params = {"gender"})
    public List<Employee> getEmployeesByGender(@RequestParam String gender) {
        return employeeRepository.getByGender(gender);
    }

    @GetMapping(params = {"page", "pageSize"})
    public List<Employee> getEmployeeByPage(@RequestParam Integer page, @RequestParam Integer pageSize) {
        return employeeRepository.findByPage(page, pageSize);
    }

    @PostMapping
    public ResponseEntity addEmployee(@RequestBody Employee employee) {
        return new ResponseEntity<>(employeeRepository.create(employee), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public Employee editEmployee(@PathVariable Integer id, @RequestBody Employee updatedEmployee) {
        return employeeRepository.save(id, updatedEmployee);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteEmployee(@PathVariable Integer id) {
        return new ResponseEntity<>(employeeRepository.delete(id), HttpStatus.NO_CONTENT);
    }
}
