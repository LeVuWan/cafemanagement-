package com.windy.cafemanagement.dto;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * create equipment dto class
 *
 * Version 1.0
 *
 * Date: 10-11-2025
 *
 * Copyright
 *
 * Modification Logs:
 * DATE AUTHOR DESCRIPTION
 * -----------------------------------------------------------------------
 * 10-11-2025 VuLQ Create
 */
public class EquipmentDto {
    private Long equipmentId;
    @NotBlank(message = "Tên thiết bị không được trống")
    private String equipmentName;
    @NotNull(message = "Số lượng không được để trống")
    private Integer quantity;
    @NotNull(message = "Ngày mua không được trống")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate purchaseDate;
    @NotNull(message = "Đơn giá không được trống")
    private String unitPrice;

    public EquipmentDto(Long equipmentId, @NotBlank(message = "Tên thiết bị không được trống") String equipmentName,
            @NotNull(message = "Số lượng không được để trống") Integer quantity, String note,
            @NotNull(message = "Ngày mua không được trống") LocalDate purchaseDate,
            @NotNull(message = "Đơn giá không được trống") String unitPrice) {
        this.equipmentId = equipmentId;
        this.equipmentName = equipmentName;
        this.quantity = quantity;
        this.purchaseDate = purchaseDate;
        this.unitPrice = unitPrice;
    }

    public EquipmentDto() {
    }

    public Long getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(Long equipmentId) {
        this.equipmentId = equipmentId;
    }

    public String getEquipmentName() {
        return equipmentName;
    }

    public void setEquipmentName(String equipmentName) {
        this.equipmentName = equipmentName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public String getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
    }

}
