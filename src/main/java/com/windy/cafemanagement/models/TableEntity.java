package com.windy.cafemanagement.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import com.windy.cafemanagement.enums.TableStatus;

/**
 * TableEntity class
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
@Entity
public class TableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tableId;
    private TableStatus status;
    private String tableName;
    private Boolean isDeleted = false;

    public TableEntity(Long tableId, TableStatus status, String tableName, Boolean isDeleted) {
        this.tableId = tableId;
        this.status = status;
        this.tableName = tableName;
        this.isDeleted = isDeleted;
    }

    public TableEntity() {
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

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    @Override
    public String toString() {
        return "TableEntity{" +
                "tableId=" + tableId +
                ", status=" + status +
                ", tableName='" + tableName + '\'' +
                ", isDeleted=" + isDeleted +
                '}';
    }
}
