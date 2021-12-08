package com.example.restapi.service;

import com.example.restapi.object.Company;
import com.example.restapi.object.Employee;
import com.example.restapi.repository.CompanyRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyService {

    private CompanyRepository companyRepository;

    public void addEmployee(Integer id, Employee employee) {
        companyRepository.addEmployee(id, employee);
    }

    public List<Company> findAll() {
        return companyRepository.findAll();
    }

    public Company findById(Integer id) {
        return companyRepository.findById(id);
    }

    public List<Employee> findEmployeesByCompanyId(Integer companyId) {
        return companyRepository.findEmployeesByCompanyId(companyId);
    }

    public List<Company> findByPage(Integer page, Integer pageSize) {
        return companyRepository.findByPage(page, pageSize);
    }

    public Company create(Company company) {
        return companyRepository.create(company);
    }

    public Company editCompany(Integer id, Company updatedCompany) {
        Company company = findById(id);

        if(updatedCompany.getCompanyName() != null) {
            company.setCompanyName(updatedCompany.getCompanyName());
        }

        return companyRepository.save(id, company);
    }
}
