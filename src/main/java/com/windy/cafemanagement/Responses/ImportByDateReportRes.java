package com.windy.cafemanagement.Responses;

/**
 * ImportByDateReportRes class
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
public class ImportByDateReportRes {
    private String importDate;

    private Double totalAmount;

    public ImportByDateReportRes(String importDate, Double totalAmount) {
        this.importDate = importDate;
        this.totalAmount = totalAmount;
    }

    public String getImportDate() {
        return importDate;
    }

    public void setImportDate(String importDate) {
        this.importDate = importDate;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

}