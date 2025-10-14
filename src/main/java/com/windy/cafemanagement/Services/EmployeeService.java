package com.windy.cafemanagement.Services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.windy.cafemanagement.dto.CreateEmployeeDto;
import com.windy.cafemanagement.models.Employee;
import com.windy.cafemanagement.repositories.EmployeeRepository;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final PermissionService permissionService;

    public EmployeeService(EmployeeRepository employeeService, PermissionService permissionService) {
        this.employeeRepository = employeeService;
        this.permissionService = permissionService;
    }

    public List<Employee> getAllEmployeesService() {
        return employeeRepository.findAllByIsDeleted(false);
    }

    public void createNewAEmployee(CreateEmployeeDto createEmployeeDto, String imgUrl) {
        Employee newEmployee = createEmployeeDtoToEmpolyee(createEmployeeDto);
        newEmployee.setAvatar(imgUrl);
        employeeRepository.save(newEmployee);
    }

    public Employee createEmployeeDtoToEmpolyee(CreateEmployeeDto createEmployeeDto) {
        Employee employee = new Employee();
        employee.setAddress(createEmployeeDto.getAddress());
        employee.setPassword(createEmployeeDto.getPassword());
        employee.setUsername(createEmployeeDto.getUsername());
        employee.setSalary(createEmployeeDto.getSalary());
        employee.setFullname(createEmployeeDto.getFullname());
        employee.setPhoneNumber(createEmployeeDto.getPhoneNumber());
        employee.setIsDeleted(false);
        employee.setPermission(permissionService.findByPermissionById(createEmployeeDto.getPermissionId()));
        return employee;
    }
}
