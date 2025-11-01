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

    public Expense createExpense(ExpenseDTO dto) {
        String username = SecurityUtil.getSessionUser();
        Employee employee = employeeRepository.findByUsername(username);

        Expense expense = new Expense();
        expense.setEmployee(employee);
        expense.setExpenseName(dto.getExpenseName());
        expense.setAmount(dto.getAmount());
        expense.setExpenseDate(dto.getExpenseDate());
        expense.setIsDelete(false);

        return expenseRepository.save(expense);
    }

    public List<Expense> getAllExpenses() {
        return expenseRepository.findAllByIsDelete(false);
    }
}
