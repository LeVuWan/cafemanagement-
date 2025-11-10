package com.windy.cafemanagement.dto;

import java.util.List;

/**
 * Choose Menu Dto class
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
public class ChooseMenuDto {
    private Long tableId;
    private List<Menu> menu;

    public ChooseMenuDto() {
    }

    public ChooseMenuDto(Long tableId, List<Menu> menu) {
        this.tableId = tableId;
        this.menu = menu;
    }

    public Long getTableId() {
        return tableId;
    }

    public void setTableId(Long tableId) {
        this.tableId = tableId;
    }

    public List<Menu> getMenu() {
        return menu;
    }

    public void setMenu(List<Menu> menu) {
        this.menu = menu;
    }

}
