package com.example.restapi.object.bo;

import com.example.restapi.object.entity.Employee;

import java.util.List;

public class CompanyWithEmployee {
    private String id;
    private String name;
    private List<Employee> employees;

    public CompanyWithEmployee() {
    }

    public CompanyWithEmployee(String id, String name, List<Employee> employees) {
        this.id = id;
        this.name = name;
        this.employees = employees;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }
}
