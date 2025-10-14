package com.windy.cafemanagement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class CreateEmployeeDto {
    @NotBlank(message = "Đên đăng nhập không để trống")
    private String username;

    @NotBlank(message = "Địa chỉ không để trgốn")
    private String address;

    @NotBlank(message = "Số điện thoại không để trống")
    private String phoneNumber;

    @NotBlank(message = "Mật khẫu không để trống")
    @Size(min = 3, max = 50, message = "Mật khẩu phải từ 3 đến 50 ký tự")
    private String password;

    @NotBlank(message = "Họ tên không được để trống")
    private String fullname;

    @NotNull(message = "Bạn chưa chon permission")
    private Long permissionId;

    @NotNull(message = "Lương không được để trống")
    @Positive(message = "Giá phải lớn hơn 0")
    private Double salary;

    public CreateEmployeeDto(@NotBlank(message = "Đên đăng nhập không để trống") String username,
            @NotBlank(message = "Địa chỉ không để trgốn") String address,
            @NotBlank(message = "Số điện thoại không để trống") String phoneNumber,
            @NotBlank(message = "Mật khẫu không để trống") @Size(min = 3, max = 50, message = "Mật khẩu phải từ 3 đến 50 ký tự") String password,
            @NotBlank(message = "Họ tên không được để trống") String fullname,
            @NotBlank(message = "Bạn chưa chon permission") Long permissionId,
            @NotNull(message = "Lương không được để trống") Double salary) {
        this.username = username;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.fullname = fullname;
        this.permissionId = permissionId;
        this.salary = salary;
    }

    public CreateEmployeeDto() {
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

    @Override
    public String toString() {
        return "CreateEmployeeDto [username=" + username + ", address=" + address + ", phoneNumber=" + phoneNumber
                + ", password=" + password + ", fullname=" + fullname + ", permissionId=" + permissionId + ", salary="
                + salary + "]";
    }

}
