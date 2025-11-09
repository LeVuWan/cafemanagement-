package com.windy.cafemanagement.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.windy.cafemanagement.Services.ExpenseService;
import com.windy.cafemanagement.dto.ExpenseDTO;
import com.windy.cafemanagement.models.Expense;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin/expense")
public class ExpenseController {
    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @GetMapping("")
    public String getExpense(Model model) {
        List<ExpenseDTO> expenses = expenseService.getAllExpenses();
        model.addAttribute("expenses", expenses);
        return "admin/expense/list-expense";
    }

    @GetMapping("list-expense")
    @ResponseBody
    public ResponseEntity<?> createExpenseController() {
        try {
            List<ExpenseDTO> expenses = expenseService.getAllExpenses();
            System.out.println("Check expenses: " + expenses.toString());
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "data", expenses,
                    "message", "Lấy danh sách chi tiêu thành công"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "status", "error",
                    "message", "Lấy danh sách chi tiêu thất bại: " + e.getMessage()));
        }
    }

    @PostMapping("create")
    @ResponseBody
    public ResponseEntity<?> createExpenseController(@Valid @RequestBody ExpenseDTO expenseDTO,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "status", "error",
                    "message", bindingResult.getAllErrors().get(0).getDefaultMessage()));
        }

        try {
            expenseService.createExpense(expenseDTO);

            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Thêm mới chi tiêu thành công"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "status", "error",
                    "message", "Thêm mới chi tiêu thất bại: " + e.getMessage()));
        }
    }
}
