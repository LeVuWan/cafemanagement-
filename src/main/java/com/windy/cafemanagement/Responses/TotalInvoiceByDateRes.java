package com.windy.cafemanagement.Responses;

/**
 * TotalInvoiceByDateRes class
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
