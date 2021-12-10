package com.example.restapi.service;

import com.example.restapi.exception.NoCompaniesFoundException;
import com.example.restapi.mapper.CompanyMapper;
import com.example.restapi.object.bo.CompanyWithEmployee;
import com.example.restapi.object.entity.Company;
import com.example.restapi.object.entity.Employee;
import com.example.restapi.repository.CompanyRepositoryNew;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CompanyService {

    private CompanyRepositoryNew companyRepositoryNew;

    private EmployeeService employeeService;

    private CompanyMapper companyMapper;

    public CompanyService(CompanyRepositoryNew companyRepositoryNew, EmployeeService employeeService, CompanyMapper companyMapper) {
        this.companyRepositoryNew = companyRepositoryNew;
        this.employeeService = employeeService;
        this.companyMapper = companyMapper;
    }

    public List<CompanyWithEmployee> findAll() {
        return companyRepositoryNew.findAll().stream()
                .map(company -> {
                    List<Employee> employees = findEmployeesByCompanyId(company.getId());
                    System.out.println("find All employees" + employees);
                    System.out.println("find All mapper" + companyMapper.toCompanyWithEmployee(company, employees));
                    return companyMapper.toCompanyWithEmployee(company, employees);
                }).collect(Collectors.toList());
    }

    public Company findById(String id) {
        return companyRepositoryNew.findById(id).orElseThrow(NoCompaniesFoundException::new);
    }

    public List<Employee> findEmployeesByCompanyId(String companyId) {
        return employeeService.findEmployeesByCompanyId(companyId);
    }

    public List<CompanyWithEmployee> findByPage(Integer page, Integer pageSize) {
        return companyRepositoryNew.findAll(PageRequest.of(page,pageSize)).getContent().stream()
                .map(company -> {
                    List<Employee> employees = findEmployeesByCompanyId(company.getId());
                    return companyMapper.toCompanyWithEmployee(company, employees);
                }).collect(Collectors.toList());
    }

    public Company create(Company company) {
        return companyRepositoryNew.insert(company);
    }

    public Company editCompany(String id, Company updatedCompany) {
        Company company = findById(id);

        if(updatedCompany.getName() != null) {
            company.setName(updatedCompany.getName());
        }

        return save(company);
    }

    public Company save(Company company) {
        return companyRepositoryNew.save(company);
    }

    public void delete(String id) {
        companyRepositoryNew.deleteById(id);
    }

    public void clearAll() {
        companyRepositoryNew.deleteAll();
    }
}
