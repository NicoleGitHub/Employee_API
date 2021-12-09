package com.example.restapi.controller;

import com.example.restapi.object.entity.Company;
import com.example.restapi.object.entity.Employee;
import com.example.restapi.repository.CompanyRepository;
import com.example.restapi.repository.EmployeeRepository;
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

    //#TODO change to service
    @Autowired
    CompanyRepository companyRepository;

    @Autowired
    EmployeeRepository employeeRepository;

    @BeforeEach
    void cleanRepository(){
        companyRepository.clearAll();
        employeeRepository.clearAll();
    }

    @Test
    void should_get_all_companies_when_perform_findAll_given() throws Exception {
        //given
        Company company = new Company("1", "a");
        companyRepository.create(company);

        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.get("/companies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").isNumber())
                .andExpect(jsonPath("$[0].companyName").value("a"));
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
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.companyName").value("company one"));
    }


    @Test
    void should_get_employee_when_perform_get_given_id() throws Exception {
        //given
        Company company = new Company("1", "a");
        companyRepository.create(company);
        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.get("/companies/" + company.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.companyName").value("a"));
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
    void should_get_employees_when_perform_get_given_page_and_pageSize() throws Exception {
        //given
        createCompanies();
        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.get("/companies/?page=1&pageSize=2"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].id").value(containsInAnyOrder(1, 2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].companyName").value(containsInAnyOrder("Coffee Shop", "Tea Shop")));
    }

    @Test
    void should_get_employees_when_perform_get_given_companyId() throws Exception {
        //given
        createCompanies();
        Integer CompanyId = 1;
        Employee employee1 = new Employee( "John Doe", 20, "male", 1000);
        employeeRepository.create(employee1);
        Employee employee2 = new Employee( "Jane Doe", 21, "female", 2000);
        employeeRepository.create(employee2);
        
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
        companyRepository.create(company);
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
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.companyName").value("a"));
    }

    @Test
    void should_delete_one_employee_when_perform_delete_given_id() throws Exception {
        //given
        createCompanies();
        //when
        mockMvc.perform(MockMvcRequestBuilders.delete("/companies/3"))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        //then
        assertEquals(2, companyRepository.findAll().size());
    }

    public List<Company> createCompanies() {

        Company company1 = new Company("1", "Coffee Shop");
        companyRepository.create(company1);
        Company company2 = new Company("2", "Tea Shop");
        companyRepository.create(company2);
        Company company3 = new Company("3", "Bakery");
        companyRepository.create(company3);

        return Arrays.asList(company1, company2, company3);
    }

}
