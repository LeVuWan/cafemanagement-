package com.windy.cafemanagement.Responses;

public class InfoMenuRes {
    private String dishName;
    private Integer quantity;
    private Long menuId;
    private Double totalPrice;

    public InfoMenuRes() {
    }

    public InfoMenuRes(Long menuId, String dishName, Integer quantity, Double totalPrice) {
        this.menuId = menuId;
        this.dishName = dishName;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
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

}
