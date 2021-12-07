package com.example.restapi.repository;

import com.example.restapi.exception.NoCompaniesFoundException;
import com.example.restapi.exception.NoEmployeesFoundException;
import com.example.restapi.object.Company;
import com.example.restapi.object.Employee;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class CompanyRepository {
    private List<Company> companies = new ArrayList<>();

    public CompanyRepository() {
        companies.add(new Company(1, "company one", Arrays.asList(
                new Employee(1, "name one", 100, "male", 10000000),
                new Employee(2, "name two", 200, "male", 20000000))));
        companies.add(new Company(2, "company two", Arrays.asList(
                new Employee(3, "name three", 300, "female", 30000000),
                new Employee(4, "name four", 400, "male", 40000000))));
        companies.add(new Company(3, "company three", Arrays.asList(
                new Employee(5, "name five", 100, "female", 50000000))));
    }

    public List<Company> getAll() {
        return companies;
    }

    public Company getById(Integer id) {
        return companies.stream()
                .filter(company -> company.getId().equals(id))
                .findFirst()
                .orElseThrow(NoCompaniesFoundException::new);
    }

    public List<Employee> getEmployeesById(Integer id) {
        return getById(id).getEmployees();
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

//    public Company save(Integer id, Company updatedCompany) {
//        Company company = getById(id);
//        companies.remove(company);
//        companies.add(updatedCompany);
//        return updatedCompany;
//    }
}
