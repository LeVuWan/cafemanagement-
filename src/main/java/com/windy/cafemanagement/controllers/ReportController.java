package com.windy.cafemanagement.controllers;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.dao.DataAccessException;
import jakarta.persistence.EntityNotFoundException;

import com.windy.cafemanagement.Responses.EmployeeInfoRes;
import com.windy.cafemanagement.Responses.GenaralReportRes;
import com.windy.cafemanagement.Responses.ImportByDateReportRes;
import com.windy.cafemanagement.Responses.ImportExportRes;
import com.windy.cafemanagement.Responses.TotalInvoiceByDateRes;
import com.windy.cafemanagement.Services.EmployeeService;
import com.windy.cafemanagement.Services.ReportService;

@Controller
@RequestMapping("/admin/report")
/**
 * ReportController
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
public class ReportController {
    private static final Logger logger = LoggerFactory.getLogger(ReportController.class);

    private final ReportService reportService;
    private final EmployeeService employeeService;

    public ReportController(ReportService reportService, EmployeeService employeeService) {
        this.reportService = reportService;
        this.employeeService = employeeService;
    }

    @GetMapping("")
    public String getReportPage() {
        return "admin/report/general-report";
    }

    /**
     * Return general report between two dates.
     * 
     * @param from start date (inclusive)
     * @param to   end date (inclusive)
     * @return ResponseEntity with report data or error
     */
    @GetMapping("/general")
    @ResponseBody
    public ResponseEntity<?> generalReport(
            @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        try {
            List<GenaralReportRes> report = reportService.generalReportService(from, to);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "data", report,
                    "message", "Lấy báo cáo thành công"));
        } catch (EntityNotFoundException ex) {
            logger.error("General report not found for {} - {}: {}", from, to, ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "status", "error",
                    "message", "Không tìm thấy báo cáo: " + ex.getMessage()));
        } catch (IllegalArgumentException | NullPointerException ex) {
            logger.error("Invalid arguments for general report: {} - {}: {}", from, to, ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "status", "error",
                    "message", "Dữ liệu không hợp lệ: " + ex.getMessage()));
        } catch (Exception ex) {
            logger.error("Unexpected error while generating general report: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "error",
                    "message", "Lấy báo cáo thất bại: " + ex.getMessage()));
        }
    }

    /**
     * Return import-export summary between dates.
     * 
     * @param from start date
     * @param to   end date
     * @return ResponseEntity with report data or error
     */
    @GetMapping("/input-output")
    @ResponseBody
    public ResponseEntity<?> inputOutputReport(
            @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        try {
            List<ImportExportRes> report = reportService.importExportReportService(from, to);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "data", report,
                    "message", "Lấy báo cáo thành công"));
        } catch (EntityNotFoundException ex) {
            logger.error("Import-export report not found for {} - {}: {}", from, to, ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "status", "error",
                    "message", "Không tìm thấy báo cáo: " + ex.getMessage()));
        } catch (IllegalArgumentException | NullPointerException ex) {
            logger.error("Invalid arguments for import-export report: {} - {}: {}", from, to, ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "status", "error",
                    "message", "Dữ liệu không hợp lệ: " + ex.getMessage()));
        } catch (Exception ex) {
            logger.error("Unexpected error while generating import-export report: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "error",
                    "message", "Lấy báo cáo thất bại: " + ex.getMessage()));
        }
    }

    /**
     * Return import orders between dates.
     * 
     * @param from start date
     * @param to   end date
     * @return ResponseEntity with import orders or error
     */
    @GetMapping("/input")
    @ResponseBody
    public ResponseEntity<?> importByDateReport(
            @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        try {
            List<ImportByDateReportRes> report = reportService.getImportOderByDateService(from, to);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "data", report,
                    "message", "Lấy báo cáo thành công"));
        } catch (EntityNotFoundException ex) {
            logger.error("Import orders not found for {} - {}: {}", from, to, ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "status", "error",
                    "message", "Không tìm thấy dữ liệu: " + ex.getMessage()));
        } catch (DataAccessException ex) {
            logger.error("Database error while getting import orders: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "error",
                    "message", "Lỗi cơ sở dữ liệu khi lấy báo cáo: " + ex.getMessage()));
        } catch (IllegalArgumentException | NullPointerException ex) {
            logger.error("Invalid arguments for import orders: {} - {}: {}", from, to, ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "status", "error",
                    "message", "Dữ liệu không hợp lệ: " + ex.getMessage()));
        } catch (Exception ex) {
            logger.error("Unexpected error while getting import orders: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "error",
                    "message", "Lấy báo cáo thất bại: " + ex.getMessage()));
        }
    }

    /**
     * Return export orders between dates.
     * 
     * @param from start date
     * @param to   end date
     * @return ResponseEntity with export orders or error
     */
    @GetMapping("/export")
    @ResponseBody
    public ResponseEntity<?> exportByDateReport(
            @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        try {
            List<ImportByDateReportRes> report = reportService.getImportOderByDateService(from, to);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "data", report,
                    "message", "Lấy báo cáo thành công"));
        } catch (EntityNotFoundException ex) {
            logger.error("Export orders not found for {} - {}: {}", from, to, ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "status", "error",
                    "message", "Không tìm thấy dữ liệu: " + ex.getMessage()));
        } catch (IllegalArgumentException | NullPointerException ex) {
            logger.error("Invalid arguments for export orders: {} - {}: {}", from, to, ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "status", "error",
                    "message", "Dữ liệu không hợp lệ: " + ex.getMessage()));
        } catch (Exception ex) {
            logger.error("Unexpected error while getting export orders: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "error",
                    "message", "Lấy báo cáo thất bại: " + ex.getMessage()));
        }
    }

    /**
     * Return invoice totals between dates.
     * 
     * @param from start date
     * @param to   end date
     * @return ResponseEntity with invoice totals or error
     */
    @GetMapping("/sell")
    @ResponseBody
    public ResponseEntity<?> invoiceByDateReport(
            @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        try {
            List<TotalInvoiceByDateRes> report = reportService.getInvoiceTotalByDateService(from, to);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "data", report,
                    "message", "Lấy báo cáo thành công"));
        } catch (EntityNotFoundException ex) {
            logger.error("Invoice totals not found for {} - {}: {}", from, to, ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "status", "error",
                    "message", "Không tìm thấy dữ liệu hóa đơn: " + ex.getMessage()));

        } catch (IllegalArgumentException | NullPointerException ex) {
            logger.error("Invalid arguments for invoice totals: {} - {}: {}", from, to, ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "status", "error",
                    "message", "Dữ liệu không hợp lệ: " + ex.getMessage()));
        } catch (Exception ex) {
            logger.error("Unexpected error while getting invoice totals: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "error",
                    "message", "Lấy báo cáo thất bại: " + ex.getMessage()));
        }
    }

    /**
     * Return employee information for report.
     * 
     * @return ResponseEntity with employee info or error
     */
    @GetMapping("/information-employy")
    @ResponseBody
    public ResponseEntity<?> getInformationEmployyController() {
        try {
            List<EmployeeInfoRes> res = employeeService.getEmployeeInformationService();
            return ResponseEntity.ok(Map.of("status", "success", "data", res, "message", "Lấy report thành công"));
        } catch (EntityNotFoundException ex) {
            logger.error("Employee information not found: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("status", "error", "message",
                            "Không tìm thấy thông tin nhân viên: " + ex.getMessage()));
        } catch (Exception ex) {
            logger.error("Unexpected error while getting employee information: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("status", "error", "message", "Lấy báo cáo thất bại: " + ex.getMessage()));
        }
    }

    /**
     * Return total expenses between dates.
     * 
     * @param from start date
     * @param to   end date
     * @return ResponseEntity with expense totals or error
     */
    @GetMapping("/expense")
    @ResponseBody
    public ResponseEntity<?> getTotalExpenseByDateReport(
            @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        try {
            List<TotalInvoiceByDateRes> report = reportService.getTotalEnpenseByDateService(from, to);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "data", report,
                    "message", "Lấy báo cáo thành công"));
        } catch (EntityNotFoundException ex) {
            logger.error("Expense totals not found for {} - {}: {}", from, to, ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "status", "error",
                    "message", "Không tìm thấy dữ liệu chi tiêu: " + ex.getMessage()));
        } catch (IllegalArgumentException | NullPointerException ex) {
            logger.error("Invalid arguments for expense totals: {} - {}: {}", from, to, ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "status", "error",
                    "message", "Dữ liệu không hợp lệ: " + ex.getMessage()));
        } catch (Exception ex) {
            logger.error("Unexpected error while getting expense totals: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "error",
                    "message", "Lấy báo cáo thất bại: " + ex.getMessage()));
        }
    }

}
