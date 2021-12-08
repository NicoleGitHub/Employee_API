package com.example.restapi.service;

import com.example.restapi.object.Company;
import com.example.restapi.object.Employee;
import com.example.restapi.repository.CompanyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
public class CompanyServiceTest {

    @Mock
    CompanyRepository companyRepository;

    @Mock
    EmployeeService employeeService;

    @InjectMocks
    CompanyService companyService;

    @BeforeEach
    void cleanRepository(){
        companyRepository.clearAll();
    }

    @Test
    void should_return_employees_when_findAll_given() {
        //given
        List<Company> companies = createCompanies();

        given(companyRepository.findAll())
                .willReturn(companies);

        //when
        List<Company> actual = companyService.findAll();

        //then
        verify(companyRepository).findAll();
        assertEquals(companies, actual);
    }

    @Test
    void should_return_employees_when_findById_given_id() {
        //given
        List<Company> companies = createCompanies();
        Integer companyId = companies.get(0).getId();

        given(companyRepository.findById(companyId))
                .willReturn(companies.get(0));

        //when
        Company actual = companyService.findById(companyId);

        //then
        verify(companyRepository).findById(companyId);
        assertEquals(companies.get(0), actual);
    }


    @Test
    void should_return_companies_when_findByPage_given_page_and_pageSize() {
        //given
        List<Company> companies = createCompanies();
        Integer page = 1;
        Integer pageSize = 2;
        List<Company> companiesOnPage = companies.subList(1,2);

        given(companyRepository.findByPage(page, pageSize))
                .willReturn(companiesOnPage);

        //when
        List<Company> actualList = companyService.findByPage(page, pageSize);

        //then
        verify(companyRepository).findByPage(page, pageSize);
        assertEquals(companiesOnPage, actualList);
    }

    @Test
    void should_company_when_create_company_given_company() {
        //given
        Company company = new Company(1, "Coffee Shop");
        given(companyRepository.create(company))
                .willReturn(company);

        //when
        Company actual = companyService.create(company);

        //then
        verify(companyRepository).create(company);
        assertEquals(company, actual);
    }

    @Test
    void should_return_company_when_editCompany_given_id_and_company() {
        //given
        List<Company> companies = createCompanies();
        Company updateCompany = new Company(1, "Coffee & Tea Shop");
        Company company = companies.get(0);
        Integer companyId = updateCompany.getId();

        given(companyService.findById(companyId))
                .willReturn(company);

        company.setCompanyName(updateCompany.getCompanyName());

        given(companyRepository.save(companyId, company))
                .willReturn(updateCompany);

        //when
        Company actual = companyService.editCompany(companyId, updateCompany);

        //then
        assertEquals(updateCompany, actual);
    }

    @Test
    void should_when_delete_company_given_id() {
        //given
        List<Company> companies = createCompanies();
        Company company = companies.get(0);
        Integer companyId = company.getId();

        given(companyRepository.findById(companyId))
                .willReturn(company);

        //when
        companyService.delete(companyId);

        //then
        verify(companyRepository).delete(companyId);
    }

    
    @Test
    void should_return_employees_when_findEmployeesByCompanyId_given_companyId() {
        //given
        List<Company> companies = createCompanies();
        Company company = companies.get(0);
        Integer companyId = company.getId();
        List<Employee> employees = Arrays.asList(new Employee(1, "John Doe", 20, "male", 1000, companyId),
                new Employee(2, "Jane Doe", 21, "female", 2000, companyId));


        given(employeeService.findEmployeesByCompanyId(companyId))
                .willReturn(employees);

        //when
        List<Employee> actual = companyService.findEmployeesByCompanyId(companyId);

        //then
        assertEquals(employees, actual);

    }

    public List<Company> createCompanies() {

        Company company1 = new Company(1, "Coffee Shop");
        companyRepository.create(company1);
        Company company2 = new Company(2, "Tea Shop");
        companyRepository.create(company2);
        Company company3 = new Company(3, "Bakery");
        companyRepository.create(company3);

        return Arrays.asList(company1, company2, company3);
    }

}
