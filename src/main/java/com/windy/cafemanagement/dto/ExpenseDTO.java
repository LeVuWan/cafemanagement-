package com.windy.cafemanagement.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ExpenseDTO {
    @NotBlank(message = "Tên khoản chi không được để trống")
    private String expenseName;

    @NotNull(message = "Số tiền không được để trống")
    private Double amount;

    @NotNull(message = "Ngày chi không được để trống")
    private LocalDate expenseDate;

    public ExpenseDTO() {
    }

    public ExpenseDTO(@NotNull(message = "Tên khoản chi không được để trống") String expenseName,
            @NotNull(message = "Số tiền không được để trống") Double amount,
            @NotNull(message = "Ngày chi không được để trống") LocalDate expenseDate) {
        this.expenseName = expenseName;
        this.amount = amount;
        this.expenseDate = expenseDate;
    }

    public String getExpenseName() {
        return expenseName;
    }

    public void setExpenseName(String expenseName) {
        this.expenseName = expenseName;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public LocalDate getExpenseDate() {
        return expenseDate;
    }

    public void setExpenseDate(LocalDate expenseDate) {
        this.expenseDate = expenseDate;
    }

}
