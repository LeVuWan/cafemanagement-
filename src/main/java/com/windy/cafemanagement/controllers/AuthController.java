package com.windy.cafemanagement.controllers;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.windy.cafemanagement.Services.CustomUserDetailsService;
import com.windy.cafemanagement.Services.EmployeeService;
import com.windy.cafemanagement.dto.ChangePasswordDto;
import com.windy.cafemanagement.dto.EditEmployeeDto;
import com.windy.cafemanagement.dto.UpdateProfileDto;
import com.windy.cafemanagement.models.Employee;

import jakarta.validation.Valid;

@Controller
public class AuthController {
    private final EmployeeService employeeService;
    private final CustomUserDetailsService customUserDetailsService;
    private final PasswordEncoder passwordEncoder;

    public AuthController(EmployeeService employeeService, CustomUserDetailsService customUserDetailsService,
            PasswordEncoder passwordEncoder) {
        this.employeeService = employeeService;
        this.customUserDetailsService = customUserDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("login")
    public String getMethodName() {
        return "/admin/auth/login";
    }

    @GetMapping("get-profile")
    public String getProfile(Model model, Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            Employee employee = employeeService.findEmployeeByUsername(authentication.getName());
            model.addAttribute("employee", employee);
        }
        return "/admin/auth/profile";
    }

    @GetMapping("update-profile/{id}")
    public String getUpdateProfileForm(Model model, @PathVariable("id") Long id) {
        EditEmployeeDto employee = employeeService.getEmployeeById(id);
        UpdateProfileDto updateProfileDto = new UpdateProfileDto(id, employee.getUsername(), employee.getFullname(),
                employee.getPhoneNumber(), employee.getAddress());
        model.addAttribute("employee", updateProfileDto);
        return "/admin/auth/update-profile";
    }

    @PostMapping("update-profile")
    public String UpdateProfile(Model model, @Valid @ModelAttribute("employee") UpdateProfileDto updateProfileDto,
            BindingResult bindingResult,
            @RequestParam("file") MultipartFile file) {
        if (bindingResult.hasErrors()) {
            return "/admin/auth/update-profile";
        }

        Employee updatedEmployee = employeeService.updateProfileService(updateProfileDto, file);

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(updatedEmployee.getUsername());

        UsernamePasswordAuthenticationToken newAuth = new UsernamePasswordAuthenticationToken(userDetails, null,
                userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(newAuth);

        return "redirect:/get-profile";
    }

    @GetMapping("change-password")
    public String getChangePassword(Model model) {
        model.addAttribute("employee", new ChangePasswordDto());
        return "admin/auth/change-password";
    }

    @PostMapping("change-password")
    public String ChangePassword(Model model, Authentication authentication,
            @Valid @ModelAttribute("employee") ChangePasswordDto changePasswordDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/auth/change-password";
        }

        Employee employee = employeeService.findEmployeeByUsername(authentication.getName());

        if (!passwordEncoder.matches(changePasswordDto.getOldPassword(), employee.getPassword())) {
            bindingResult.rejectValue("oldPassword", "error.oldPassword", "Mật khẩu cũ không chính xác.");
            return "admin/auth/change-password";
        }

        if (!changePasswordDto.getNewPassword().equals(changePasswordDto.getRepeatPassword())) {
            bindingResult.rejectValue("repeatPassword", "error.repeatPassword", "Mật khẩu nhập lại không khớp.");
            return "admin/auth/change-password";
        }

        employee.setPassword(passwordEncoder.encode(changePasswordDto.getNewPassword()));
        employeeService.saveEmployeeService(employee);

        return "redirect:admin";
    }
}
