package com.windy.cafemanagement.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;

public class ExportProductDto {
    @NotNull(message = "Chưa chọn hàng hóa")
    private Long productId;
    @NotNull(message = "Số lượng không được để trống")
    private Integer quantity;
    @NotNull(message = "Ngày mua không được trống")
    private LocalDate exportDate;

    public ExportProductDto(@NotNull(message = "Chưa chọn hàng hóa") Long productId,
            @NotNull(message = "Số lượng không được để trống") Integer quantity,
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

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public LocalDate getExportDate() {
        return exportDate;
    }

    public void setExportDate(LocalDate exportDate) {
        this.exportDate = exportDate;
    }

}
