package com.windy.cafemanagement.Responses;

/**
 * EmployeeLoginInfoRes class
 *
 * Version 1.0
 *
 * Date: 11-10-2025
 *
 * Copyright
 *
 * Modification Logs:
 * DATE AUTHOR DESCRIPTION
 * -----------------------------------------------------------------------
 * 11-10-2025 VuLQ Create
 */
public class EmployeeLoginInfoRes {
    private Long employeeId;

    private String username;

    private String password;

    private String permissionName;

    private String avatar;

    public EmployeeLoginInfoRes() {
    }

    public EmployeeLoginInfoRes(Long employeeId,String username, String password, String permissionName, String avatar
            ) {
        this.employeeId = employeeId;
        this.username = username;
        this.password = password;
        this.permissionName = permissionName;
        this.avatar = avatar;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPermissionName() {
        return permissionName;
    }

    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

}
