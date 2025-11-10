package com.windy.cafemanagement.dto;

import java.time.LocalDate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * create imprort product dto class
 *
 * Version 1.0
 *
 * Date: 10-11-2025
 *
 * Copyright 
 *
 * Modification Logs:
 * DATE                 AUTHOR          DESCRIPTION
 * -----------------------------------------------------------------------
 * 10-11-2025         VuLQ            Create
 */
public class ImportProductDto {
    private Long productId;
    @NotBlank(message = "Tên hàng hóa không được trống")
    private String productName;
    @NotNull(message = "Số lượng không được để trống")
    private Double quantity;
    @NotNull(message = "Đơn giá không được trống")
    private Double unitPrice;
    @NotNull(message = "Ngày mua không được trống")
    private LocalDate purchaseDate;
    @NotNull(message = "Đơn vị tính không được trống")
    private Long unitId;

    public ImportProductDto() {
    }

    public ImportProductDto(Long productId, String productName, Double quantity, Double unitPrice,
            LocalDate purchaseDate, Long unitId) {
        this.productName = productName;
        this.productId = productId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.purchaseDate = purchaseDate;
        this.unitId = unitId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Long getUnitId() {
        return unitId;
    }

    public void setUnitId(Long unitId) {
        this.unitId = unitId;
    }

}
