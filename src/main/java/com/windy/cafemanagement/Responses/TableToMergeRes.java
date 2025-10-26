package com.windy.cafemanagement.Responses;

import java.util.List;

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
