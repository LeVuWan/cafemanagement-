package com.windy.cafemanagement.dto;

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
