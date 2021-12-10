package com.example.restapi.controller;

import com.example.restapi.object.entity.Company;
import com.example.restapi.object.entity.Employee;
import com.example.restapi.service.CompanyService;
import com.example.restapi.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CompanyControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    CompanyService companyService;

    @Autowired
    EmployeeService employeeService;

    @BeforeEach
    void cleanRepository(){
        companyService.clearAll();
        employeeService.clearAll();
    }

    @Test
    void should_get_all_companies_when_perform_findAll_given() throws Exception {
        //given
        Company company = new Company("1", "Tea Shop");
        companyService.create(company);
        List<Employee> employees = createEmployees();

        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.get("/companies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[*].name").value("Tea Shop"))
                .andExpect(jsonPath("$[*].employees[0].name").value(employees.get(0).getName()));
    }

    @Test
    void should_return_employee_when_perform_post_given_employee() throws Exception {
        //given
        String employee = "    {\n" +
                "        \"id\": 1,\n" +
                "        \"companyName\": \"company one\"\n" +
                "    }";
        //when
        //then
        mockMvc.perform(post("/companies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(employee))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.companyName").value("company one"));
    }


    @Test
    void should_get_employee_when_perform_get_given_id() throws Exception {
        //given
        Company company = new Company("1", "a");
        companyService.create(company);
        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.get("/companies/" + company.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("a"));
    }

    @Test
    void should_throw_exception_when_perform_get_given_invalid_id() throws Exception {
        //given
        Integer CompanyId = 1;

        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.get("/companies/" + CompanyId))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Entity Not Found."))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(404));
    }

    @Test
    void should_get_companies_with_employees_when_perform_get_given_page_and_pageSize() throws Exception {
        //given
        Employee employee = new Employee("John Doe", 20, "male", 1000, "1");
        employeeService.create(employee);
        createCompanies();
        System.out.println("cecking" + employeeService.findAll().get(0).getCompanyId() + "cecking" +companyService.findAll().get(0).getId() );
        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.get("/companies/?page=0&pageSize=2"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].name").value(containsInAnyOrder("Coffee Shop", "Tea Shop")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].employees[0].name").value(containsInAnyOrder("John Doe")));
    }

    @Test
    void should_get_employees_when_perform_get_given_companyId() throws Exception {
        //given
        createCompanies();
        Integer CompanyId = 1;
        Employee employee1 = new Employee( "John Doe", 20, "male", 1000, "1");
        employeeService.create(employee1);
        Employee employee2 = new Employee( "Jane Doe", 21, "female", 2000, "1");
        employeeService.create(employee2);
        
        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.get("/companies/" + CompanyId + "/employees"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].id").value(containsInAnyOrder(1, 2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].name").value(containsInAnyOrder("John Doe", "Jane Doe")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].age").value(containsInAnyOrder(20, 21)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].gender").value(containsInAnyOrder("male", "female")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].salary").value(containsInAnyOrder(1000, 2000)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].companyId").value(containsInAnyOrder(1, 1)));
    }

    @Test
    void should_throw_exception_when_perform_get_given_companyId_and_empty_employee_repo() throws Exception {
        //given
        createCompanies();
        Integer CompanyId = 1;

        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.get("/companies/" + CompanyId + "/employees"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Entity Not Found."))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(404));
    }

    @Test
    void should_return_employee_when_perform_put_given_updated_employee() throws Exception {
        //given
        //given
        Company company = new Company("1", "a");
        companyService.create(company);
        String updatedCompany = "    {\n" +
                "        \"id\": 1,\n" +
                "        \"companyName\": \"a\"\n" +
                "    }";
        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.put("/companies/" + company.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(updatedCompany))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value("1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("a"));
    }

    @Test
    void should_delete_one_employee_when_perform_delete_given_id() throws Exception {
        //given
        createCompanies();
        //when
        mockMvc.perform(MockMvcRequestBuilders.delete("/companies/3"))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        //then
        assertEquals(2, companyService.findAll().size());
    }

    public List<Company> createCompanies() {

        Company company1 = new Company("1", "Coffee Shop");
        companyService.create(company1);
        Company company2 = new Company("2", "Tea Shop");
        companyService.create(company2);
        Company company3 = new Company("3", "Bakery");
        companyService.create(company3);

        return Arrays.asList(company1, company2, company3);
    }

    public List<Employee> createEmployees() {

        Employee employee1 = new Employee("John Doe", 20, "male", 1000, "1");
        employeeService.create(employee1);
        Employee employee2 = new Employee("Jane Doe", 21, "female", 2000, "1");
        employeeService.create(employee2);
        Employee employee3 = new Employee("Doe Doe", 20, "male", 3000, "1");
        employeeService.create(employee3);

        return Arrays.asList(employee1, employee2, employee3);
    }

}
