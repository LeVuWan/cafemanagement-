package com.windy.cafemanagement.Responses;

import com.windy.cafemanagement.enums.TableStatus;

public class TableRes {
    private Long tableId;
    private TableStatus status;
    private String tableName;

    public TableRes() {
    }

    public TableRes(Long tableId, TableStatus status, String tableName) {
        this.tableId = tableId;
        this.status = status;
        this.tableName = tableName;
    }

    public Long getTableId() {
        return tableId;
    }

    public void setTableId(Long tableId) {
        this.tableId = tableId;
    }

    public TableStatus getStatus() {
        return status;
    }

    public void setStatus(TableStatus status) {
        this.status = status;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

}
