package com.windy.cafemanagement.controllers;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.windy.cafemanagement.Responses.EmployeeInfoRes;
import com.windy.cafemanagement.Responses.GenaralReportRes;
import com.windy.cafemanagement.Responses.ImportByDateReportRes;
import com.windy.cafemanagement.Responses.ImportExportRes;
import com.windy.cafemanagement.Responses.TotalInvoiceByDateRes;
import com.windy.cafemanagement.Services.EmployeeService;
import com.windy.cafemanagement.Services.ReportService;

@Controller
@RequestMapping("/admin/report")
public class ReportController {
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
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "status", "error",
                    "message", "Lấy báo cáo thất bại: " + e.getMessage()));
        }
    }

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
        } catch (Exception e) {
            System.out.println("Check msg: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "status", "error",
                    "message", "Lấy báo cáo thất bại: " + e.getMessage()));
        }
    }

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
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "status", "error",
                    "message", "Lấy báo cáo thất bại: " + e.getMessage()));
        }
    }

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
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "status", "error",
                    "message", "Lấy báo cáo thất bại: " + e.getMessage()));
        }
    }

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
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "status", "error",
                    "message", "Lấy báo cáo thất bại: " + e.getMessage()));
        }
    }

    @GetMapping("/information-employy")
    @ResponseBody
    public ResponseEntity<?> getInformationEmployyController() {
        try {
            List<EmployeeInfoRes> res = employeeService.getEmployeeInformationService();
            return ResponseEntity.ok(Map.of("data", res, "status", "success", "message", "Lấy report thành công"));
        } catch (Exception e) {
            System.out.println("Check msg: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("status", "error", "message", "Lấy báo cáo thất bại: " + e.getMessage()));
        }
    }

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
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "status", "error",
                    "message", "Lấy báo cáo thất bại: " + e.getMessage()));
        }
    }

}
