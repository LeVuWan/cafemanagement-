package com.windy.cafemanagement.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "employee")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "employeeid")
    private Long employeeId;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    private String fullname;

    private String address;

    private String phoneNumber;

    private String password;

    private Double salary;

    private String avatar;

    private Boolean isDeleted;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "permissionid")
    private Permission permission;

    public Employee(Long employeeId, String username, String address, String phoneNumber, String password,
            Double salary, String avatar, Permission permission, String fullname) {
        this.employeeId = employeeId;
        this.username = username;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.salary = salary;
        this.avatar = avatar;
        this.permission = permission;
        this.isDeleted = false;
        this.fullname = fullname;
    }

    public Employee() {
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

    public Permission getPermission() {
        return permission;
    }

    public void setPermission(Permission permission) {
        this.permission = permission;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    @Override
    public String toString() {
        return "Employee [employeeId=" + employeeId + ", username=" + username + ", fullname=" + fullname + ", address="
                + address + ", phoneNumber=" + phoneNumber + ", password=" + password + ", salary=" + salary
                + ", avatar=" + avatar + ", isDeleted=" + isDeleted + ", permission=" + permission + "]";
    }

}
