package com.example.restapi.controller;

import com.example.restapi.object.Employee;
import com.example.restapi.repository.EmployeeRepository;
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

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class EmployeeControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    EmployeeService employeeService;

    @BeforeEach
    void cleanRepository(){
        employeeService.clearAll();
    }

    @Test
    void should_get_all_employees_when_perform_when_perform_get_given_employees() throws Exception {
        //given
        Employee employee = new Employee(1, "John Doe", 20, "male", 1000);
        employeeService.create(employee);
        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.get("/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").isNumber())
                .andExpect(jsonPath("$[0].name").value("John Doe"))
                .andExpect(jsonPath("$[0].age").value(20))
                .andExpect(jsonPath("$[0].gender").value("male"))
                .andExpect(jsonPath("$[0].salary").value(1000));
    }

    @Test
    void should_return_employee_when_perform_post_given_employee() throws Exception {
        //given
        String employee = "{\n" +
            "        \"name\": \"John Doe\",\n" +
            "        \"age\": 20,\n" +
            "        \"gender\": \"male\",\n" +
            "        \"salary\": 1000\n" +
            "    }";
        //when
        //then
        mockMvc.perform(post("/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(employee))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.age").value(20))
                .andExpect(jsonPath("$.gender").value("male"))
                .andExpect(jsonPath("$.salary").value(1000));
    }

    @Test
    void should_get_employee_when_perform_get_given_id() throws Exception {
        //given
        Employee employee = new Employee(1, "John Doe", 20, "male", 1000);
        employeeService.create(employee);
        //when
        mockMvc.perform(MockMvcRequestBuilders.get("/employees/" + employee.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.age").value(20))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("John Doe"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.gender").value("male"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.salary").value(1000));
        //then
    }

    @Test
    void should_get_employees_when_perform_get_given_page_and_pageSize() throws Exception {
        //given
        createThreeEmployees();
        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.get("/employees/?page=1&pageSize=2"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].id").value(containsInAnyOrder(1, 2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].name").value(containsInAnyOrder("John Doe", "Jane Doe")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].age").value(containsInAnyOrder(20, 21)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].gender").value(containsInAnyOrder("male", "female")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].salary").value(containsInAnyOrder(1000, 2000)));
    }

    @Test
    void should_get_employee_when_perform_get_given_gender() throws Exception {
        //given
        createThreeEmployees();
        //when
        mockMvc.perform(MockMvcRequestBuilders.get("/employees?gender=male"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].id").value(containsInAnyOrder(1, 3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].name").value(containsInAnyOrder("John Doe", "Doe Doe")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].age").value(containsInAnyOrder(20, 20)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].gender").value(containsInAnyOrder("male", "male")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].salary").value(containsInAnyOrder(1000, 3000)));
        //then
    }

    @Test
    void should_return_employee_when_perform_put_given_updated_employee() throws Exception {
        //given
        Employee employee = new Employee(1, "John Doe", 20, "male", 1000);
        employeeService.create(employee);
        String updatedEmployee="{\n" +
                "    \"age\": 23,\n" +
                "    \"salary\": 123456\n" +
                "}";
        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.put("/employees/" + employee.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(updatedEmployee))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.age").value(23))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("John Doe"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.gender").value("male"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.salary").value(123456));
    }

    @Test
    void should_delete_one_employee_when_perform_delete_given_id() throws Exception {
        //given
        createThreeEmployees();
        //when
        mockMvc.perform(MockMvcRequestBuilders.delete("/employees/3"))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        //then
        assertEquals(2, employeeService.findAll().size());
    }

    private void createThreeEmployees() {
        Employee employee1 = new Employee(1, "John Doe", 20, "male", 1000);
        employeeService.create(employee1);
        Employee employee2 = new Employee(2, "Jane Doe", 21, "female", 2000);
        employeeService.create(employee2);
        Employee employee3 = new Employee(3, "Doe Doe", 20, "male", 3000);
        employeeService.create(employee3);
    }
}