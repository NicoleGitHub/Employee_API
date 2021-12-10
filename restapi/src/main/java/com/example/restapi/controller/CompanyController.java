package com.example.restapi.controller;

import com.example.restapi.mapper.CompanyMapper;
import com.example.restapi.mapper.EmployeeMapper;
import com.example.restapi.object.dto.CompanyResponse;
import com.example.restapi.object.dto.EmployeeResponse;
import com.example.restapi.object.entity.Company;
import com.example.restapi.object.entity.Employee;
import com.example.restapi.service.CompanyService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/companies")
public class CompanyController {

    private CompanyService companyService;
    private CompanyMapper companyMapper;
    private EmployeeMapper employeeMapper;

    public CompanyController(CompanyService companyService, CompanyMapper companyMapper, EmployeeMapper employeeMapper) {
        this.companyService = companyService;
        this.companyMapper = companyMapper;
        this.employeeMapper = employeeMapper;
    }

    @GetMapping
    public List<CompanyResponse> getAllCompanies() {
        return companyMapper.toResponses(companyService.findAll());
    }

    @GetMapping("/{id}")
    public CompanyResponse getCompaniesByID(@PathVariable String id) {
        Company company = companyService.findById(id);
        List<Employee> employees = companyService.findEmployeesByCompanyId(id);
        return companyMapper.toResponse(company, employees);
    }

    @GetMapping("/{id}/employees")
    public List<EmployeeResponse> getEmployeesByID(@PathVariable String id) {
        return employeeMapper.toResponses(companyService.findEmployeesByCompanyId(id));
    }

    @GetMapping(params = {"page", "pageSize"})
    public List<CompanyResponse> getCompaniesByPage(@RequestParam Integer page, @RequestParam Integer pageSize) {
        return companyMapper.toResponses(companyService.findByPage(page, pageSize));
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
