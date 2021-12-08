package com.example.restapi.service;

import com.example.restapi.object.Employee;
import com.example.restapi.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    @BeforeEach
    void cleanRepository(){
        employeeRepository.clearAll();
    }

    @Test
    void should_return_all_employees_when_find_all_given() {
        //given
        List<Employee> employees = new ArrayList<>();
        employees.add(new Employee(1, "John Doe", 20, "male", 1000, null));
        given(employeeRepository.findAll())
                .willReturn(employees);
        //when
        List<Employee> actual = employeeService.findAll();

        //then
        verify(employeeRepository).findAll();
        assertEquals(employees, actual);
    }

    @Test
    void should_return_employee_when_edit_employees_given_updated_employee() {
        //given
        Employee employee = new Employee(1, "John Doe", 20, "male", 1000, null);
        Employee updatedEmployee = new Employee(1, "John Doe", 25, "male", 2000, null);
        given(employeeRepository.findById(any()))
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
    void should_return_employees_when_get_by_gender_given_gender_male() {
        //given
        String gender = "male";
        createThreeEmployees();
        List<Employee> employeesWithMale = Arrays.asList(new Employee(1, "John Doe", 20, "male", 1000, null),
                                                        new Employee(3, "Doe Doe", 20, "male", 3000, null));
        given(employeeRepository.findByGender(gender))
                .willReturn(employeesWithMale);

        //when
        List<Employee> actualList = employeeService.getByGender(gender);

        //then
        verify(employeeRepository).findByGender(gender);
        assertEquals(employeesWithMale, actualList);
    }

    @Test
    void should_return_employees_when_get_by_page_employees_given_page_and_pageSize() {
        //given
        Integer page = 1;
        Integer pageSize = 2;
        createThreeEmployees();
        List<Employee> employeesOnPage = Arrays.asList(new Employee(1, "John Doe", 20, "male", 1000, null),
                new Employee(3, "Doe Doe", 20, "male", 3000, null));
        given(employeeRepository.findByPage(page, pageSize))
                .willReturn(employeesOnPage);

        //when
        List<Employee> actualList = employeeService.findByPage(page, pageSize);

        //then
        verify(employeeRepository).findByPage(page, pageSize);
        assertEquals(employeesOnPage, actualList);
    }

    @Test
    void should_return_employee_when_create_employee_given_employee() {
        //given
        Employee employee = new Employee(1, "John Doe", 20, "male", 1000, null);

        given(employeeRepository.create(employee))
                .willReturn(employee);

        //when
        Employee actual = employeeService.create(employee);

        //then
        verify(employeeRepository).create(employee);
        assertEquals(employee, actual);
    }

    @Test
    void should_return_nothing_when_delete_employee_given_id() {
        //given
        Employee employee = new Employee(1, "John Doe", 20, "male", 1000, null);

        //when
        employeeService.delete(employee.getId());

        //then
        verify(employeeRepository).delete(employee.getId());
    }

    private void createThreeEmployees() {
        Employee employee1 = new Employee(1, "John Doe", 20, "male", 1000, null);
        employeeRepository.create(employee1);
        Employee employee2 = new Employee(2, "Jane Doe", 21, "female", 2000, null);
        employeeRepository.create(employee2);
        Employee employee3 = new Employee(3, "Doe Doe", 20, "male", 3000, null);
        employeeRepository.create(employee3);
    }

}
