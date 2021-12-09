package com.example.restapi.controller;

import com.example.restapi.object.entity.Company;
import com.example.restapi.object.entity.Employee;
import com.example.restapi.service.CompanyService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/companies")
public class CompanyController {

    CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping
    public List<Company> getAllCompanies() {
        return companyService.findAll();
    }

    @GetMapping("/{id}")
    public Company getCompaniesByID(@PathVariable String id) {
        return companyService.findById(id);
    }

    @GetMapping("/{id}/employees")
    public List<Employee> getEmployeesByID(@PathVariable String id) {
        return companyService.findEmployeesByCompanyId(id);
    }

    @GetMapping(params = {"page", "pageSize"})
    public List<Company> getCompaniesByPage(@RequestParam Integer page, @RequestParam Integer pageSize) {
        return companyService.findByPage(page, pageSize);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Company addCompany(@RequestBody Company company) {
        return companyService.create(company);
    }

    @PutMapping("/{id}")
    public Company updateCompany(@PathVariable String id, @RequestBody Company updatedCompany) {
        return companyService.editCompany(id, updatedCompany);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteEmployee(@PathVariable String id) {
        companyService.delete(id);
    }

}
