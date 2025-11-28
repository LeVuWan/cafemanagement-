package com.windy.cafemanagement.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
 * DATE AUTHOR DESCRIPTION
 * -----------------------------------------------------------------------
 * 12-11-2013 VuLQ Create
 */
public class OrderTableDto {
    private Long tableId;
    private String customerName;
    private String customerPhone;
    private LocalDateTime dateOrder;

    public OrderTableDto() {
    }

    public OrderTableDto(Long tableId, String customerName, String customerPhone,
            LocalDateTime dateOrder) {
        this.tableId = tableId;
        this.customerName = customerName;
        this.customerPhone = customerPhone;
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

    public LocalDateTime getDateOrder() {
        return dateOrder;
    }

    public void setDateOrder(LocalDateTime dateOrder) {
        this.dateOrder = dateOrder;
    }

}
