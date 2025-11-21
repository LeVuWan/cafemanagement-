package com.windy.cafemanagement.Services;

public class MenuForChooseMenuRes {
    private Long menuId;
    private String dishName;

    public MenuForChooseMenuRes(String dishName) {
        this.dishName = dishName;
    }

    public MenuForChooseMenuRes(Long menuId, String dishName) {
        this.menuId = menuId;
        this.dishName = dishName;
    }

    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }

    public String getDishName() {
        return dishName;
    }

    public void setDishName(String dishName) {
        this.dishName = dishName;
    }

}
