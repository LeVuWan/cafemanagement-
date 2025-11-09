package com.windy.cafemanagement.Responses;

public class TotalInvoiceByDateRes {
    private String date;

    private Double totalAmount;

    public TotalInvoiceByDateRes(String date, Double totalAmount) {
        this.date = date;
        this.totalAmount = totalAmount;
    }

    public String getImportDate() {
        return date;
    }

    public void setImportDate(String date) {
        this.date = date;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }
}
