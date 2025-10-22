package com.windy.cafemanagement.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class MenuDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long menuDetailId;

    @ManyToOne
    @JoinColumn(name = "productId")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "menuId")
    private Menu menu;

    private Double quantity;

    @ManyToOne
    @JoinColumn(name = "unitId")
    private Unit unit;

    private Boolean isDeleted;

    public MenuDetail() {
    }

    public MenuDetail(Long menuDetailId, Product product, Menu menu, Double quantity, Boolean isDeleted, Unit unit) {
        this.menuDetailId = menuDetailId;
        this.product = product;
        this.menu = menu;
        this.quantity = quantity;
        this.isDeleted = isDeleted;
        this.unit = unit;
    }

    public Long getMenuDetailId() {
        return menuDetailId;
    }

    public void setMenuDetailId(Long menuDetailId) {
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