package com.windy.cafemanagement.controllers;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.windy.cafemanagement.Services.VoucherService;
import com.windy.cafemanagement.dto.CreateVoucherDto;
import com.windy.cafemanagement.models.Voucher;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequestMapping("/admin/voucher")
/**
 * VoucherController
 *
 * Version 1.0
 *
 * Date: 12-11-2013
 *
 * Copyright
 *
 * Modification Logs:
 * DATE AUTHOR DESCRIPTION
 * -----------------------------------------------------------------------
 */
public class VoucherController {
    private static final Logger logger = LoggerFactory.getLogger(VoucherController.class);

    private final VoucherService voucherService;

    public VoucherController(VoucherService voucherService) {
        this.voucherService = voucherService;
    }

    /**
     * Show voucher list page.
     * 
     * @param model the model for the view
     * @return view name for voucher list
     * 
     */
    @GetMapping("")
    public String getListVoucherController(Model model) {
        try {
            model.addAttribute("vouchers", voucherService.getAllVouchersService());
            return "admin/voucher/list-voucher";
        } catch (Exception ex) {
            logger.error("Unexpected error while listing vouchers: {}", ex.getMessage(), ex);
            model.addAttribute("errorMessage", "Lấy voucher thất bại: " + ex.getMessage());
            return "admin/errors/500-error";
        }
    }

    /**
     * Show create voucher form.
     * 
     * @param model the model for the view
     * @return view name for create voucher
     * 
     */
    @GetMapping("create")
    public String getFormCreateController(Model model) {
        try {
            model.addAttribute("voucher", new CreateVoucherDto());
            return "admin/voucher/create-voucher";
        } catch (Exception ex) {
            logger.error("Unexpected error while opening create voucher form: {}", ex.getMessage(), ex);
            model.addAttribute("errorMessage", "Không thể mở form tạo voucher: " + ex.getMessage());
            return "admin/errors/500-error";
        }
    }

    /**
     * Handle voucher creation.
     * 
     * @param createVoucherDto the voucher DTO
     * @param bindingResult    validation result
     * @param model            the model for the view
     * @return redirect on success or the create view on validation/error
     * 
     */
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

        try {
            voucherService.saveVoucherService(createVoucherDto);
            return "redirect:/admin/voucher";
        } catch (IllegalArgumentException | NullPointerException ex) {
            logger.error("Invalid voucher data: {}", ex.getMessage(), ex);
            model.addAttribute("errorMessage", "Dữ liệu voucher không hợp lệ: " + ex.getMessage());
            return "admin/errors/500-error";
        } catch (Exception ex) {
            logger.error("Unexpected error while creating voucher: {}", ex.getMessage(), ex);
            model.addAttribute("errorMessage", "Tạo voucher thất bại: " + ex.getMessage());
            return "admin/errors/500-error";
        }
    }

    /**
     * Show edit voucher form.
     * 
     * @param model the model for the view
     * @param id    the voucher id
     * @return view name for edit voucher
     * 
     */
    @GetMapping("edit/{id}")
    public String getFormEditController(Model model, @PathVariable("id") Long id) {
        try {
            Voucher voucher = voucherService.findVoucherById(id);
            logger.debug("Check {}", voucher);
            model.addAttribute("voucher", voucher);
            return "/admin/voucher/edit-voucher";
        } catch (EntityNotFoundException ex) {
            logger.error("Voucher not found for id {}: {}", id, ex.getMessage(), ex);
            model.addAttribute("errorMessage", "Không tìm thấy voucher: " + ex.getMessage());
            return "admin/errors/500-error";
        } catch (Exception ex) {
            logger.error("Unexpected error while loading voucher for edit id {}: {}", id, ex.getMessage(), ex);
            model.addAttribute("errorMessage", "Không thể mở form chỉnh sửa voucher: " + ex.getMessage());
            return "admin/errors/500-error";
        }
    }

    /**
     * Update an existing voucher.
     * 
     * @param id             the voucher id
     * @param updatedVoucher the updated voucher entity
     * @return redirect to voucher list
     * 
     */
    @PostMapping("update/{id}")
    public String updateVoucherController(@PathVariable("id") Long id,
            @ModelAttribute("voucher") Voucher updatedVoucher) {
        try {
            voucherService.updateVoucher(id, updatedVoucher);
            return "redirect:/admin/voucher";
        } catch (EntityNotFoundException ex) {
            logger.error("Voucher not found while updating id {}: {}", id, ex.getMessage(), ex);
            return "admin/errors/500-error";
        } catch (Exception ex) {
            logger.error("Unexpected error while updating voucher id {}: {}", id, ex.getMessage(), ex);
            return "admin/errors/500-error";
        }
    }

    /**
     * Delete a voucher by id.
     * 
     * @param id the voucher id
     * @return redirect to voucher list or error view
     * 
     */
    @GetMapping("/delete/{id}")
    public String deleteEmployeeController(@PathVariable("id") Long id) {
        try {
            voucherService.deleteVoucherService(id);
            return "redirect:/admin/voucher";
        } catch (EntityNotFoundException ex) {
            logger.error("Voucher not found for delete id {}: {}", id, ex.getMessage(), ex);
            return "admin/errors/500-error";
        } catch (Exception ex) {
            logger.error("Unexpected error while deleting voucher id {}: {}", id, ex.getMessage(), ex);
            return "admin/errors/500-error";
        }
    }

    /**
     * Get vouchers within the valid time window.
     * 
     * @return ResponseEntity with the list of vouchers
     * 
     */
    @GetMapping("/get-voucher-by-date")
    @ResponseBody
    public ResponseEntity<?> getVoucherByTime() {
        try {
            List<Voucher> vouchers = voucherService.getVoucherByTimeService();

            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "data", vouchers,
                    "message", "Lấy voucher thành công"));
        } catch (Exception ex) {
            logger.error("Unexpected error while fetching vouchers by date: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "error",
                    "message", "Lấy voucher thất bại: " + ex.getMessage()));
        }
    }

    /**
     * Get a voucher by id.
     * 
     * @param id the voucher id
     * @return ResponseEntity with the voucher
     * 
     */
    @GetMapping("/get-voucher-by-id/{id}")
    @ResponseBody
    public ResponseEntity<?> getVoucherById(@PathVariable("id") Long id) {
        try {
            Voucher voucher = voucherService.findVoucherById(id);

            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "data", voucher,
                    "message", "Lấy voucher thành công"));
        } catch (EntityNotFoundException ex) {
            logger.error("Voucher not found for id {}: {}", id, ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "status", "error",
                    "message", "Không tìm thấy voucher: " + ex.getMessage()));
        } catch (Exception ex) {
            logger.error("Unexpected error while fetching voucher id {}: {}", id, ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "error",
                    "message", "Lấy voucher thất bại: " + ex.getMessage()));
        }
    }

}
