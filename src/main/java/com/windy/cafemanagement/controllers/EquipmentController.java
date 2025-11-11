package com.windy.cafemanagement.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.windy.cafemanagement.Services.EquipmentService;
import com.windy.cafemanagement.dto.EquipmentDto;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataAccessException;

/**
 * Equipment Controller
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
 * 11-10-2025 VuLQ Create EquipmentController
 */
@Controller
@RequestMapping("admin/equipment")
public class EquipmentController {
    private final EquipmentService equipmentService;
    private final Logger logger = LoggerFactory.getLogger(EquipmentController.class);

    public EquipmentController(EquipmentService equipmentService) {
        this.equipmentService = equipmentService;
    }

    /**
     * get list equipment
     * 
     * @param model
     * @return String
     * @throws
     */
    @GetMapping("")
    public String getListEquipment(Model model) {
        try {
            model.addAttribute("equipments", equipmentService.getAllEquipmentsService());
            return "admin/equipment/list-equipment";
        } catch (EntityNotFoundException e) {
            logger.warn("Entity not found in getListEquipment: {}", e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
            return "admin/errors/500-error";
        } catch (DataAccessException e) {
            logger.error("Data access error in getListEquipment: {}", e.getMessage(), e);
            model.addAttribute("errorMessage", "Lỗi truy cập dữ liệu");
            return "admin/errors/500-error";
        } catch (IllegalArgumentException | NullPointerException e) {
            logger.warn("Invalid input in getListEquipment: {}", e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
            return "admin/errors/500-error";
        } catch (Exception e) {
            logger.error("Unexpected error in getListEquipment: {}", e.getMessage(), e);
            model.addAttribute("errorMessage", "Đã xảy ra lỗi");
            return "admin/errors/500-error";
        }
    }

    /**
     * get form create equipment
     * 
     * @param model
     * @return String
     * @throws
     */
    @GetMapping("create")
    public String getFormCreateEquipment(Model model) {
        try {
            model.addAttribute("equipment", new EquipmentDto());
            return "admin/equipment/create-equipment";
        } catch (Exception e) {
            logger.error("Unexpected error in getFormCreateEquipment: {}", e.getMessage(), e);
            model.addAttribute("errorMessage", "Đã xảy ra lỗi");
            return "admin/errors/500-error";
        }
    }

    /**
     * create equipment
     * 
     * @param equipmentDto, result, model
     * @return String
     * @throws
     */
    @PostMapping("create")
    public String createEquipment(@Valid @ModelAttribute("equipment") EquipmentDto equipmentDto, BindingResult result,
            Model model) {
        try {
            if (result.hasErrors()) {
                model.addAttribute("equipment", equipmentDto);
                return "admin/equipment/create-equipment";
            }
            equipmentService.createEquipmentWithImportOrder(equipmentDto);
            return "redirect:/admin/equipment";
        } catch (EntityNotFoundException e) {
            logger.warn("Entity not found in createEquipment: {}", e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
            return "admin/errors/500-error";
        } catch (DataAccessException e) {
            logger.error("Data access error in createEquipment: {}", e.getMessage(), e);
            model.addAttribute("errorMessage", "Lỗi truy cập dữ liệu");
            return "admin/errors/500-error";
        } catch (IllegalArgumentException | NullPointerException e) {
            logger.warn("Invalid input in createEquipment: {}", e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
            return "admin/errors/500-error";
        } catch (Exception e) {
            logger.error("Unexpected error in createEquipment: {}", e.getMessage(), e);
            model.addAttribute("errorMessage", "Đã xảy ra lỗi");
            return "admin/errors/500-error";
        }
    }

    /**
     * get form edit equipment
     * 
     * @param equipmentId, model
     * @return String
     * @throws
     */
    @GetMapping("edit/{id}")
    public String getFormEditEquipment(@PathVariable("id") Long equipmentId, Model model) {
        try {
            model.addAttribute("equipment", equipmentService.getEquipmentById(equipmentId));
            return "admin/equipment/edit-equipment";
        } catch (EntityNotFoundException e) {
            logger.warn("Entity not found in getFormEditEquipment id={}: {}", equipmentId, e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
            return "admin/errors/500-error";
        } catch (DataAccessException e) {
            logger.error("Data access error in getFormEditEquipment id={}: {}", equipmentId, e.getMessage(), e);
            model.addAttribute("errorMessage", "Lỗi truy cập dữ liệu");
            return "admin/errors/500-error";
        } catch (Exception e) {
            logger.error("Unexpected error in getFormEditEquipment id={}: {}", equipmentId, e.getMessage(), e);
            model.addAttribute("errorMessage", "Đã xảy ra lỗi");
            return "admin/errors/500-error";
        }
    }

    /**
     * edit equipment
     * 
     * @param equipmentDto, result, model
     * @return String
     * @throws
     */
    @PostMapping("/edit")
    public String EditEquipment(@Valid @ModelAttribute("equipment") EquipmentDto equipmentDto, BindingResult result,
            Model model) {
        try {
            if (result.hasErrors()) {
                model.addAttribute("equipment", equipmentDto);
                return "admin/equipment/edit-equipment";
            }

            equipmentService.updateEquipmentWithImportOrder(equipmentDto);
            return "redirect:/admin/equipment";
        } catch (EntityNotFoundException e) {
            logger.warn("Entity not found in EditEquipment: {}", e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
            return "admin/errors/500-error";
        } catch (DataAccessException e) {
            logger.error("Data access error in EditEquipment: {}", e.getMessage(), e);
            model.addAttribute("errorMessage", "Lỗi truy cập dữ liệu");
            return "admin/errors/500-error";
        } catch (IllegalArgumentException | NullPointerException e) {
            logger.warn("Invalid input in EditEquipment: {}", e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
            return "admin/errors/500-error";
        } catch (Exception e) {
            logger.error("Unexpected error in EditEquipment: {}", e.getMessage(), e);
            model.addAttribute("errorMessage", "Đã xảy ra lỗi");
            return "admin/errors/500-error";
        }
    }

    /**
     * delete equipment by id
     * 
     * @param equipmentId, model
     * @return String
     * @throws
     */
    @GetMapping("/delete/{id}")
    public String deleteEquipment(@PathVariable("id") Long equipmentId, Model model) {
        try {
            equipmentService.softDeleteEquipment(equipmentId);
            return "redirect:/admin/equipment";
        } catch (EntityNotFoundException e) {
            logger.warn("Entity not found in deleteEquipment id={}: {}", equipmentId, e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
            return "admin/errors/500-error";
        } catch (DataAccessException e) {
            logger.error("Data access error in deleteEquipment id={}: {}", equipmentId, e.getMessage(), e);
            model.addAttribute("errorMessage", "Lỗi truy cập dữ liệu");
            return "admin/errors/500-error";
        } catch (Exception e) {
            logger.error("Unexpected error in deleteEquipment id={}: {}", equipmentId, e.getMessage(), e);
            model.addAttribute("errorMessage", "Đã xảy ra lỗi");
            return "admin/errors/500-error";
        }
    }
}
