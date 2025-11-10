package com.windy.cafemanagement.dto;

import java.util.List;

/**
 * MenuIngredient DTO
 *
 * Version 1.0
 *
 * Date: 10-11-2025
 *
 * Copyright 
 *
 * Modification Logs:
 * DATE                 AUTHOR          DESCRIPTION
 * -----------------------------------------------------------------------
 * 10-11-2025         VuLQ            Create
 */
public class MergeTableDto {
    private List<Long> listIdTableFrom;
    private Long tableToId;

    public MergeTableDto() {
    }

    public MergeTableDto(List<Long> listIdTableFrom, Long tableToId) {
        this.listIdTableFrom = listIdTableFrom;
        this.tableToId = tableToId;
    }

    public List<Long> getListIdTableFrom() {
        return listIdTableFrom;
    }

    public void setListIdTableFrom(List<Long> listIdTableFrom) {
        this.listIdTableFrom = listIdTableFrom;
    }

    public Long getTableToId() {
        return tableToId;
    }

    public void setTableToId(Long tableToId) {
        this.tableToId = tableToId;
    }

}
