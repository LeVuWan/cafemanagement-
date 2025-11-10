package com.windy.cafemanagement.dto;

/**
 * edit employee dto class
 *
 * Version 1.0
 *
 * Date: 10-11-2025
 *
 * Copyright 
 *
 * Modification Logs:
 * DATE                 AUTHOR          DESCRIPTION
 * -----------------------------------------------------------------------
 * 10-11-2025         VuLQ            Create
 */
public class EditEmployeeDto {
    private Long employeeId;

    private String username;

    private String address;

    private String phoneNumber;

    private String password;

    private String fullname;

    private Long permissionId;

    private Double salary;

    private String avatar;

    public EditEmployeeDto(Long employeeId, String username,
            String address,
            String phoneNumber,
            String password,
            String fullname,
            Long permissionId,
            Double salary,
            String avatar) {
        this.employeeId = employeeId;
        this.username = username;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.fullname = fullname;
        this.permissionId = permissionId;
        this.salary = salary;
        this.avatar = avatar;
    }

    public EditEmployeeDto() {
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public Long getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(Long permissionId) {
        this.permissionId = permissionId;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Override
    public String toString() {
        return "EditEmployeeDto [employeeId=" + employeeId + ", username=" + username + ", address=" + address
                + ", phoneNumber=" + phoneNumber + ", password=" + password + ", fullname=" + fullname
                + ", permissionId=" + permissionId + ", salary=" + salary + ", avatar=" + avatar + "]";
    }

}
