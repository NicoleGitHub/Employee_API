package com.example.restapi;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class EmployeeRepository {
    private List<Employee> employees = new ArrayList<>();

    public EmployeeRepository() {
        employees.add(new Employee(1, "John Doe", 100, "Male", 10000000));
    }

    public List<Employee> getAll() {
        return employees;
    }

    public Employee getEmployeesByID(Integer id){
        System.out.print(id);
        return employees.stream().filter(employee -> employee.getId().equals(id)).findFirst().orElse(null);
    }
}
