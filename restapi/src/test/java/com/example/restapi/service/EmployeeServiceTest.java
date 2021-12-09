package com.example.restapi.service;

import com.example.restapi.object.entity.Employee;
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
import static org.mockito.BDDMockito.willDoNothing;
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
        employees.add(new Employee("John Doe", 20, "male", 1000));
        given(employeeRepository.findAll())
                .willReturn(employees);
        //when
        List<Employee> actual = employeeService.findAll();

        //then
        verify(employeeRepository).findAll();
        assertEquals(employees, actual);
        assertEquals(employees.size(), actual.size());
        assertEquals(employees.get(0), actual.get(0));
    }

    @Test
    void should_return_employee_when_edit_employees_given_updated_employee() {
        //given
        Employee employee = new Employee("John Doe", 20, "male", 1000);
        Employee updatedEmployee = new Employee("John Doe", 25, "male", 2000);
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
        //#TODO
    }

    @Test
    void should_return_employees_when_get_by_gender_given_gender_male() {
        //given
        String gender = "male";
        createThreeEmployees();
        List<Employee> expected = Arrays.asList(new Employee("John Doe", 20, "male", 1000),
                                                        new Employee("Doe Doe", 20, "male", 3000));
        given(employeeRepository.findByGender(gender))
                .willReturn(expected);

        //when
        List<Employee> actualList = employeeService.getByGender(gender);

        //then
        verify(employeeRepository).findByGender(gender);
        assertEquals(expected, actualList);
        //#TODO
    }

    @Test
    void should_return_employees_when_get_by_page_employees_given_page_and_pageSize() {
        //given
        Integer page = 1;
        Integer pageSize = 2;
        createThreeEmployees();
        List<Employee> employeesOnPage = Arrays.asList(new Employee("John Doe", 20, "male", 1000),
                new Employee("Doe Doe", 20, "male", 3000));
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
        Employee employee = new Employee("John Doe", 20, "male", 1000);

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
        Employee employee = new Employee("John Doe", 20, "male", 1000);
        willDoNothing().given(employeeRepository).delete(employee.getId());
        //when
        employeeService.delete(employee.getId());

        //then
        verify(employeeRepository).delete(employee.getId());
    }

    private void createThreeEmployees() {
        Employee employee1 = new Employee("John Doe", 20, "male", 1000);
        employeeRepository.create(employee1);
        Employee employee2 = new Employee("Jane Doe", 21, "female", 2000);
        employeeRepository.create(employee2);
        Employee employee3 = new Employee("Doe Doe", 20, "male", 3000);
        employeeRepository.create(employee3);
    }

}
