package com.example.restapi;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class EmployeeRepository {
    private List<Employee> employees = new ArrayList<>();

    public EmployeeRepository() {
        employees.add(new Employee(1, "John Doe", 100, "male", 10000000));
    }

    public List<Employee> getAll() {
        return employees;
    }

    public Employee getById(Integer id){
        return employees.stream()
                .filter(employee -> employee.getId().equals(id)).
                findFirst()
                .orElseThrow(NoEmployeesFoundException::new);
    }

    public List<Employee> getByGender(String gender) {
        return employees.stream()
                .filter(employee -> employee.getGender().equals(gender))
                .collect(Collectors.toList());
    }

    public Employee save(Integer id, Employee updatedEmployee) {
        Employee employee = getById(id);
        employees.remove(employee);
        employees.add(updatedEmployee);
        return updatedEmployee;
    }
}
