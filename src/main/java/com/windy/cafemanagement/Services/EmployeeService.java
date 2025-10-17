package com.windy.cafemanagement.Services;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.windy.cafemanagement.dto.CreateEmployeeDto;
import com.windy.cafemanagement.dto.EditEmployeeDto;
import com.windy.cafemanagement.dto.UpdateProfileDto;
import com.windy.cafemanagement.models.Employee;
import com.windy.cafemanagement.repositories.EmployeeRepository;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final PermissionService permissionService;
    private final PasswordEncoder passwordEncoder;
    private final UploadService uploadService;

    public EmployeeService(EmployeeRepository employeeService, PermissionService permissionService,
            PasswordEncoder passwordEncoder, UploadService uploadService) {
        this.employeeRepository = employeeService;
        this.permissionService = permissionService;
        this.passwordEncoder = passwordEncoder;
        this.uploadService = uploadService;
    }

    public List<Employee> getAllEmployeesService(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return employeeRepository.findAllByIsDeleted(false);
        } else {
            return employeeRepository.searchByKeyword(keyword.trim());
        }
    }

    public void createNewAEmployee(CreateEmployeeDto createEmployeeDto, String imgUrl) {
        Employee newEmployee = createEmployeeDtoToEmpolyee(createEmployeeDto);
        newEmployee.setAvatar(imgUrl);
        newEmployee.setPassword(passwordEncoder.encode(createEmployeeDto.getPassword()));
        employeeRepository.save(newEmployee);
    }

    public Employee findEmployeeByUsername(String username) {
        return employeeRepository.findByUsername(username);
    }

    public void saveEmployeeService(Employee employee) {
        employeeRepository.save(employee);
    }

    public void editEmployee(EditEmployeeDto editEmployeeDto) {
        Employee existingEmployee = employeeRepository.findById(editEmployeeDto.getEmployeeId())
                .orElseThrow(() -> new RuntimeException(
                        "Không tìm thấy nhân viên với ID: " + editEmployeeDto.getEmployeeId()));

        if (isNotBlank(editEmployeeDto.getUsername()))
            existingEmployee.setUsername(editEmployeeDto.getUsername());
        if (isNotBlank(editEmployeeDto.getAddress()))
            existingEmployee.setAddress(editEmployeeDto.getAddress());
        if (isNotBlank(editEmployeeDto.getPhoneNumber()))
            existingEmployee.setPhoneNumber(editEmployeeDto.getPhoneNumber());
        if (isNotBlank(editEmployeeDto.getPassword()))
            existingEmployee.setPassword(editEmployeeDto.getPassword());
        if (isNotBlank(editEmployeeDto.getFullname()))
            existingEmployee.setFullname(editEmployeeDto.getFullname());
        if (editEmployeeDto.getPermissionId() != null)
            existingEmployee.setPermission(permissionService.findByPermissionById(editEmployeeDto.getPermissionId()));
        if (editEmployeeDto.getSalary() != null)
            existingEmployee.setSalary(editEmployeeDto.getSalary());
        if (isNotBlank(editEmployeeDto.getAvatar()))
            existingEmployee.setAvatar(editEmployeeDto.getAvatar());
        if (isNotBlank(editEmployeeDto.getPassword())) {
            existingEmployee.setPassword(passwordEncoder.encode(editEmployeeDto.getPassword()));
        }

        employeeRepository.save(existingEmployee);
    }

    public void deleteEmployeeService(Long id) {
        employeeRepository.softDeleteById(id);
    }

    public EditEmployeeDto getEmployeeById(Long id) {
        return employeeToCreateEmployeeDto(employeeRepository.findById(id).get());
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

    public Employee updateProfileService(UpdateProfileDto updateProfileDto, MultipartFile file) {
        Employee existingEmployee = employeeRepository.findById(updateProfileDto.getEmployeeId())
                .orElseThrow(() -> new RuntimeException(
                        "Không tìm thấy nhân viên với ID: " + updateProfileDto.getEmployeeId()));

        String avatarUrl = null;
        if (file != null && !file.isEmpty()) {
            avatarUrl = uploadService.uploadImage(file, "avatar");
        }

        if (isNotBlank(updateProfileDto.getUsername())) {
            existingEmployee.setUsername(updateProfileDto.getUsername());
        }
        if (isNotBlank(updateProfileDto.getFullname())) {
            existingEmployee.setFullname(updateProfileDto.getFullname());
        }
        if (isNotBlank(updateProfileDto.getAddress())) {
            existingEmployee.setAddress(updateProfileDto.getAddress());
        }
        if (isNotBlank(updateProfileDto.getPhoneNumber())) {
            existingEmployee.setPhoneNumber(updateProfileDto.getPhoneNumber());
        }
        if (isNotBlank(avatarUrl)) {
            existingEmployee.setAvatar(avatarUrl);
        }

        employeeRepository.save(existingEmployee);
        return existingEmployee;
    }

    public EditEmployeeDto employeeToCreateEmployeeDto(Employee employee) {
        EditEmployeeDto editEmployeeDto = new EditEmployeeDto();
        editEmployeeDto.setAvatar(employee.getAvatar());
        editEmployeeDto.setEmployeeId(employee.getEmployeeId());
        editEmployeeDto.setUsername(employee.getUsername());
        editEmployeeDto.setAddress(employee.getAddress());
        editEmployeeDto.setPhoneNumber(employee.getPhoneNumber());
        editEmployeeDto.setFullname(employee.getFullname());
        editEmployeeDto.setSalary(employee.getSalary());
        editEmployeeDto.setPassword(employee.getPassword());
        editEmployeeDto.setPermissionId(employee.getPermission().getPermissionId());
        return editEmployeeDto;
    }

    public Employee editEmployeeDtoToEmployee(EditEmployeeDto editEmployeeDto) {
        Employee employee = new Employee();
        employee.setEmployeeId(editEmployeeDto.getEmployeeId());
        employee.setAddress(editEmployeeDto.getAddress());
        employee.setPassword(editEmployeeDto.getPassword());
        employee.setUsername(editEmployeeDto.getUsername());
        employee.setSalary(editEmployeeDto.getSalary());
        employee.setFullname(editEmployeeDto.getFullname());
        employee.setPhoneNumber(editEmployeeDto.getPhoneNumber());
        employee.setIsDeleted(false);
        employee.setAvatar(editEmployeeDto.getAvatar());
        employee.setPermission(permissionService.findByPermissionById(editEmployeeDto.getPermissionId()));
        return employee;
    }

    private boolean isNotBlank(String value) {
        return value != null && !value.trim().isEmpty();
    }
}
