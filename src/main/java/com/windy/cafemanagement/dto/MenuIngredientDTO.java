package com.windy.cafemanagement.dto;

/**
 * MenuIngredient DTO
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
public class MenuIngredientDTO {
    private Long productId;
    private Double quantity;
    private Long unitId;

    public MenuIngredientDTO() {
    }

    public MenuIngredientDTO(Long productId, Double quantity, Long unitId) {
        this.productId = productId;
        this.quantity = quantity;
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

    public Long getUnitId() {
        return unitId;
    }

    public void setUnitId(Long unitId) {
        this.unitId = unitId;
    }

}
