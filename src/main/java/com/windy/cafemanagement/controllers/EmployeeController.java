package com.windy.cafemanagement.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.windy.cafemanagement.Services.EmployeeService;
import com.windy.cafemanagement.Services.PermissionService;
import com.windy.cafemanagement.Services.UploadService;
import com.windy.cafemanagement.dto.CreateEmployeeDto;
import com.windy.cafemanagement.dto.EditEmployeeDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataAccessException;

/**
 * Employee Controller
 *
 * Version 1.0
 *
 * Date: 10-11-2025
 *
 * Copyright
 *
 * Modification Logs:
 * DATE AUTHOR DESCRIPTION
 * -----------------------------------------------------------------------
 * 10-11-2025 VuLQ Employee Controller
 */

@Controller
@RequestMapping("/admin/employee")
public class EmployeeController {
    private final Logger logger = LoggerFactory.getLogger(EmployeeController.class);
    private final EmployeeService employeeService;
    private final PermissionService permissionService;
    private final UploadService uploadService;

    public EmployeeController(EmployeeService employeeService, PermissionService permissionService,
            UploadService uploadService) {
        this.employeeService = employeeService;
        this.uploadService = uploadService;
        this.permissionService = permissionService;
    }

    /**
     * get list employee by keyword
     * 
     * @param model, keyword
     * @return String
     * @throws 
     */
    @GetMapping("")
    public String getTableUserController(Model model,
            @RequestParam(value = "keyword", required = false) String keyword) {
        try {
            model.addAttribute("keyword", keyword);
            model.addAttribute("employeis", employeeService.getAllEmployeesService(keyword));
            return "/admin/employee/list-employee";
        } catch (EntityNotFoundException e) {
            logger.warn("Entity not found in getTableUserController: {}", e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
            return "admin/errors/500-error";
        } catch (DataAccessException e) {
            logger.error("Data access error in getTableUserController: {}", e.getMessage(), e);
            model.addAttribute("errorMessage", "Lỗi truy cập dữ liệu");
            return "admin/errors/500-error";
        } catch (IllegalArgumentException | NullPointerException e) {
            logger.warn("Invalid input in getTableUserController: {}", e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
            return "admin/errors/500-error";
        } catch (Exception e) {
            logger.error("Unexpected error in getTableUserController: {}", e.getMessage(), e);
            model.addAttribute("errorMessage", "Đã xảy ra lỗi");
            return "admin/errors/500-error";
        }
    }

    /**
     * get form create employee
     * 
     * @param model
     * @return String
     * @throws 
     */
    @GetMapping("/create")
    public String getFormCreateController(Model model) {
        try {
            model.addAttribute("permissions", permissionService.getAllPermissionsService());
            model.addAttribute("employee", new CreateEmployeeDto());
            return "/admin/employee/create-employee";
        } catch (DataAccessException e) {
            logger.error("Data access error in getFormCreateController: {}", e.getMessage(), e);
            model.addAttribute("errorMessage", "Lỗi truy cập dữ liệu");
            return "admin/errors/500-error";
        } catch (Exception e) {
            logger.error("Unexpected error in getFormCreateController: {}", e.getMessage(), e);
            model.addAttribute("errorMessage", "Đã xảy ra lỗi");
            return "admin/errors/500-error";
        }
    }

    /**
     * create new employee
     * 
     * @param createEmployeeDto, bindingResult, file, model
     * @return String
     * @throws 
     */
    @PostMapping("/create")
    public String createEmployeeController(
            @Valid @ModelAttribute("employee") CreateEmployeeDto createEmployeeDto,
            BindingResult bindingResult,
            @RequestParam("file") MultipartFile file,
            Model model) {
        try {
            if (bindingResult.hasErrors()) {
                model.addAttribute("permissions", permissionService.getAllPermissionsService());
                return "/admin/employee/create-employee";
            }

            String imgUrl = uploadService.uploadImage(file, "avatar");
            if (imgUrl == null) {
                imgUrl = "/assets/img/avatar/default.jpg";
            }
            employeeService.createNewAEmployee(createEmployeeDto, imgUrl);
            return "redirect:/admin/employee";
        } catch (EntityNotFoundException e) {
            logger.warn("Entity not found in createEmployeeController: {}", e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
            return "admin/errors/500-error";
        } catch (DataAccessException e) {
            logger.error("Data access error in createEmployeeController: {}", e.getMessage(), e);
            model.addAttribute("errorMessage", "Lỗi truy cập dữ liệu");
            return "admin/errors/500-error";
        } catch (IllegalArgumentException | NullPointerException e) {
            logger.warn("Invalid input in createEmployeeController: {}", e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
            return "admin/errors/500-error";
        } catch (Exception e) {
            logger.error("Unexpected error in createEmployeeController: {}", e.getMessage(), e);
            model.addAttribute("errorMessage", "Đã xảy ra lỗi");
            return "admin/errors/500-error";
        }
    }

    /**
     * get form edit employee
     * 
     * @param model, id
     * @return String
     * @throws 
     */
    @GetMapping("/edit/{id}")
    public String getEditFormController(Model model, @PathVariable("id") Long id) {
        try {
            model.addAttribute("employee", employeeService.getEmployeeById(id));
            model.addAttribute("permissions", permissionService.getAllPermissionsService());
            return "/admin/employee/edit-employee";
        } catch (EntityNotFoundException e) {
            logger.warn("Entity not found in getEditFormController id={}: {}", id, e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
            return "admin/errors/500-error";
        } catch (DataAccessException e) {
            logger.error("Data access error in getEditFormController id={}: {}", id, e.getMessage(), e);
            model.addAttribute("errorMessage", "Lỗi truy cập dữ liệu");
            return "admin/errors/500-error";
        } catch (Exception e) {
            logger.error("Unexpected error in getEditFormController id={}: {}", id, e.getMessage(), e);
            model.addAttribute("errorMessage", "Đã xảy ra lỗi");
            return "admin/errors/500-error";
        }
    }

    /**
     * edit employee
     * 
     * @param editEmployeeDto, file, model
     * @return String
     * @throws 
     */
    @PostMapping("/edit")
    public String editEmployeeController(@ModelAttribute("employee") EditEmployeeDto editEmployeeDto,
            @RequestParam(value = "file", required = false) MultipartFile file,
            Model model) {
        try {
            employeeService.editEmployee(editEmployeeDto);
            return "redirect:/admin/employee";
        } catch (EntityNotFoundException e) {
            logger.warn("Entity not found in editEmployeeController: {}", e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
            return "admin/errors/500-error";
        } catch (DataAccessException e) {
            logger.error("Data access error in editEmployeeController: {}", e.getMessage(), e);
            model.addAttribute("errorMessage", "Lỗi truy cập dữ liệu");
            return "admin/errors/500-error";
        } catch (IllegalArgumentException | NullPointerException e) {
            logger.warn("Invalid input in editEmployeeController: {}", e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
            return "admin/errors/500-error";
        } catch (Exception e) {
            logger.error("Unexpected error in editEmployeeController: {}", e.getMessage(), e);
            model.addAttribute("errorMessage", "Đã xảy ra lỗi");
            return "admin/errors/500-error";
        }
    }

    /**
     * delete employee by id
     * 
     * @param id
     * @return String
     * @throws 
     */
    @GetMapping("/delete/{id}")
    public String deleteEmployeeController(@PathVariable("id") Long id, Model model) {
        try {
            employeeService.deleteEmployeeService(id);
            return "redirect:/admin/employee";
        } catch (EntityNotFoundException e) {
            logger.warn("Entity not found in deleteEmployeeController id={}: {}", id, e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
            return "admin/errors/500-error";
        } catch (DataAccessException e) {
            logger.error("Data access error in deleteEmployeeController id={}: {}", id, e.getMessage(), e);
            model.addAttribute("errorMessage", "Lỗi truy cập dữ liệu");
            return "admin/errors/500-error";
        } catch (Exception e) {
            logger.error("Unexpected error in deleteEmployeeController id={}: {}", id, e.getMessage(), e);
            model.addAttribute("errorMessage", "Đã xảy ra lỗi");
            return "admin/errors/500-error";
        }
    }

}
