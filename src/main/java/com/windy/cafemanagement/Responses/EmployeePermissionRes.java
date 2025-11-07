package com.windy.cafemanagement.Responses;

public class EmployeePermissionRes {
    private String username;
    private String password;
    private String permissionName;
    private String avatar;

    public EmployeePermissionRes(String username, String password, String permissionName, String avatar) {
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

    @Override
    public String toString() {
        return "EmployeePermissionRes [username=" + username + ", password=" + password + ", permissionName="
                + permissionName + ", avatar=" + avatar + "]";
    }

}
