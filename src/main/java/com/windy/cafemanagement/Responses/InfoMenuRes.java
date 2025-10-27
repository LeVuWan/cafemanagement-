package com.windy.cafemanagement.Responses;

public class InfoMenuRes {
    private String dishName;
    private Integer quantity;
    private Long menuId;

    public InfoMenuRes() {
    }

    public InfoMenuRes(Long menuId, String dishName, Integer quantity) {
        this.menuId = menuId;
        this.dishName = dishName;
        this.quantity = quantity;
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

}
