package com.windy.cafemanagement.Responses;

import java.util.List;

/**
 * TableToMergeRes class
 *
 * Version 1.0
 *
 * Date: 11-10-2025
 *
 * Copyright
 *
 * Modification Logs:
 * DATE AUTHOR DESCRIPTION
 * -----------------------------------------------------------------------
 * 11-10-2025 VuLQ Create
 */
public class TableToMergeRes {
    private List<TableInforRes> tablesFrom;
    private List<TableInforRes> tablesTo;

    public TableToMergeRes() {
    }

    public TableToMergeRes(List<TableInforRes> tablesFrom, List<TableInforRes> tablesTo) {
        this.tablesFrom = tablesFrom;
        this.tablesTo = tablesTo;
    }

    public List<TableInforRes> getTablesFrom() {
        return tablesFrom;
    }

    public void setTablesFrom(List<TableInforRes> tablesFrom) {
        this.tablesFrom = tablesFrom;
    }

    public List<TableInforRes> getTablesTo() {
        return tablesTo;
    }

    public void setTablesTo(List<TableInforRes> tablesTo) {
        this.tablesTo = tablesTo;
    }

}
