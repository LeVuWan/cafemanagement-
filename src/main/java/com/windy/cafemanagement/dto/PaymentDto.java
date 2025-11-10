package com.windy.cafemanagement.dto;

/**
 * payment dto class
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
public class PaymentDto {
    private Long voucherId;
    private Long tableId;

    public PaymentDto() {
    }

    public PaymentDto(Long voucherId, Long tableId) {
        this.voucherId = voucherId;
        this.tableId = tableId;
    }

    public Long getVoucherId() {
        return voucherId;
    }

    public void setVoucherId(Long voucherId) {
        this.voucherId = voucherId;
    }

    public Long getTableId() {
        return tableId;
    }

    public void setTableId(Long tableId) {
        this.tableId = tableId;
    }

}
