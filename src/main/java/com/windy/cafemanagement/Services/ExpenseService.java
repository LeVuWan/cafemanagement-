package com.windy.cafemanagement.Services;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.windy.cafemanagement.configs.SecurityUtil;
import com.windy.cafemanagement.dto.ExpenseDTO;
import com.windy.cafemanagement.models.Employee;
import com.windy.cafemanagement.models.Expense;
import com.windy.cafemanagement.repositories.EmployeeRepository;
import com.windy.cafemanagement.repositories.ExpenseRepository;

/**
 * ExpenseService
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
 * 10-11-2025 VuLQ Create
 */
@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final EmployeeRepository employeeRepository;

    public ExpenseService(ExpenseRepository expenseRepository, EmployeeRepository employeeRepository) {
        this.expenseRepository = expenseRepository;
        this.employeeRepository = employeeRepository;
    }

    /**
     * create expense
     * 
     * @param expenseDTO
     * @throws DataAccessException
     */
    public void createExpense(ExpenseDTO expenseDTO) throws DataAccessException {
        Expense expense = expenseDTOToExpense(expenseDTO);
        expenseRepository.save(expense);
    }

    /**
     * get list expense
     * 
     * @return List<ExpenseDTO>
     * @throws DataAccessException
     */
    public List<ExpenseDTO> getAllExpenses() throws DataAccessException {
        List<Expense> expenses = expenseRepository.findByIsDeleteFalse();
        List<ExpenseDTO> expenseDTOs = expenses.stream()
                .map(this::expenseToExpenseDTO)
                .toList();
        return expenseDTOs;
    }

    /**
     * Convert ExpenseDTO to Expense 
     * 
     * @param expenseDTO
     * @return Expense
     */
    private Expense expenseDTOToExpense(ExpenseDTO expenseDTO) {
        Expense expense = new Expense();
        expense.setAmount(expenseDTO.getAmount());
        expense.setExpenseName(expenseDTO.getExpenseName());
        expense.setExpenseDate(expenseDTO.getExpenseDate());
        expense.setIsDelete(false);
        String username = SecurityUtil.getSessionUser();
        Employee employee = employeeRepository.findByUsername(username);
        expense.setEmployee(employee);
        return expense;
    }

    /**
     * Convert Expense to ExpenseDTO
     * 
     * @param expense
     * @return ExpenseDTO
     */
    private ExpenseDTO expenseToExpenseDTO(Expense expense) {
        ExpenseDTO expenseDTO = new ExpenseDTO();
        expenseDTO.setAmount(expense.getAmount());
        expenseDTO.setExpenseName(expense.getExpenseName());
        expenseDTO.setExpenseDate(expense.getExpenseDate());
        return expenseDTO;
    }
}