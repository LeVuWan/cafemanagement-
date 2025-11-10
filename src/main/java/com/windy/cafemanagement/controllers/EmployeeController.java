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
        model.addAttribute("keyword", keyword);
        model.addAttribute("employeis", employeeService.getAllEmployeesService(keyword));
        return "/admin/employee/list-employee";
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
        model.addAttribute("permissions", permissionService.getAllPermissionsService());
        model.addAttribute("employee", new CreateEmployeeDto());
        return "/admin/employee/create-employee";
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
        model.addAttribute("employee", employeeService.getEmployeeById(id));
        model.addAttribute("permissions", permissionService.getAllPermissionsService());
        return "/admin/employee/edit-employee";
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
        employeeService.editEmployee(editEmployeeDto);
        return "redirect:/admin/employee";
    }

    /**
     * delete employee by id
     * 
     * @param id
     * @return String
     * @throws 
     */
    @GetMapping("/delete/{id}")
    public String deleteEmployeeController(@PathVariable("id") Long id) {
        employeeService.deleteEmployeeService(id);
        return "redirect:/admin/employee";
    }

}
