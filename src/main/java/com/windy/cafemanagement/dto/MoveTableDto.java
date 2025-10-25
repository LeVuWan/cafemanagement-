package com.windy.cafemanagement.dto;

public class MoveTableDto {
    private Long fromTableId;
    private Long toTableId;

    public MoveTableDto() {
    }

    public MoveTableDto(Long fromTableId, Long toTableId) {
        this.fromTableId = fromTableId;
        this.toTableId = toTableId;
    }

    public Long getFromTableId() {
        return fromTableId;
    }

    public void setFromTableId(Long fromTableId) {
        this.fromTableId = fromTableId;
    }

    public Long getToTableId() {
        return toTableId;
    }

    public void setToTableId(Long toTableId) {
        this.toTableId = toTableId;
    }

}
