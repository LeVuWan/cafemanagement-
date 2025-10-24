package com.windy.cafemanagement.dto;

import java.util.List;

public class ChooseMenuDto {
    private Long tableId;
    private List<Menus> menus;

    public ChooseMenuDto() {
    }

    public ChooseMenuDto(Long tableId, List<Menus> menus) {
        this.tableId = tableId;
        this.menus = menus;
    }

    public Long getTableId() {
        return tableId;
    }

    public void setTableId(Long tableId) {
        this.tableId = tableId;
    }

    public List<Menus> getMenus() {
        return menus;
    }

    public void setMenus(List<Menus> menus) {
        this.menus = menus;
    }

}
