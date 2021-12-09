package com.example.restapi.mapper;

import com.example.restapi.object.dto.EmployeeRequest;
import com.example.restapi.object.entity.Employee;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class EmployeeMapper {
    public Employee toEntity(EmployeeRequest employeeRequest) {
        Employee employee = new Employee();

        // 1. manual set all attribute to employee from request
//        employee.setName(employeeRequest.getName());
//        employee.setAge(employeeRequest.getAge());
//        employee.setGender(employeeRequest.getGender());
//        employee.setSalary(employeeRequest.getSalary());

        // 2. BeanUtils only if the same
        BeanUtils.copyProperties(employeeRequest, employee);

        return employee;
    }
}
