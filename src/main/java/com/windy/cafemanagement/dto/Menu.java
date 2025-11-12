package com.windy.cafemanagement.dto;

import java.util.ArrayList;
import java.util.List;

import com.windy.cafemanagement.models.InvoiceDetail;

import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;

/**
 * Menu DTO
 *
 * Version 1.0
 *
 * Date: 10-11-2025
 *
 * Copyright
 *
 * Modification Logs:
 * DATE AUTHOR DESCRIPTION
 * -----------------------------------------------------------------------
 * 10-11-2025 VuLQ Create
 */
public class Menu {
    private Long menuId;
    private Integer quantity;
    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL)
    private List<InvoiceDetail> invoiceDetails = new ArrayList<>();

    public Menu() {
    }

    public Menu(Long menuId, Integer quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public List<InvoiceDetail> getInvoiceDetails() {
        return invoiceDetails;
    }

    public void setInvoiceDetails(List<InvoiceDetail> invoiceDetails) {
        this.invoiceDetails = invoiceDetails;
    }

    @Override
    public String toString() {
        return "Menus [menuId=" + menuId + ", quantity=" + quantity + "]";
    }

}
