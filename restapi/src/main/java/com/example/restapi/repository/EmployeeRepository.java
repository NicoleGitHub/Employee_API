package com.example.restapi.repository;

import com.example.restapi.exception.NoEmployeesFoundException;
import com.example.restapi.object.entity.Employee;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class EmployeeRepository {
    private List<Employee> employees = new ArrayList<>();

    public EmployeeRepository() {
        employees.add(new Employee( "name one", 100, "male", 10000000));
        employees.add(new Employee("name two", 200, "male", 20000000));
        employees.add(new Employee("name three", 300, "male", 30000000));
        employees.add(new Employee("name four", 400, "female", 40000000));
        employees.add(new Employee("name five", 500, "female", 50000000));
        employees.add(new Employee("name six", 600, "female", 60000000));
    }

    public List<Employee> getEmployees() {
        if(employees.isEmpty()) {
            throw new NoEmployeesFoundException();
        }
        return employees;
    }

    public List<Employee> findAll() {
        return employees;
    }

    public Employee findById(String id){
        return employees.stream()
                .filter(employee -> employee.getId().equals(id))
                .findFirst()
                .orElseThrow(NoEmployeesFoundException::new);
    }

    public List<Employee> findByGender(String gender) {
        return getEmployees().stream()
                .filter(employee -> employee.getGender().equals(gender))
                .collect(Collectors.toList());
    }

    public Employee save(String id, Employee updatedEmployee) {
        Employee employee = findById(id);
        employees.remove(employee);
        employees.add(updatedEmployee);
        return updatedEmployee;
    }

    public List<Employee> findByPage(Integer page, Integer pageSize) {
        return getEmployees().stream().skip((long) (page - 1) * pageSize).limit(pageSize).collect(Collectors.toList());
    }

    public Employee create(Employee employee) {
//        Integer nextId = employees.stream()
//                                .mapToInt(Integer.valueOf(Employee::getId))
//                                .max()
//                                .orElse(0)+1;
        Integer nextId = (employees.size()+1);
        employee.setId(nextId.toString());
        employees.add(employee);
        return employee;
    }

    public void delete(String id) {
        employees.remove(findById(id));
    }

    public void clearAll() {
        employees.clear();
    }

    public List<Employee> findEmployeesByCompanyId(String companyId) {
        return getEmployees().stream().filter(employee -> companyId.equals(employee.getCompanyId())).collect(Collectors.toList());
    }
}
