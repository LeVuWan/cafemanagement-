package com.windy.cafemanagement.dto;

/**
 * Move Table Dto class
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
