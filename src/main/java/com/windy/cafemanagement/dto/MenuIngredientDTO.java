package com.windy.cafemanagement.dto;

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
