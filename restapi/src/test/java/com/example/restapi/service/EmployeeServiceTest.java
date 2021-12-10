package com.example.restapi.service;

import com.example.restapi.object.entity.Employee;
import com.example.restapi.repository.EmployeeRepositoryNew;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
public class EmployeeServiceTest {

    @Mock
    EmployeeRepositoryNew employeeRepositoryNew;

    @InjectMocks
    EmployeeService employeeService;

    @BeforeEach
    void cleanRepository(){
        employeeRepositoryNew.deleteAll();
    }

    @Test
    void should_return_all_employees_when_find_all_given() {
        //given
        List<Employee> employees = new ArrayList<>();
        employees.add(new Employee("John Doe", 20, "male", 1000, "1"));
        given(employeeRepositoryNew.findAll())
                .willReturn(employees);
        //when
        List<Employee> actual = employeeService.findAll();

        //then
        verify(employeeRepositoryNew).findAll();
        assertEquals(employees, actual);
        assertEquals(employees.size(), actual.size());
        assertEquals(employees.get(0), actual.get(0));
    }

    @Test
    void should_return_employee_when_edit_employees_given_updated_employee() {
        //given
        Employee employee = new Employee("John Doe", 20, "male", 1000, "1");
        Employee updatedEmployee = new Employee("John Doe", 25, "male", 2000, "1");
        given(employeeRepositoryNew.findById(any()))
                .willReturn(java.util.Optional.of(employee));
        employee.setAge(updatedEmployee.getAge());
        employee.setSalary(updatedEmployee.getSalary());
        given(employeeRepositoryNew.save(employee))
                .willReturn(employee);
        //when
        Employee actual = employeeService.edit(employee.getId(), updatedEmployee);

        //then
        verify(employeeRepositoryNew).save(employee);
        assertEquals(employee, actual);
        assertEquals(employee.getId(), actual.getId());
        //#TODO
    }

    @Test
    void should_return_employees_when_get_by_gender_given_gender_male() {
        //given
        String gender = "male";
        createEmployees();
        List<Employee> expected = Arrays.asList(new Employee("John Doe", 20, "male", 1000, "1"),
                                                        new Employee("Doe Doe", 20, "male", 3000, "1"));
        given(employeeRepositoryNew.findAllByGender(gender))
                .willReturn(expected);

        //when
        List<Employee> actualList = employeeService.getByGender(gender);

        //then
        verify(employeeRepositoryNew).findAllByGender(gender);
        assertEquals(expected, actualList);
        //#TODO
    }

    @Test
    void should_return_employees_when_get_by_page_employees_given_page_and_pageSize() {
        //given
        Integer page = 1;
        Integer pageSize = 2;
        createEmployees();
        List<Employee> employeesOnPage = Arrays.asList(new Employee("John Doe", 20, "male", 1000, "1"),
                new Employee("Jane Doe", 21, "female", 2000, "1"));
        given(employeeRepositoryNew.findAll(PageRequest.of(page,pageSize)))
                .willReturn(new PageImpl<>(employeesOnPage, PageRequest.of(1, 2), 1));

        //when
        List<Employee> actualList = employeeService.findByPage(page, pageSize);

        //then
        assertAll(
                () -> assertEquals(employeesOnPage, actualList),
                () ->  assertEquals(employeesOnPage.size(), actualList.size()),
                () ->  assertEquals(employeesOnPage.get(0).getId(), actualList.get(0).getId()),
                () ->  assertEquals(employeesOnPage.get(0).getName(), actualList.get(0).getName()),
                () ->  assertEquals(employeesOnPage.get(0).getAge(), actualList.get(0).getAge()),
                () ->  assertEquals(employeesOnPage.get(0).getGender(), actualList.get(0).getGender()),
                () ->  assertEquals(employeesOnPage.get(0).getSalary(), actualList.get(0).getSalary()),
                () ->  assertEquals(employeesOnPage.get(0).getCompanyId(), actualList.get(0).getCompanyId())
                );

    }

    @Test
    void should_return_employee_when_create_employee_given_employee() {
        //given
        Employee employee = new Employee("John Doe", 20, "male", 1000, "1");

        given(employeeRepositoryNew.insert(employee))
                .willReturn(employee);

        //when
        Employee actual = employeeService.create(employee);

        //then
        verify(employeeRepositoryNew).insert(employee);
        assertEquals(employee, actual);
    }

    @Test
    void should_return_nothing_when_delete_employee_given_id() {
        //given
        Employee employee = new Employee("John Doe", 20, "male", 1000, "1");
        willDoNothing().given(employeeRepositoryNew).deleteById(employee.getId());
        //when
        employeeService.delete(employee.getId());

        //then
        verify(employeeRepositoryNew).deleteById(employee.getId());
    }

    private void createEmployees() {
        Employee employee1 = new Employee("John Doe", 20, "male", 1000, "1");
        employeeRepositoryNew.insert(employee1);
        Employee employee2 = new Employee("Jane Doe", 21, "female", 2000, "1");
        employeeRepositoryNew.insert(employee2);
        Employee employee3 = new Employee("Doe Doe", 20, "male", 3000, "1");
        employeeRepositoryNew.insert(employee3);
    }

}
