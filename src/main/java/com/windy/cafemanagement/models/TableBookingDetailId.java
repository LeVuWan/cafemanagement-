package com.windy.cafemanagement.models;

import java.io.Serializable;
import java.util.Objects;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

/**
 * TableBookingDetailId class
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
public class TableBookingDetailId implements Serializable {
    @Column(name = "table_id")
    private Long tableId;

    @Column(name = "employee_id")
    private Long employeeId;

    @Column(name = "invoice_id")
    private Long invoiceId;

    public TableBookingDetailId() {
    }

    public TableBookingDetailId(Long tableId, Long employeeId, Long invoiceId) {
        this.tableId = tableId;
        this.employeeId = employeeId;
        this.invoiceId = invoiceId;
    }

    public Long getTableId() {
        return tableId;
    }

    public void setTableId(Long tableId) {
        this.tableId = tableId;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public Long getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(Long invoiceId) {
        this.invoiceId = invoiceId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        TableBookingDetailId that = (TableBookingDetailId) o;
        return Objects.equals(tableId, that.tableId) && Objects.equals(employeeId, that.employeeId)
                && Objects.equals(invoiceId, that.invoiceId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tableId, employeeId, invoiceId);
    }

}
