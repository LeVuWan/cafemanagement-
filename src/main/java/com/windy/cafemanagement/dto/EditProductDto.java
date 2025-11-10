package com.windy.cafemanagement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * edit product dto class
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
public class EditProductDto {
    private Long productId;
    @NotBlank(message = "Tên hành hóa không được trống")
    private String productName;
    @NotNull(message = "Đơn vị tính không được trống")
    private Long unitId;
    @NotNull(message = "Đơn giá không được trống")
    private Double unitPrice;

    public EditProductDto(Long productId, String productName, Long unitId, Double unitPrice) {
        this.productId = productId;
        this.productName = productName;
        this.unitId = unitId;
        this.unitPrice = unitPrice;
    }

    public EditProductDto() {
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
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

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

}