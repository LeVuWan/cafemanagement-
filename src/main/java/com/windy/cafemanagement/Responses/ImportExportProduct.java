package com.windy.cafemanagement.Responses;

import java.time.LocalDate;

/**
 * ImportExportProduct class
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
public class ImportExportProduct {
    private String nameProduct;
    private LocalDate importDate;
    private LocalDate exportDate;
    private Integer quantity;
    private String unit;
    private Double toltalPrice;

    public ImportExportProduct() {
    }

    public ImportExportProduct(String nameProduct, LocalDate importDate, LocalDate exportDate, Integer quantity,
            String unit, Double toltalPrice) {
        this.nameProduct = nameProduct;
        this.importDate = importDate;
        this.exportDate = exportDate;
        this.quantity = quantity;
        this.unit = unit;
        this.toltalPrice = toltalPrice;
    }

    public String getNameProduct() {
        return nameProduct;
    }

    public void setNameProduct(String nameProduct) {
        this.nameProduct = nameProduct;
    }

    public LocalDate getImportDate() {
        return importDate;
    }

    public void setImportDate(LocalDate importDate) {
        this.importDate = importDate;
    }

    public LocalDate getExportDate() {
        return exportDate;
    }

    public void setExportDate(LocalDate exportDate) {
        this.exportDate = exportDate;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Double getToltalPrice() {
        return toltalPrice;
    }

    public void setToltalPrice(Double toltalPrice) {
        this.toltalPrice = toltalPrice;
    }

}
