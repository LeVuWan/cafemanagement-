package com.windy.cafemanagement.Responses;

/**
 * ExportByDateReportRes class
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
public class ExportByDateReportRes {
    private String exportDate;

    private Double totalAmount;

    public ExportByDateReportRes(String exportDate, Double totalAmount) {
        this.exportDate = exportDate;
        this.totalAmount = totalAmount;
    }

    public String getImportDate() {
        return exportDate;
    }

    public void setImportDate(String exportDate) {
        this.exportDate = exportDate;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

}
