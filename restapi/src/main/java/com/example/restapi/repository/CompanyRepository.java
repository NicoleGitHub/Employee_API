package com.example.restapi.repository;

import com.example.restapi.exception.NoCompaniesFoundException;
import com.example.restapi.object.entity.Company;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class CompanyRepository {
    private List<Company> companies = new ArrayList<>();

    public CompanyRepository() {
        companies.add(new Company("1", "company one"));
        companies.add(new Company("2", "company two"));
        companies.add(new Company("3", "company three"));
    }

    public List<Company> findAll() {
        return companies;
    }

    public Company findById(String id) {
        return companies.stream()
                .filter(company -> company.getId().equals(id))
                .findFirst()
                .orElseThrow(NoCompaniesFoundException::new);
    }

    public List<Company> findByPage(Integer page, Integer pageSize) {
        return companies.stream().skip((long) (page - 1) * pageSize).limit(pageSize).collect(Collectors.toList());
    }

    public Company create(Company company) {
//        Integer nextId = companies.stream()
//                .mapToInt(Company::getId)
//                .max()
//                .orElse(0)+1;
        company.setId(String.valueOf(companies.size()+1));
        companies.add(company);
        return company;
    }

    public Company save(String id, Company updatedCompany) {
        Company company = findById(id);
        if(updatedCompany.getCompanyName().isEmpty()) {
            company.setCompanyName(updatedCompany.getCompanyName());
        }
        return updatedCompany;
    }

    public void delete(String id) {
        companies.remove(findById(id));
    }

    public void clearAll() {
        companies.clear();
    }
}
