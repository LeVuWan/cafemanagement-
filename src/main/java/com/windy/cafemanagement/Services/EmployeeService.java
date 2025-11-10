package com.windy.cafemanagement.Services;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.windy.cafemanagement.Responses.EmployeeInfoRes;
import com.windy.cafemanagement.dto.CreateEmployeeDto;
import com.windy.cafemanagement.dto.EditEmployeeDto;
import com.windy.cafemanagement.dto.UpdateProfileDto;
import com.windy.cafemanagement.models.Employee;
import com.windy.cafemanagement.repositories.EmployeeRepository;

import jakarta.persistence.EntityNotFoundException;
import javassist.NotFoundException;

/**
 * EmployeeService
 *
 * Version 1.0
 *
 * Date: 10-11-2025
 *
 * Copyright
 *
 * Modification Logs:
 * DATE AUTHOR DESCRIPTION
 * -----------------------------------------------------------------------
 * 10-11-2025 VuLQ Create
 */
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

    /**
     * get all employees by keyword and isDeleted false
     * 
     * @param keyword
     * @return List<Employee>
     * @throws DataAccessException
     */
    public List<Employee> getAllEmployeesService(String keyword) throws DataAccessException {
        if (keyword == null || keyword.trim().isEmpty()) {
            return employeeRepository.findAllByIsDeleted(false);
        } else {
            return employeeRepository.searchByKeyword(keyword.trim());
        }
    }

    /**
     * create new employee
     * 
     * @param createEmployeeDto
     * @param imgUrl
     * @throws DataAccessException
     */
    public void createNewAEmployee(CreateEmployeeDto createEmployeeDto, String imgUrl) throws DataAccessException {
        Employee newEmployee = createEmployeeDtoToEmpolyee(createEmployeeDto);
        newEmployee.setAvatar(imgUrl);
        newEmployee.setPassword(passwordEncoder.encode(createEmployeeDto.getPassword()));
        employeeRepository.save(newEmployee);
    }

    /**
     * get employee by username
     * 
     * @param username
     * @return Employee
     * @throws DataAccessException
     * @throws NotFoundException
     */
    public Employee findEmployeeByUsername(String username) {
        Employee employee = employeeRepository.findByUsername(username);
        if (employee == null || employee.getIsDeleted() == true) {
            throw new EntityNotFoundException("Không tìm thấy nhân viên có username: " + username);
        }
        return employee;
    }

    /**
     * save employee
     * 
     * @param employee
     * @throws DataAccessException
     */
    public void saveEmployeeService(Employee employee) throws DataAccessException {
        employeeRepository.save(employee);
    }

    /**
     * Edit employee
     * 
     * @param editEmployeeDto
     * @throws DataAccessException
     * @throws EntityNotFoundException
     */
    public void editEmployee(EditEmployeeDto editEmployeeDto) throws DataAccessException {
        Employee existingEmployee = employeeRepository.findById(editEmployeeDto.getEmployeeId())
                .orElseThrow(() -> new EntityNotFoundException(
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

    /**
     * Soft delete employee
     * 
     * @param id
     * @return Employee
     * @throws DataAccessException
     * @throws EntityNotFoundException
     */
    public Employee deleteEmployeeService(Long id) throws DataAccessException, EntityNotFoundException {
        Employee employee = employeeRepository.softDeleteById(id);
        if (employee == null) {
            throw new EntityNotFoundException("Không tìm thấy nhân viên có ID: " + id);
        }
        return employee;
    }

    /**
     * get EditEmployeeDto by id
     * 
     * @param id
     * @return Employee
     * @throws DataAccessException
     * @throws EntityNotFoundException
     */
    public EditEmployeeDto getEmployeeById(Long id) throws DataAccessException, EntityNotFoundException {
        return employeeToCreateEmployeeDto(
                employeeRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(
                        "Không tìm thấy nhân viên với ID: " + id)));
    }

    /**
     * convert CreateEmployeeDto to Employee
     * 
     * @param createEmployeeDto
     * @return Employee
     */
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

    /**
     * update profile
     * 
     * @param updateProfileDto
     * @param file
     * @return Employee
     * @throws DataAccessException
     * @throws EntityNotFoundException
     * @throws Exception
     */
    public Employee updateProfileService(UpdateProfileDto updateProfileDto, MultipartFile file)
            throws EntityNotFoundException, Exception {

        Employee existingEmployee = employeeRepository.findById(updateProfileDto.getEmployeeId())
                .orElseThrow(() -> new EntityNotFoundException(
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

    /**
     * convert Employee to EditEmployeeDto
     * 
     * @param employee
     * @return EditEmployeeDto
     */
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

    /**
     * convert EditEmployeeDto to Employee
     * 
     * @param editEmployeeDto
     * @return Employee
     */
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

    /**
     * check string is not blank
     * 
     * @param value
     * @return boolean
     */
    private boolean isNotBlank(String value) {
        return value != null && !value.trim().isEmpty();
    }

    /**
     * get employee information with permission name
     * 
     * @return List<EmployeeInfoRes>
     */
    public List<EmployeeInfoRes> getEmployeeInformationService() {
        return employeeRepository.getEmployeeInformation().stream()
                .map(obj -> new EmployeeInfoRes(
                        (String) obj[0],
                        (String) obj[1],
                        (String) obj[2],
                        (Double) obj[3],
                        (String) obj[4]))
                .toList();
    }
}
