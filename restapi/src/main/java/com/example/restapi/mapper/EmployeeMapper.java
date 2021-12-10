package com.example.restapi.mapper;

import com.example.restapi.object.dto.EmployeeRequest;
import com.example.restapi.object.dto.EmployeeResponse;
import com.example.restapi.object.entity.Employee;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class EmployeeMapper {

    public Employee toEntity(EmployeeRequest employeeRequest) {
        Employee employee = new Employee();

        BeanUtils.copyProperties(employeeRequest, employee);

        return employee;
    }

    public EmployeeResponse toResponse(Employee employee) {
        EmployeeResponse employeeResponse = new EmployeeResponse();

        BeanUtils.copyProperties(employee, employeeResponse);

        return employeeResponse;

    }

    public List<EmployeeResponse> toResponses(List<Employee> employees) {
        return employees.stream().map(employee -> toResponse(employee)).collect(Collectors.toList());
    }
}
