package com.windy.cafemanagement.Responses;

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