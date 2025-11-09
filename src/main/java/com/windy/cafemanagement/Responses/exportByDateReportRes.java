package com.windy.cafemanagement.Responses;

public class exportByDateReportRes {
    private String exportDate;

    private Double totalAmount;

    public exportByDateReportRes(String exportDate, Double totalAmount) {
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
