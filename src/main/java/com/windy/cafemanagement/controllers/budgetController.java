package com.windy.cafemanagement.controllers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.windy.cafemanagement.Responses.FinancialSummaryRes;
import com.windy.cafemanagement.Services.BudgetService;
import com.windy.cafemanagement.Services.ExpenseService;
import com.windy.cafemanagement.dto.ExpenseDTO;
import com.windy.cafemanagement.models.Expense;

import jakarta.validation.Valid;

@Controller
@RequestMapping("admin/budget")
public class BudgetController {

    private final BudgetService budgetService;
    private final ExpenseService expenseService;

    public BudgetController(BudgetService budgetService, ExpenseService expenseService) {
        this.budgetService = budgetService;
        this.expenseService = expenseService;
    }

    @GetMapping("")
    public String getFinancialSummary(
            @RequestParam(value = "startDate", required = false) String startDateStr,
            @RequestParam(value = "endDate", required = false) String endDateStr,
            Model model) {
        LocalDate startDate = (startDateStr != null && !startDateStr.isEmpty())
                ? LocalDate.parse(startDateStr)
                : LocalDate.now().minusDays(7);

        LocalDate endDate = (endDateStr != null && !endDateStr.isEmpty())
                ? LocalDate.parse(endDateStr)
                : LocalDate.now();

        List<FinancialSummaryRes> financialSummaries = budgetService.getFinancialSummary(startDate, endDate);

        model.addAttribute("financialSummaries", financialSummaries);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);

        return "admin/budget/list_bubget";
    }

    @GetMapping("/create-expense")
    public String getCreateExpense(Model model) {
        List<Expense> expenses = expenseService.getAllExpenses();
        model.addAttribute("expenses", expenses);
        return "admin/expense/list-expense";
    }

    @PostMapping("/create-expense")
    public ResponseEntity<?> createExpense(@Valid @RequestBody ExpenseDTO dto, BindingResult result) {
        if (result.hasErrors()) {
            String errorMessage = result.getAllErrors().get(0).getDefaultMessage();
            return ResponseEntity.badRequest().body(errorMessage);
        }
        try {
            Expense expense = expenseService.createExpense(dto);
            return ResponseEntity.ok(expense);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Lỗi khi lưu dữ liệu: " + e.getMessage());
        }
    }
}
