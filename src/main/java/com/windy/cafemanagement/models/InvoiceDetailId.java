package com.windy.cafemanagement.models;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

/**
 * InvoiceDetailId class
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
public class InvoiceDetailId implements Serializable {
    @Column(name = "invoice_id")
    private Long invoiceId;

    @Column(name = "menu_id")
    private Long menuId;

    public InvoiceDetailId() {
    }

    public InvoiceDetailId(Long invoiceId, Long menuId) {
        this.invoiceId = invoiceId;
        this.menuId = menuId;
    }

    public Long getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(Long invoiceId) {
        this.invoiceId = invoiceId;
    }

    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        InvoiceDetailId that = (InvoiceDetailId) o;
        return Objects.equals(invoiceId, that.invoiceId) && Objects.equals(menuId, that.menuId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(invoiceId, menuId);
    }

}
