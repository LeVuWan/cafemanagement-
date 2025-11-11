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
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.windy.cafemanagement.Services.CustomUserDetailsService;
import com.windy.cafemanagement.Services.EmployeeService;
import com.windy.cafemanagement.dto.ChangePasswordDto;
import com.windy.cafemanagement.dto.EditEmployeeDto;
import com.windy.cafemanagement.dto.UpdateProfileDto;
import com.windy.cafemanagement.models.Employee;

/**
 * EmploymentDetailsDAO
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
 * 11-10-2025 VuLQ Create AuthController class
 */
@Controller
public class AuthController {
    private final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final EmployeeService employeeService;
    private final CustomUserDetailsService customUserDetailsService;
    private final PasswordEncoder passwordEncoder;

    public AuthController(EmployeeService employeeService, CustomUserDetailsService customUserDetailsService,
            PasswordEncoder passwordEncoder) {
        this.employeeService = employeeService;
        this.customUserDetailsService = customUserDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * get form login
     * 
     * @return String
     */
    @GetMapping("login")
    public String getMethodName() {
        return "/admin/auth/login";
    }

    /**
     * get form profile
     * 
     * @param model, authentication
     * @return String
     * @throws
     */
    @GetMapping("get-profile")
    public String getProfile(Model model, Authentication authentication) {
        try {
            if (authentication != null && authentication.isAuthenticated()) {
                Employee employee = employeeService.findEmployeeByUsername(authentication.getName());
                model.addAttribute("employee", employee);
            }
        } catch (EntityNotFoundException e) {
            logger.warn("Entity not found in getProfile: {}", e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
            return "admin/errors/500-error";
        } catch (DataAccessException e) {
            logger.error("Data access error in getProfile: {}", e.getMessage(), e);
            model.addAttribute("errorMessage", "Lỗi truy cập dữ liệu");
            return "admin/errors/500-error";
        } catch (UsernameNotFoundException e) {
            logger.warn("User not found in getProfile: {}", e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
            return "admin/errors/500-error";
        } catch (IllegalArgumentException | NullPointerException e) {
            logger.warn("Invalid input in getProfile: {}", e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
            return "admin/errors/500-error";
        } catch (Exception e) {
            logger.error("Unexpected error in getProfile: {}", e.getMessage(), e);
            model.addAttribute("errorMessage", "Đã xảy ra lỗi");
            return "admin/errors/500-error";
        }

        return "/admin/auth/profile";
    }

    /**
     * get form update profile
     * 
     * @param model, id
     * @return String
     * @throws
     */
    @GetMapping("update-profile/{id}")
    public String getUpdateProfileForm(Model model, @PathVariable("id") Long id) {
        try {
            EditEmployeeDto employee = employeeService.getEmployeeById(id);
            UpdateProfileDto updateProfileDto = new UpdateProfileDto(id, employee.getUsername(), employee.getFullname(),
                    employee.getPhoneNumber(), employee.getAddress());
            model.addAttribute("employee", updateProfileDto);
            return "/admin/auth/update-profile";
        } catch (EntityNotFoundException e) {
            logger.warn("Entity not found in getUpdateProfileForm id={}: {}", id, e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
            return "admin/errors/500-error";
        } catch (DataAccessException e) {
            logger.error("Data access error in getUpdateProfileForm id={}: {}", id, e.getMessage(), e);
            model.addAttribute("errorMessage", "Lỗi truy cập dữ liệu");
            return "admin/errors/500-error";
        } catch (Exception e) {
            logger.error("Unexpected error in getUpdateProfileForm id={}: {}", id, e.getMessage(), e);
            model.addAttribute("errorMessage", "Đã xảy ra lỗi");
            return "admin/errors/500-error";
        }
    }

    /**
     * post update profile
     * 
     * @param model, updateProfileDto, bindingResult, file
     * @return String
     * @throws
     */
    @PostMapping("update-profile")
    public String UpdateProfile(Model model, @Valid @ModelAttribute("employee") UpdateProfileDto updateProfileDto,
            BindingResult bindingResult,
            @RequestParam("file") MultipartFile file) {
        try {
            if (bindingResult.hasErrors()) {
                return "/admin/auth/update-profile";
            }

            Employee updatedEmployee = employeeService.updateProfileService(updateProfileDto, file);

            UserDetails userDetails = customUserDetailsService.loadUserByUsername(updatedEmployee.getUsername());

            UsernamePasswordAuthenticationToken newAuth = new UsernamePasswordAuthenticationToken(userDetails, null,
                    userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(newAuth);

            return "redirect:/get-profile";
        } catch (EntityNotFoundException e) {
            logger.warn("Entity not found in UpdateProfile: {}", e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
            return "admin/errors/500-error";
        } catch (DataAccessException e) {
            logger.error("Data access error in UpdateProfile: {}", e.getMessage(), e);
            model.addAttribute("errorMessage", "Lỗi truy cập dữ liệu");
            return "admin/errors/500-error";
        } catch (UsernameNotFoundException e) {
            logger.warn("User not found in UpdateProfile: {}", e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
            return "admin/errors/500-error";
        } catch (IllegalArgumentException | NullPointerException e) {
            logger.warn("Invalid input in UpdateProfile: {}", e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
            return "admin/errors/500-error";
        } catch (Exception e) {
            logger.error("Unexpected error in UpdateProfile: {}", e.getMessage(), e);
            model.addAttribute("errorMessage", "Đã xảy ra lỗi");
            return "admin/errors/500-error";
        }

    }

    /**
     * get form change password
     * 
     * @param model
     * @return String
     * @throws
     */
    @GetMapping("change-password")
    public String getChangePassword(Model model) {
        try {
            model.addAttribute("employee", new ChangePasswordDto());
            return "admin/auth/change-password";
        } catch (Exception e) {
            logger.error("Unexpected error in getChangePassword: {}", e.getMessage(), e);
            model.addAttribute("errorMessage", "Đã xảy ra lỗi");
            return "admin/errors/500-error";
        }
    }

    /**
     * post change password
     * 
     * @param model, authentication, changePasswordDto, bindingResult
     * @return String
     * @throws
     */
    @PostMapping("change-password")
    public String ChangePassword(Model model, Authentication authentication,
            @Valid @ModelAttribute("employee") ChangePasswordDto changePasswordDto, BindingResult bindingResult) {
        try {
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
        } catch (EntityNotFoundException e) {
            logger.warn("Entity not found in ChangePassword: {}", e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
            return "admin/errors/500-error";
        } catch (DataAccessException e) {
            logger.error("Data access error in ChangePassword: {}", e.getMessage(), e);
            model.addAttribute("errorMessage", "Lỗi truy cập dữ liệu");
            return "admin/errors/500-error";
        } catch (IllegalArgumentException | NullPointerException e) {
            logger.warn("Invalid input in ChangePassword: {}", e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
            return "admin/errors/500-error";
        } catch (Exception e) {
            logger.error("Unexpected error in ChangePassword: {}", e.getMessage(), e);
            model.addAttribute("errorMessage", "Đã xảy ra lỗi");
            return "admin/errors/500-error";
        }
    }
}
