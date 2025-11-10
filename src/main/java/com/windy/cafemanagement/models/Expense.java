package com.windy.cafemanagement.models;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * Expense class
 *
 * Version 1.0
 *
 * Date: 10-11-2025
 *
 * Copyright 
 *
 * Modification Logs:
 * DATE                 AUTHOR          DESCRIPTION
 * -----------------------------------------------------------------------
 * 10-11-2025         VuLQ            Create
 */
@Entity
@Table(name = "expense")
public class Expense {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long expenseId;
    @ManyToOne
    @JoinColumn(name = "employeeid")
    private Employee employee;
    @NotNull(message = "Số tiền không được để trống")
    @Positive(message = "Số tiền phải lớn hơn 0")
    private Double amount;

    @NotBlank(message = "Tên chi tiêu không được để trống")
    private String expenseName;

    @NotNull(message = "Ngày chi tiêu không được để trống")
    private LocalDate expenseDate;
    private Boolean isDelete;

    public Expense() {
    }

    public Expense(Long expenseId, Employee employee, Double amount, String expenseName, LocalDate expenseDate,
            Boolean isDelete) {
        this.expenseId = expenseId;
        this.employee = employee;
        this.amount = amount;
        this.expenseName = expenseName;
        this.expenseDate = expenseDate;
        this.isDelete = isDelete;
    }

    public Long getExpenseId() {
        return expenseId;
    }

    public void setExpenseId(Long expenseId) {
        this.expenseId = expenseId;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getExpenseName() {
        return expenseName;
    }

    public void setExpenseName(String expenseName) {
        this.expenseName = expenseName;
    }

    public LocalDate getExpenseDate() {
        return expenseDate;
    }

    public void setExpenseDate(LocalDate expenseDate) {
        this.expenseDate = expenseDate;
    }

    public Boolean getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Boolean isDelete) {
        this.isDelete = isDelete;
    }

    @Override
    public String toString() {
        return "Expense [expenseId=" + expenseId + ", employee=" + employee + ", amount=" + amount + ", expenseName="
                + expenseName + ", expenseDate=" + expenseDate + ", isDelete=" + isDelete + "]";
    }

}