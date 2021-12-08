package com.example.restapi.service;

import com.example.restapi.object.Employee;
import com.example.restapi.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {

    private EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    public Employee getById(Integer id) {
        return employeeRepository.findById(id);
    }

    public List<Employee> getByGender(String gender) {
        return employeeRepository.findByGender(gender);
    }

    public List<Employee> findByPage(Integer page, Integer pageSize) {
        return employeeRepository.findByPage(page, pageSize);
    }

    public Employee edit(Integer id, Employee updatedEmployee) {
        Employee employee = getById(id);

        if(updatedEmployee.getAge() != null) {
            employee.setAge(updatedEmployee.getAge());
        }
        if(updatedEmployee.getSalary() != null) {
            employee.setSalary(updatedEmployee.getSalary());
        }

        return employeeRepository.save(id, employee);
    }

    public Employee create(Employee employee) {
        return employeeRepository.create(employee);
    }

    public void delete(Integer id) {
        employeeRepository.delete(id);
    }

    public void clearAll() {
        employeeRepository.clearAll();
    }
}