package com.example.restapi.service;

import com.example.restapi.object.Employee;
import com.example.restapi.repository.EmployeeRepository;
import com.example.restapi.service.EmployeeService;
import com.fasterxml.jackson.databind.deser.UnresolvedId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
public class EmployeeServiceTest {

    @Mock
    EmployeeRepository employeeRepository;

    @InjectMocks
    EmployeeService employeeService;

    @Test
    void should_return_all_employees_when_find_all_given_employees() {
        //given
        List<Employee> employees = new ArrayList<>();
        employees.add(new Employee(1, "John Doe", 20, "male", 1000));
        given(employeeRepository.getAll())
                .willReturn(employees);
        //when
        List<Employee> actual = employeeService.findAll();

        //then
        assertEquals(employees, actual);
    }

    @Test
    void should_return_employee_when_edit_employee_given_updated_employee() {
        //given
        Employee employee = new Employee(1, "John Doe", 20, "male", 1000);
        Employee updatedEmployee = new Employee(1, "John Doe", 25, "male", 2000);
        given(employeeRepository.getById(any()))
                .willReturn(employee);
        employee.setAge(updatedEmployee.getAge());
        employee.setSalary(updatedEmployee.getSalary());
        given(employeeRepository.save(any(),any(Employee.class)))
                .willReturn(employee);
        //when
        Employee actual = employeeService.edit(employee.getId(), updatedEmployee);

        //then
        verify(employeeRepository).save(employee.getId(), employee);
        assertEquals(employee, actual);
    }

    @Test
    void should_return_employees_when_get_employee_given_gender_male() {
        //given
        List<Employee> employees = Arrays.asList(new Employee(1, "John Doe", 20, "male", 1000),
                new Employee(2, "Jane Doe", 21, "female", 2000));

        List<Employee> employeesWithoutMale = employees.stream().filter(employee -> employee.getGender().equals("male")).collect(Collectors.toList());
        //when
        List<Employee> actualList = employeeService.getByGender("male");

        //then
        assertEquals(employeesWithoutMale, actualList);
    }

}
