package com.windy.cafemanagement.models;

import java.io.Serializable;
import java.util.Objects;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

/**
 * MenuDetailId class
 *
 * Version 1.0
 *
 * Date: 12-11-2025
 *
 * Copyright
 *
 * Modification Logs:
 * DATE AUTHOR DESCRIPTION
 * -----------------------------------------------------------------------
 * 12-11-2025 VuLQ Create
 */
@Embeddable
public class MenuDetailId implements Serializable {
    @Column(name = "menu_id")
    private Long menuId;
    @Column(name = "product_id")
    private Long productId;

    public MenuDetailId() {
    }

    public MenuDetailId(Long menuId, Long productId) {
        this.menuId = menuId;
        this.productId = productId;
    }

    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MenuDetailId that = (MenuDetailId) o;
        return Objects.equals(menuId, that.menuId) && Objects.equals(productId, that.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(menuId, productId);
    }

}
