package com.windy.cafemanagement.Responses;

public class InfoMenuRes {
    private String dishName;
    private Integer quantity;

    public InfoMenuRes() {
    }

    public InfoMenuRes(String dishName, Integer quantity) {
        this.dishName = dishName;
        this.quantity = quantity;
    }

    public String getMenuName() {
        return dishName;
    }

    public void setMenuName(String dishName) {
        this.dishName = dishName;
    }

    public Integer getquantity() {
        return quantity;
    }

    public void setquantity(Integer quantity) {
        this.quantity = quantity;
    }

}
