package com.example.restapi.service;

import com.example.restapi.mapper.CompanyMapper;
import com.example.restapi.object.bo.CompanyWithEmployee;
import com.example.restapi.object.entity.Company;
import com.example.restapi.object.entity.Employee;
import com.example.restapi.repository.CompanyRepositoryNew;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
public class CompanyServiceTest {

    @Mock
    EmployeeRepositoryNew employeeRepositoryNew;

    @Mock
    CompanyRepositoryNew companyRepositoryNew;

    @Mock
    EmployeeService employeeService;

    @Mock
    CompanyMapper companyMapper;

    @InjectMocks
    CompanyService companyService;

    @BeforeEach
    void cleanRepository(){
        companyService.clearAll();
        employeeService.clearAll();
    }

    @Test
    void should_return_company_with_employees_when_findAll_given() {
        //given
        Employee employee = new Employee("John Doe", 20, "male", 1000, "1");
        employeeRepositoryNew.insert(employee);
        List<Employee> employees = Arrays.asList(employee);
        List<Company> companies = createCompanies();

        CompanyWithEmployee companyWithEmployee1 = new CompanyWithEmployee(companies.get(0).getId(), companies.get(0).getName(), employees);
        CompanyWithEmployee companyWithEmployee2 = new CompanyWithEmployee(companies.get(1).getId(), companies.get(1).getName(), new ArrayList<>());
        CompanyWithEmployee companyWithEmployee3 = new CompanyWithEmployee(companies.get(2).getId(), companies.get(2).getName(), new ArrayList<>());
        List<CompanyWithEmployee> companyWithEmployees = Arrays.asList(companyWithEmployee1, companyWithEmployee2, companyWithEmployee3);

        given(companyRepositoryNew.findAll())
                .willReturn(companies);

        given(companyService.findEmployeesByCompanyId("1"))
                .willReturn(employees);

        given(companyMapper.toCompanyWithEmployee(companies.get(0), employees))
                .willReturn(companyWithEmployee1);

        given(companyMapper.toCompanyWithEmployee(companies.get(1), new ArrayList<>()))
                .willReturn(companyWithEmployee2);

        given(companyMapper.toCompanyWithEmployee(companies.get(2), new ArrayList<>()))
                .willReturn(companyWithEmployee3);

        //when
        List<CompanyWithEmployee> actual = companyService.findAll();

        //then
        verify(companyRepositoryNew).findAll();
        assertEquals(companyWithEmployees, actual);
    }

    @Test
    void should_return_employees_when_findById_given_id() {
        //given
        List<Company> companies = createCompanies();
        String companyId = companies.get(0).getId();

        given(companyRepositoryNew.findById(companyId))
                .willReturn(java.util.Optional.ofNullable(companies.get(0)));

        //when
        Company actual = companyService.findById(companyId);

        //then
        verify(companyRepositoryNew).findById(companyId);
        assertEquals(companies.get(0), actual);
    }


    @Test
    void should_return_companies_when_findByPage_given_page_and_pageSize() {
        //given
        List<Company> companies = createCompanies();
        List<Employee> employees = createEmployees();
        Integer page = 1;
        Integer pageSize = 2;
        List<Company> companiesOnPage = companies.subList(1,2);

        CompanyWithEmployee companyWithEmployee = new CompanyWithEmployee(companies.get(0).getId(), companies.get(0).getName(), employees);
        List<CompanyWithEmployee> companyWithEmployees = Arrays.asList(companyWithEmployee);

        given(companyMapper.toCompanyWithEmployee(any(), any()))
                .willReturn(companyWithEmployee);

        given(companyRepositoryNew.findAll(PageRequest.of(page, pageSize)))
                .willReturn(new PageImpl<>(companiesOnPage, PageRequest.of(1, 2), 1));

        //when
        List<CompanyWithEmployee> actualList = companyService.findByPage(page, pageSize);

        //then
        verify(companyRepositoryNew).findAll(PageRequest.of(page,pageSize));
        assertEquals(companyWithEmployees, actualList);
        assertEquals(companyWithEmployees.size(), actualList.size());
        assertEquals(companyWithEmployees.get(0).getName(), actualList.get(0).getName());
    }

    @Test
    void should_company_when_create_company_given_company() {
        //given
        Company company = new Company("1", "Coffee Shop");
        given(companyRepositoryNew.insert(company))
                .willReturn(company);

        //when
        Company actual = companyService.create(company);

        //then
        verify(companyRepositoryNew).insert(company);
        assertEquals(company, actual);
    }

    @Test
    void should_return_company_when_editCompany_given_id_and_company() {
        //given
        List<Company> companies = createCompanies();
        Company updateCompany = new Company("1", "Coffee & Tea Shop");
        Company company = companies.get(0);
        String companyId = updateCompany.getId();

        given(companyRepositoryNew.findById(companyId))
                .willReturn(java.util.Optional.ofNullable(company));

        company.setName(updateCompany.getName());

        given(companyRepositoryNew.save(company))
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
        String companyId = company.getId();

        given(companyRepositoryNew.findById(companyId))
                .willReturn(java.util.Optional.of(company));
        willDoNothing().given(companyRepositoryNew)
                .deleteById(company.getId());

        //when
        companyService.delete(companyId);

        //then
        verify(companyRepositoryNew).deleteById(companyId);
    }


    @Test
    void should_return_employees_when_findEmployeesByCompanyId_given_companyId() {
        //given
        List<Company> companies = createCompanies();
        Company company = companies.get(0);
        String companyId = company.getId();
        List<Employee> employees = Arrays.asList(new Employee( "John Doe", 20, "male", 1000, "1"),
                new Employee("Jane Doe", 21, "female", 2000, "1"));


        given(employeeService.findEmployeesByCompanyId(companyId))
                .willReturn(employees);

        //when
        List<Employee> actual = companyService.findEmployeesByCompanyId(companyId);

        //then
        assertEquals(employees, actual);

    }

    public List<Company> createCompanies() {

        Company company1 = new Company("1", "Coffee Shop");
        companyRepositoryNew.insert(company1);
        Company company2 = new Company("2", "Tea Shop");
        companyRepositoryNew.insert(company2);
        Company company3 = new Company("3", "Bakery");
        companyRepositoryNew.insert(company3);

        return Arrays.asList(company1, company2, company3);
    }

    private List<Employee> createEmployees() {
        Employee employee1 = new Employee("John Doe", 20, "male", 1000, "1");
        employeeRepositoryNew.insert(employee1);
        Employee employee2 = new Employee("Jane Doe", 21, "female", 2000, "1");
        employeeRepositoryNew.insert(employee2);
        Employee employee3 = new Employee("Doe Doe", 20, "male", 3000, "1");
        employeeRepositoryNew.insert(employee3);

        return Arrays.asList(employee1, employee2, employee3);
    }

}
