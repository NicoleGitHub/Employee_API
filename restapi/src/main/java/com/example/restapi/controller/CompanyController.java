package com.example.restapi.controller;

import com.example.restapi.object.Company;
import com.example.restapi.object.Employee;
import com.example.restapi.repository.CompanyRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/companies")
public class CompanyController {

    CompanyRepository companyRepository;

    public CompanyController(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @GetMapping
    public List<Company> getAllCompanies() {
        return companyRepository.getAll();
    }

    @GetMapping("/{id}")
    public Company getCompaniesByID(@PathVariable Integer id) {
        return companyRepository.getById(id);
    }

    @GetMapping("/{id}/employees")
    public List<Employee> getEmployeesByID(@PathVariable Integer id) {
        return companyRepository.getEmployeesById(id);
    }

    @GetMapping(params = {"page", "pageSize"})
    public List<Company> getCompaniesByPage(@RequestParam Integer page, @RequestParam Integer pageSize) {
        return companyRepository.findByPage(page, pageSize);
    }



}
