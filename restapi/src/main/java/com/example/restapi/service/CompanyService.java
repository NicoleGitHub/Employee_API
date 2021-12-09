package com.example.restapi.service;

import com.example.restapi.object.entity.Company;
import com.example.restapi.object.entity.Employee;
import com.example.restapi.repository.CompanyRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyService {

    private CompanyRepository companyRepository;

    private EmployeeService employeeService;

    public CompanyService(CompanyRepository companyRepository, EmployeeService employeeService) {
        this.companyRepository = companyRepository;
        this.employeeService = employeeService;
    }

    public List<Company> findAll() {
        return companyRepository.findAll();
    }

    public Company findById(String id) {
        return companyRepository.findById(id);
    }

    public List<Employee> findEmployeesByCompanyId(String companyId) {
        return employeeService.findEmployeesByCompanyId(companyId);
    }

    public List<Company> findByPage(Integer page, Integer pageSize) {
        return companyRepository.findByPage(page, pageSize);
    }

    public Company create(Company company) {
        return companyRepository.create(company);
    }

    public Company editCompany(String id, Company updatedCompany) {
        Company company = findById(id);

        if(updatedCompany.getCompanyName() != null) {
            company.setCompanyName(updatedCompany.getCompanyName());
        }

        return companyRepository.save(id, company);
    }

    public void delete(String id) {
        companyRepository.delete(id);
    }
}
