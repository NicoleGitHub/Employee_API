package com.example.restapi.service;

import com.example.restapi.exception.NoEmployeesFoundException;
import com.example.restapi.object.entity.Employee;
import com.example.restapi.repository.EmployeeRepositoryNew;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {

    private EmployeeRepositoryNew employeeRepositoryNew;

    public EmployeeService(EmployeeRepositoryNew employeeRepositoryNew) {
        this.employeeRepositoryNew = employeeRepositoryNew;
    }

    public List<Employee> findAll() {
        return employeeRepositoryNew.findAll();
    }

    public Employee findById(String id) {
        return employeeRepositoryNew.findById(id).orElseThrow(NoEmployeesFoundException::new);
    }

    public List<Employee> getByGender(String gender) {
        return employeeRepositoryNew.findAllByGender(gender);
    }

    public List<Employee> findByPage(Integer page, Integer pageSize) {
        return employeeRepositoryNew.findAll(PageRequest.of(page,pageSize)).getContent();
    }

    public Employee edit(String id, Employee updatedEmployee) {
        Employee employee = findById(id);

        if(updatedEmployee.getAge() != null) {
            employee.setAge(updatedEmployee.getAge());
        }
        if(updatedEmployee.getSalary() != null) {
            employee.setSalary(updatedEmployee.getSalary());
        }

        return save(employee);
    }

    public Employee save(Employee employee) {
        return employeeRepositoryNew.save(employee);
    }

    public Employee create(Employee employee) {
        return employeeRepositoryNew.insert(employee);
    }

    public void delete(String id) {
        employeeRepositoryNew.deleteById(id);
    }

    public List<Employee> findEmployeesByCompanyId(String companyId){
        return employeeRepositoryNew.findAllByCompanyId(companyId);
    }

    public void clearAll() {
        employeeRepositoryNew.deleteAll();
    }
}