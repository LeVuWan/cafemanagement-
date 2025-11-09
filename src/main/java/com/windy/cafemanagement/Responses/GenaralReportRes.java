package com.windy.cafemanagement.Responses;

public class GenaralReportRes {
    private String date;
    private Double income;
    private Double expense;

    public GenaralReportRes() {
    }

    public GenaralReportRes(String date, Double income, Double expense) {
        this.date = date;
        this.income = income;
        this.expense = expense;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
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
