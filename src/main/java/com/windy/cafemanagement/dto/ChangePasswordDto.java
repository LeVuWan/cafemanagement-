package com.windy.cafemanagement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ChangePasswordDto {
    @NotBlank(message = "Vui lòng nhập mật khẩu cũ.")
    private String oldPassword;

    @Size(min = 6, message = "Mật khẩu mới phải có ít nhất 6 ký tự.")
    @NotBlank(message = "Vui lòng nhập mật khẩu mới.")
    private String newPassword;

    @Size(min = 6, message = "Mật khẩu mới phải có ít nhất 6 ký tự.")
    @NotBlank(message = "Vui lòng nhập lại mật khẩu mới.")
    private String repeatPassword;

    public ChangePasswordDto(String oldPassword, String newPassword, String repeatPassword) {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
        this.repeatPassword = repeatPassword;
    }

    public ChangePasswordDto() {
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getRepeatPassword() {
        return repeatPassword;
    }

    public void setRepeatPassword(String repeatPassword) {
        this.repeatPassword = repeatPassword;
    }

}
