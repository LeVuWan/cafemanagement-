package com.windy.cafemanagement.models;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

/**
 * InvoiceDetail class
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
@Entity
@Table(name = "invoice_detail")
public class InvoiceDetail {
    @EmbeddedId
    private InvoiceDetailId id;

    @ManyToOne
    @MapsId("menuId")
    @JoinColumn(name = "menu_id")
    private Menu menu;

    @ManyToOne
    @MapsId("invoiceId")
    @JoinColumn(name = "invoice_id")
    private Invoice invoice;

    private Integer quantity;
    private Double totalPrice;
    private Boolean isDeleted;

    public InvoiceDetail() {
    }

    public InvoiceDetail(InvoiceDetailId id, Menu menu, Invoice invoice, Integer quantity, Double totalPrice,
            Boolean isDeleted) {
        this.id = id;
        this.menu = menu;
        this.invoice = invoice;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.isDeleted = isDeleted;
    }

    public InvoiceDetailId getId() {
        return id;
    }

    public void setId(InvoiceDetailId id) {
        this.id = id;
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

}
