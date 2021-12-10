package com.example.restapi.mapper;

import com.example.restapi.object.bo.CompanyWithEmployee;
import com.example.restapi.object.dto.CompanyRequest;
import com.example.restapi.object.dto.CompanyResponse;
import com.example.restapi.object.entity.Company;
import com.example.restapi.object.entity.Employee;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CompanyMapper {

    private EmployeeMapper employeeMapper;

    public CompanyMapper(EmployeeMapper employeeMapper) {
        this.employeeMapper = employeeMapper;
    }

    public Employee toEntity(CompanyRequest companyRequest) {
        Employee employee = new Employee();

        BeanUtils.copyProperties(companyRequest, employee);

        return employee;
    }

    public CompanyResponse toResponse(Company company, List<Employee> employees) {
        CompanyResponse companyResponse = new CompanyResponse();

        BeanUtils.copyProperties(company, companyResponse);
        companyResponse.setEmployees(employees.stream().map(employee -> employeeMapper.toResponse(employee)).collect(Collectors.toList()));

        return companyResponse;
    }

    public CompanyResponse toResponse(CompanyWithEmployee companyWithEmployee) {
        CompanyResponse companyResponse = new CompanyResponse();

        BeanUtils.copyProperties(companyWithEmployee, companyResponse);
        companyResponse.setEmployees(companyWithEmployee.getEmployees().stream().map(employee -> employeeMapper.toResponse(employee)).collect(Collectors.toList()));

        return companyResponse;
    }

    public List<CompanyResponse> toResponses(List<CompanyWithEmployee> CompanyWithEmployees) {
        return CompanyWithEmployees.stream().map(CompanyWithEmployee -> toResponse(CompanyWithEmployee)).collect(Collectors.toList());
    }

    public CompanyWithEmployee toCompanyWithEmployee(Company company, List<Employee> employees) {
        System.out.println("company name" + company.getName());
        System.out.println("employees size" + employees.size());
        CompanyWithEmployee companyWithEmployee = new CompanyWithEmployee();

        BeanUtils.copyProperties(company, companyWithEmployee);
        companyWithEmployee.setEmployees(employees);

        System.out.println("companyWithEmployee name" + companyWithEmployee.getName());
        System.out.println("companyWithEmployee setEmployees" + companyWithEmployee.getEmployees().size());
        return companyWithEmployee;
    }

}
