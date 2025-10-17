package com.windy.cafemanagement.dto;

import jakarta.validation.constraints.NotBlank;

public class UpdateProfileDto {
    private Long employeeId;
    @NotBlank(message = "Tên đăng nhập không để trống")
    private String username;
    @NotBlank(message = "Họ và tên không để trông")
    private String fullname;
    @NotBlank(message = "Số điện thoại không được để trống")
    private String phoneNumber;
    @NotBlank(message = "Địa chỉ không được để trống")
    private String address;

    public UpdateProfileDto() {
    }

    public UpdateProfileDto(Long employeeId, @NotBlank(message = "Tên đăng nhập không để trống") String username,
            @NotBlank(message = "Họ và tên không để trông") String fullname,
            @NotBlank(message = "Số điện thoại không được để trống") String phoneNumber, String address) {
        this.employeeId = employeeId;
        this.username = username;
        this.fullname = fullname;
        this.phoneNumber = phoneNumber;
        this.address = address;
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

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}
