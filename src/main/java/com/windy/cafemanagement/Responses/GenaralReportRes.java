package com.windy.cafemanagement.Responses;

import java.time.LocalDate;

/**
 * GenaralReportRes class
 *
 * Version 1.0
 *
 * Date: 11-10-2025
 *
 * Copyright
 *
 * Modification Logs:
 * DATE AUTHOR DESCRIPTION
 * -----------------------------------------------------------------------
 * 11-10-2025 VuLQ Create
 */
public class GenaralReportRes {
    private LocalDate date;
    private Double income;
    private Double expense;

    public GenaralReportRes() {
    }

    public GenaralReportRes(LocalDate date, Double income, Double expense) {
        this.date = date;
        this.income = income;
        this.expense = expense;
    }

    public GenaralReportRes(String date, Double income, Double expense) {
        this.date = LocalDate.parse(date);
        this.income = income;
        this.expense = expense;
    }

    public LocalDate  getDate() {
        return date;
    }

    public void setDate(LocalDate  date) {
        this.date = date;
    }

    public Double getIncome() {
        return income;
    }

    public void setIncome(Double income) {
        this.income = income;
    }

    public Double getExpense() {
        return expense;
    }

    public void setExpense(Double expense) {
        this.expense = expense;
    }
}
