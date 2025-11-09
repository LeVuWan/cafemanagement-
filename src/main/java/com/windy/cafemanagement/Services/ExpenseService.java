package com.windy.cafemanagement.Services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.windy.cafemanagement.configs.SecurityUtil;
import com.windy.cafemanagement.dto.ExpenseDTO;
import com.windy.cafemanagement.models.Employee;
import com.windy.cafemanagement.models.Expense;
import com.windy.cafemanagement.repositories.EmployeeRepository;
import com.windy.cafemanagement.repositories.ExpenseRepository;

@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final EmployeeRepository employeeRepository;

    public ExpenseService(ExpenseRepository expenseRepository, EmployeeRepository employeeRepository) {
        this.expenseRepository = expenseRepository;
        this.employeeRepository = employeeRepository;
    }

    public void createExpense(ExpenseDTO expenseDTO) {
        Expense expense = expenseDTOToExpense(expenseDTO);
        expenseRepository.save(expense);
    }

    public List<ExpenseDTO> getAllExpenses() {
        List<Expense> expenses = expenseRepository.findByIsDeleteFalse();
        List<ExpenseDTO> expenseDTOs = expenses.stream()
                .map(this::expenseToExpenseDTO)
                .toList();
        return expenseDTOs;
    }

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

    private ExpenseDTO expenseToExpenseDTO(Expense expense) {
        ExpenseDTO expenseDTO = new ExpenseDTO();
        expenseDTO.setAmount(expense.getAmount());
        expenseDTO.setExpenseName(expense.getExpenseName());
        expenseDTO.setExpenseDate(expense.getExpenseDate());
        return expenseDTO;
    }
}