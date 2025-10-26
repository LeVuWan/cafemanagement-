package com.windy.cafemanagement.dto;

import java.util.List;

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
