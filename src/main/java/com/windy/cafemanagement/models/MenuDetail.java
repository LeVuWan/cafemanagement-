package com.windy.cafemanagement.models;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;

/**
 * MenuDetail class
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
@Entity
public class MenuDetail {
    @EmbeddedId
    private MenuDetailId menuDetailId;

    @ManyToOne
    @MapsId("productId")
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @MapsId("menuId")
    @JoinColumn(name = "menu_id")
    private Menu menu;

    private Double quantity;

    @ManyToOne
    @JoinColumn(name = "unit_id")
    private Unit unit;

    private Boolean isDeleted;

    public MenuDetail() {
    }

    public MenuDetail(MenuDetailId menuDetailId, Product product, Menu menu, Double quantity, Boolean isDeleted, Unit unit) {
        this.menuDetailId = menuDetailId;
        this.product = product;
        this.menu = menu;
        this.quantity = quantity;
        this.isDeleted = isDeleted;
        this.unit = unit;
    }

    public MenuDetailId getMenuDetailId() {
        return menuDetailId;
    }

    public void setMenuDetailId(MenuDetailId menuDetailId) {
        this.menuDetailId = menuDetailId;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

}