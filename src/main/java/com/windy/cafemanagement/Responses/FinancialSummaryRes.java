package com.windy.cafemanagement.Responses;

import java.time.LocalDate;

public class FinancialSummaryRes {
    private LocalDate date;
    private Double totalIncome;
    private Double totalExpense;

    public FinancialSummaryRes() {
    }

    public FinancialSummaryRes(LocalDate date, Double totalIncome, Double totalExpense) {
        this.date = date;
        this.totalIncome = totalIncome;
        this.totalExpense = totalExpense;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Double getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(Double totalIncome) {
        this.totalIncome = totalIncome;
    }

    public Double getTotalExpense() {
        return totalExpense;
    }

    public void setTotalExpense(Double totalExpense) {
        this.totalExpense = totalExpense;
    }

    @Override
    public String toString() {
        return "FinancialSummaryRes{" +
                "date=" + date +
                ", totalIncome=" + totalIncome +
                ", totalExpense=" + totalExpense +
                '}';
    }
}
