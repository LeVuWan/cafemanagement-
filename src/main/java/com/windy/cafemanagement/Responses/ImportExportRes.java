package com.windy.cafemanagement.Responses;

/**
 * ImportExportRes class
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
public class ImportExportRes {
    private String date;
    private Double importAmount;
    private Double exportAmount;

    public ImportExportRes(String date, Double importAmount, Double exportAmount) {
        this.date = date;
        this.importAmount = importAmount;
        this.exportAmount = exportAmount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Double getImportAmount() {
        return importAmount;
    }

    public void setImportAmount(Double importAmount) {
        this.importAmount = importAmount;
    }

    public Double getExportAmount() {
        return exportAmount;
    }

    public void setExportAmount(Double exportAmount) {
        this.exportAmount = exportAmount;
    }

}
