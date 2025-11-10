package com.windy.cafemanagement.dto;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Order Table Dto class
 *
 * Version 1.0
 *
 * Date: 12-11-2013
 *
 * Copyright 
 *
 * Modification Logs:
 * DATE                 AUTHOR          DESCRIPTION
 * -----------------------------------------------------------------------
 * 12-11-2013         VuLQ            Create
 */
public class OrderTableDto {
    private Long tableId;
    private String customerName;
    private String customerPhone;
    private LocalTime timeOrder;
    private LocalDate dateOrder;

    public OrderTableDto() {
    }

    public OrderTableDto(Long tableId, String customerName, String customerPhone, LocalTime timeOrder,
            LocalDate dateOrder) {
        this.tableId = tableId;
        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.timeOrder = timeOrder;
        this.dateOrder = dateOrder;
    }

    public Long getTableId() {
        return tableId;
    }

    public void setTableId(Long tableId) {
        this.tableId = tableId;
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

    public LocalTime getTimeOrder() {
        return timeOrder;
    }

    public void setTimeOrder(LocalTime timeOrder) {
        this.timeOrder = timeOrder;
    }

    public LocalDate getDateOrder() {
        return dateOrder;
    }

    public void setDateOrder(LocalDate dateOrder) {
        this.dateOrder = dateOrder;
    }

}
