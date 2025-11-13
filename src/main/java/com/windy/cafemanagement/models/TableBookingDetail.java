package com.windy.cafemanagement.models;

import java.time.LocalDateTime;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

/**
 * TableBookingDetail class
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
@Table(name = "table_booking_detail")
public class TableBookingDetail {
    @EmbeddedId
    private TableBookingDetailId tableBookingDetailId;

    @ManyToOne
    @MapsId("tableId")
    @JoinColumn(name = "table_id")
    private TableEntity table;

    @ManyToOne
    @MapsId("employeeId")
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @ManyToOne
    @MapsId("invoiceId")
    @JoinColumn(name = "invoice_id")
    private Invoice invoice;

    private String customerName;
    private String customerPhone;
    private LocalDateTime bookingTime;
    private Boolean isDeleted;

    public TableBookingDetail() {
    }

    public TableBookingDetail(TableBookingDetailId tableBookingDetailId, TableEntity table, Employee employee,
            Invoice invoice,
            String customerName, String customerPhone, LocalDateTime bookingTime, Boolean isDeleted) {
        this.tableBookingDetailId = tableBookingDetailId;
        this.table = table;
        this.employee = employee;
        this.invoice = invoice;
        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.bookingTime = bookingTime;
        this.isDeleted = isDeleted;
    }

    public TableBookingDetailId getTableBookingDetailId() {
        return tableBookingDetailId;
    }

    public void setTableBookingDetailId(TableBookingDetailId tableBookingDetailId) {
        this.tableBookingDetailId = tableBookingDetailId;
    }

    public TableEntity getTable() {
        return table;
    }

    public void setTable(TableEntity table) {
        this.table = table;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public LocalDateTime getBookingTime() {
        return bookingTime;
    }

    public void setBookingTime(LocalDateTime bookingTime) {
        this.bookingTime = bookingTime;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    @Override
    public String toString() {
        return "TableBookingDetail [tableBookingDetailId=" + tableBookingDetailId + ", table=" + table + ", employee="
                + employee + ", invoice=" + invoice + ", customerName=" + customerName + ", customerPhone="
                + customerPhone + ", bookingTime=" + bookingTime + ", isDeleted=" + isDeleted + "]";
    }

}
