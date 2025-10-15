package com.windy.cafemanagement.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.windy.cafemanagement.Services.VoucherService;
import com.windy.cafemanagement.dto.CreateVoucherDto;
import com.windy.cafemanagement.models.Voucher;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequestMapping("/admin/voucher")
public class VoucherController {
    private final VoucherService voucherService;

    public VoucherController(VoucherService voucherService) {
        this.voucherService = voucherService;
    }

    @GetMapping("")
    public String getListVoucherController(Model model) {
        model.addAttribute("vouchers", voucherService.getAllVouchersService());
        return "admin/voucher/list-voucher";
    }

    @GetMapping("create")
    public String getFormCreateController(Model model) {
        model.addAttribute("voucher", new CreateVoucherDto());
        return "admin/voucher/create-voucher";
    }

    @PostMapping("create")
    public String createVoucherController(@Valid @ModelAttribute("voucher") CreateVoucherDto createVoucherDto,
            BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            return "/admin/voucher/create-voucher";
        }

        if (!createVoucherDto.getStartDate().isBefore(createVoucherDto.getEndDate())) {
            model.addAttribute("errorDate", "Ngày bắt đầu và kết thúc không hợp lệ");
            return "/admin/voucher/create-voucher";
        }

        voucherService.saveVoucherService(createVoucherDto);

        return "redirect:/admin/voucher";
    }

    @GetMapping("edit/{id}")
    public String getFormEditController(Model model, @PathVariable("id") Long id) {
        Voucher voucher = voucherService.findVoucherById(id);
        System.out.println("Check " + voucher.toString());
        model.addAttribute("voucher", voucher);
        return "/admin/voucher/edit-voucher";
    }

    @PostMapping("update/{id}")
    public String updateVoucherController(@PathVariable("id") Long id,
            @ModelAttribute("voucher") Voucher updatedVoucher) {
        voucherService.updateVoucher(id, updatedVoucher);
        return "redirect:/admin/voucher";
    }

    @GetMapping("/delete/{id}")
    public String deleteEmployeeController(@PathVariable("id") Long id) {
        voucherService.deleteVoucherService(id);
        return "redirect:/admin/voucher";
    }

}
