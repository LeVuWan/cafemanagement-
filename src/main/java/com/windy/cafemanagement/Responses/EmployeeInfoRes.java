package com.windy.cafemanagement.Responses;

public class EmployeeInfoRes {
    private String fullname;
    private String address;
    private String phoneNumber;
    private Double salary;
    private String permissionName;

    public EmployeeInfoRes(String fullname, String address, String phoneNumber, Double salary, String permissionName) {
        this.fullname = fullname;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.salary = salary;
        this.permissionName = permissionName;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
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

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    public String getPermissionName() {
        return permissionName;
    }

    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName;
    }

}
