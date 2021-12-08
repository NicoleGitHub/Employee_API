package com.example.restapi.service;

import com.example.restapi.object.Company;
import com.example.restapi.object.Employee;
import com.example.restapi.repository.CompanyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
public class CompanyServiceTest {

    @Mock
    CompanyRepository companyRepository;

    @InjectMocks
    CompanyService companyService;

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
        given(companyRepository.findById(companies.get(0).getId()))
                .willReturn(companies.get(0));
        //when
        Company actual = companyService.findById(companies.get(0).getId());

        //then
        verify(companyRepository).findById(companies.get(0).getId());
        assertEquals(companies.get(0), actual);
    }

    public List<Company> createCompanies() {
        return Arrays.asList(new Company(1, "Coffee Shop", new ArrayList<>()),
                new Company(2, "Tea Shop", new ArrayList<>()),
                new Company(3, "Bakery", new ArrayList<>()));
    }

}
