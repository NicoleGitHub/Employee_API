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
    void should_return_employee_when_edit_employees_given_updated_employee() {
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
    void should_return_employee_when_save_page_employee() {
        //given
        Employee employee = new Employee(1, "John Doe", 20, "male", 1000);
        given(employeeRepository.save(employee.getId(), employee))
                .willReturn(employee);

        //when
        Employee actual = employeeService.save(employee.getId(), employee);

        //then
        assertEquals(employee, actual);
    }

    @Test
    void should_return_employees_when_get_by_gender_given_gender_male() {
        //given
        String gender = "male";
        createThreeEmployees();
        List<Employee> employeesWithMale = Arrays.asList(new Employee(1, "John Doe", 20, "male", 1000),
                                                        new Employee(3, "Doe Doe", 20, "male", 3000));
        given(employeeRepository.getByGender(gender))
                .willReturn(employeesWithMale);

        //when
        List<Employee> actualList = employeeService.getByGender(gender);

        //then
        assertEquals(employeesWithMale, actualList);
    }

    @Test
    void should_return_employees_when_get_by_page_employees_given_page_and_pageSize() {
        //given
        Integer page = 1;
        Integer pageSize = 2;
        createThreeEmployees();
        List<Employee> employeesOnPage = Arrays.asList(new Employee(1, "John Doe", 20, "male", 1000),
                new Employee(3, "Doe Doe", 20, "male", 3000));
        given(employeeRepository.findByPage(page, pageSize))
                .willReturn(employeesOnPage);

        //when
        List<Employee> actualList = employeeService.findByPage(page, pageSize);

        //then
        assertEquals(employeesOnPage, actualList);
    }

    @Test
    void should_return_employee_when_create_employee_given_employee() {
        //given
        Employee employee = new Employee(1, "John Doe", 20, "male", 1000);

        given(employeeRepository.create(employee))
                .willReturn(employee);

        //when
        Employee actual = employeeService.create(employee);

        //then
        assertEquals(employee, actual);
    }

    @Test
    void should_return_nothing_when_delete_employee_given_id() {
        //given
        Employee employee = new Employee(1, "John Doe", 20, "male", 1000);

        //when
        employeeService.delete(employee.getId());

        //then
        verify(employeeRepository).delete(employee.getId());
    }

    private void createThreeEmployees() {
        Employee employee1 = new Employee(1, "John Doe", 20, "male", 1000);
        employeeRepository.create(employee1);
        Employee employee2 = new Employee(2, "Jane Doe", 21, "female", 2000);
        employeeRepository.create(employee2);
        Employee employee3 = new Employee(3, "Doe Doe", 20, "male", 3000);
        employeeRepository.create(employee3);
    }

}
