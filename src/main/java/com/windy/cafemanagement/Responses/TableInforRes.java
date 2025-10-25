package com.windy.cafemanagement.Responses;

public class TableInforRes {
    private Long tableId;
    private String tableName;

    public TableInforRes() {
    }

    public TableInforRes(Long tableId, String tableName) {
        this.tableId = tableId;
        this.tableName = tableName;
    }

    public Long getTableId() {
        return tableId;
    }

    public void setTableId(Long tableId) {
        this.tableId = tableId;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

}
