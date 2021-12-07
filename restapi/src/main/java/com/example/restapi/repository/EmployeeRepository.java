package com.example.restapi.repository;

import com.example.restapi.exception.NoEmployeesFoundException;
import com.example.restapi.object.Employee;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class EmployeeRepository {
    private List<Employee> employees = new ArrayList<>();

    public EmployeeRepository() {
        employees.add(new Employee(1, "name one", 100, "male", 10000000));
        employees.add(new Employee(2, "name two", 200, "male", 20000000));
        employees.add(new Employee(3, "name three", 300, "male", 30000000));
        employees.add(new Employee(4, "name four", 400, "female", 40000000));
        employees.add(new Employee(5, "name five", 500, "female", 50000000));
        employees.add(new Employee(6, "name six", 600, "female", 60000000));
    }

    public List<Employee> getAll() {
        return employees;
    }

    public Employee getById(Integer id){
        return employees.stream()
                .filter(employee -> employee.getId().equals(id))
                .findFirst()
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

    public List<Employee> findByPage(Integer page, Integer pageSize) {
        return employees.stream().skip((long) (page - 1 ) * pageSize).limit(pageSize).collect(Collectors.toList());
    }

    public Employee create(Employee employee) {
        Integer nextId = employees.stream()
                                .mapToInt(Employee::getId)
                                .max()
                                .orElse(0)+1;
        employee.setId(nextId);
        employees.add(employee);
        return employee;
    }

    public Employee delete(Integer id) {
        Employee employee = getById(id);
        Employee deletedEmployee = employee;
        employees.remove(employee);
        return deletedEmployee;
    }
}
