package com.windy.cafemanagement.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.windy.cafemanagement.Services.EquipmentService;
import com.windy.cafemanagement.dto.EquipmentDto;

import jakarta.validation.Valid;

@Controller
@RequestMapping("admin/equipment")
public class EquipmentController {
    private final EquipmentService equipmentService;

    public EquipmentController(EquipmentService equipmentService) {
        this.equipmentService = equipmentService;
    }

    @GetMapping("")
    public String getListEquipment(Model model) {
        model.addAttribute("equipments", equipmentService.getAllEquipmentsService());
        return "admin/equipment/list-equipment";
    }

    @GetMapping("create")
    public String getFormCreateEquipment(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("==> Current User: " + authentication.getName());
        System.out.println("==> Roles: " + authentication.getAuthorities());
        model.addAttribute("equipment", new EquipmentDto());
        return "admin/equipment/create-equipment";
    }

    @PostMapping("create")
    public String createEquipment(@Valid @ModelAttribute("equipment") EquipmentDto equipmentDto, BindingResult result,
            Model model) {
        if (result.hasErrors()) {
            model.addAttribute("club", equipmentDto);
            return "admin/equipment/create-equipment";
        }
        equipmentService.createEquipmentWithImportOrder(equipmentDto);
        return "redirect:/admin/equipment";
    }
}
