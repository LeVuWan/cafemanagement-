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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import jakarta.validation.Valid;
import jakarta.persistence.EntityNotFoundException;

import com.windy.cafemanagement.Services.ExpenseService;
import com.windy.cafemanagement.dto.ExpenseDTO;

@Controller
@RequestMapping("/admin/expense")
/**
 * ExpenseController
 *
 * Version 1.0
 *
 * Date: 12-11-2013
 *
 * Copyright
 *
 * Modification Logs:
 * DATE                 AUTHOR          DESCRIPTION
 * -----------------------------------------------------------------------
 */
public class ExpenseController {
    private static final Logger logger = LoggerFactory.getLogger(ExpenseController.class);

    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    /**
     * Display the expense listing page.
     * @param model the Spring MVC model
     * @return the view name for the expense list page
     * @throws Exception when retrieving expenses fails
     */
    @GetMapping("")
    public String getExpense(Model model) {
        try {
            List<ExpenseDTO> expenses = expenseService.getAllExpenses();
            model.addAttribute("expenses", expenses);
            return "admin/expense/list-expense";
        } catch (EntityNotFoundException ex) {
            logger.error("Expenses not found: {}", ex.getMessage(), ex);
            model.addAttribute("errorMessage", "Lấy danh sách chi tiêu thất bại: " + ex.getMessage());
            return "admin/errors/500-error";
        } catch (DataAccessException ex) {
            logger.error("Database error while fetching expenses: {}", ex.getMessage(), ex);
            model.addAttribute("errorMessage", "Lỗi cơ sở dữ liệu khi lấy danh sách chi tiêu: " + ex.getMessage());
            return "admin/errors/500-error";
        } catch (IllegalArgumentException | NullPointerException ex) {
            logger.error("Invalid arguments while fetching expenses: {}", ex.getMessage(), ex);
            model.addAttribute("errorMessage", "Dữ liệu không hợp lệ: " + ex.getMessage());
            return "admin/errors/500-error";
        } catch (Exception ex) {
            logger.error("Unexpected error while fetching expenses: {}", ex.getMessage(), ex);
            model.addAttribute("errorMessage", "Lấy danh sách chi tiêu thất bại: " + ex.getMessage());
            return "admin/errors/500-error";
        }
    }

    /**
     * Return the list of expenses as JSON.
     * @return ResponseEntity containing the list of expenses or an error message
     * @throws Exception when retrieval fails
     */
    @GetMapping("list-expense")
    @ResponseBody
    public ResponseEntity<?> createExpenseController() {
        try {
            List<ExpenseDTO> expenses = expenseService.getAllExpenses();
            logger.debug("Check expenses: {}", expenses);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "data", expenses,
                    "message", "Lấy danh sách chi tiêu thành công"));
        } catch (EntityNotFoundException ex) {
            logger.error("Expenses not found: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "error",
                    "message", "Lấy danh sách chi tiêu thất bại: " + ex.getMessage()));
        } catch (DataAccessException ex) {
            logger.error("Database error while listing expenses: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "error",
                    "message", "Lỗi cơ sở dữ liệu khi lấy danh sách chi tiêu: " + ex.getMessage()));
        } catch (IllegalArgumentException | NullPointerException ex) {
            logger.error("Invalid argument while listing expenses: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "status", "error",
                    "message", "Dữ liệu không hợp lệ: " + ex.getMessage()));
        } catch (Exception ex) {
            logger.error("Unexpected error while listing expenses: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "error",
                    "message", "Lấy danh sách chi tiêu thất bại: " + ex.getMessage()));
        }
    }

    /**
     * Create a new expense from the provided DTO.
     * @param expenseDTO the expense data transfer object
     * @param bindingResult validation result for the DTO
     * @return ResponseEntity with success or error message
     * @throws Exception when creation fails
     */
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
        } catch (DataAccessException ex) {
            logger.error("Database error while creating expense: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "error",
                    "message", "Lỗi cơ sở dữ liệu khi thêm chi tiêu: " + ex.getMessage()));
        } catch (IllegalArgumentException | NullPointerException ex) {
            logger.error("Invalid data while creating expense: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "status", "error",
                    "message", "Dữ liệu không hợp lệ: " + ex.getMessage()));
        } catch (Exception ex) {
            logger.error("Unexpected error while creating expense: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "error",
                    "message", "Thêm mới chi tiêu thất bại: " + ex.getMessage()));
        }
    }
}
