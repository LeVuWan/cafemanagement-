package com.windy.cafemanagement.dto;

import java.time.LocalDate;
import jakarta.validation.constraints.NotNull;

/**
 * create exprort product dto class
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
public class ExportProductDto {
    @NotNull(message = "Chưa chọn hàng hóa")
    private Long productId;
    @NotNull(message = "Số lượng không được để trống")
    private Double quantity;
    @NotNull(message = "Ngày mua không được trống")
    private LocalDate exportDate;

    public ExportProductDto(@NotNull(message = "Chưa chọn hàng hóa") Long productId,
            @NotNull(message = "Số lượng không được để trống") Double quantity,
            @NotNull(message = "Ngày mua không được trống") LocalDate exportDate) {
        this.productId = productId;
        this.quantity = quantity;
        this.exportDate = exportDate;
    }

    public ExportProductDto() {
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

    public LocalDate getExportDate() {
        return exportDate;
    }

    public void setExportDate(LocalDate exportDate) {
        this.exportDate = exportDate;
    }

}
