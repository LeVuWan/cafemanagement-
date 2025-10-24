package com.windy.cafemanagement.models;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "table_booking_detail")
public class TableBookingDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tableBookingDetailId;

    @ManyToOne
    @JoinColumn(name = "tableId")
    private TableEntity table;

    @ManyToOne
    @JoinColumn(name = "employeeId")
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "invoiceId")
    private Invoice invoice;

    private String customerName;
    private String customerPhone;
    private LocalDateTime bookingTime;
    private Boolean isDeleted;

    public TableBookingDetail() {
    }

    public TableBookingDetail(Long tableBookingDetailId, TableEntity table, Employee employee, Invoice invoice,
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

    public Long getTableBookingDetailId() {
        return tableBookingDetailId;
    }

    public void setTableBookingDetailId(Long tableBookingDetailId) {
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

}
