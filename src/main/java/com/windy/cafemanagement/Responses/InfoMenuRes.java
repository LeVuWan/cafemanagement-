package com.windy.cafemanagement.Responses;

/**
 * InfoMenuRes class
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
public class InfoMenuRes {
    private String dishName;
    private Integer quantity;
    private Long menuId;
    private Double totalPrice;
    private Double unitPrice;

    public InfoMenuRes() {
    }

    public InfoMenuRes(Long menuId, String dishName, Integer quantity, Double totalPrice, Double unitPrice) {
        this.menuId = menuId;
        this.dishName = dishName;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.unitPrice = unitPrice;
    }

    public String getDishName() {
        return dishName;
    }

    public void setDishName(String dishName) {
        this.dishName = dishName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

}
