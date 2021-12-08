package com.example.restapi.repository;

import com.example.restapi.exception.NoCompaniesFoundException;
import com.example.restapi.object.Company;
import com.example.restapi.object.Employee;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class CompanyRepository {
    private List<Company> companies = new ArrayList<>();

    public CompanyRepository() {
        companies.add(new Company(1, "company one", new ArrayList<>()));
            addEmployee(1,new Employee(1, "name one", 100, "male", 10000000));
            addEmployee(1,new Employee(1, "name one", 100, "male", 10000000));
        companies.add(new Company(2, "company two", new ArrayList<>()));
            addEmployee(2, new Employee(1, "name three", 300, "female", 30000000));
            addEmployee(2, new Employee(1, "name four", 400, "male", 40000000));
        companies.add(new Company(3, "company three", new ArrayList<>()));
            addEmployee(2, new Employee(1, "name five", 500, "male", 50000000));
    }

    public void addEmployee(Integer id, Employee employee) {
        Company company = findById(id);
        Integer nextId = company.getEmployees().stream()
                .mapToInt(Employee::getId)
                .max()
                .orElse(0)+1;
        employee.setId(nextId);
        company.getEmployees().add(employee);
    }

    public List<Company> findAll() {
        return companies;
    }

    public Company findById(Integer id) {
        return companies.stream()
                .filter(company -> company.getId().equals(id))
                .findFirst()
                .orElseThrow(NoCompaniesFoundException::new);
    }

    public List<Employee> findEmployeesByCompanyId(Integer id) {
        return findById(id).getEmployees();
    }

    public List<Company> findByPage(Integer page, Integer pageSize) {
        return companies.stream().skip((long) (page - 1) * pageSize).limit(pageSize).collect(Collectors.toList());
    }

    public Company create(Company company) {
        Integer nextId = companies.stream()
                .mapToInt(Company::getId)
                .max()
                .orElse(0)+1;
        company.setId(nextId);
        companies.add(company);
        return company;
    }

    public Company editCompany(Integer id, Company updatedCompany) {
        Company company = findById(id);

        if(updatedCompany.getCompanyName() != null) {
            company.setCompanyName(updatedCompany.getCompanyName());
        }

        return save(id, company);
    }

    public Company save(Integer id, Company updatedCompany) {
        Company company = findById(id);
        if(updatedCompany.getCompanyName().isEmpty()) {
            company.setCompanyName(updatedCompany.getCompanyName());
        }
        return company;
    }

    public void delete(Integer id) {
        companies.remove(findById(id));
    }

    public void clearAll() {
        companies.clear();
    }
}
