package com.windy.cafemanagement.dto;

import java.util.List;

public class CutTableDto {
    private Long fromTableId;
    private Long toTableId;
    private List<Menus> menus;

    public CutTableDto() {
    }

    public CutTableDto(Long fromTableId, Long toTableId, List<Menus> menus) {
        this.fromTableId = fromTableId;
        this.toTableId = toTableId;
        this.menus = menus;
    }

    public Long getFromTableId() {
        return fromTableId;
    }

    public void setFromTableId(Long fromTableId) {
        this.fromTableId = fromTableId;
    }

    @Override
    public String toString() {
        return "CutTableDto [fromTableId=" + fromTableId + ", toTableId=" + toTableId + ", menus=" + menus + "]";
    }

    public Long getToTableId() {
        return toTableId;
    }

    public void setToTableId(Long toTableId) {
        this.toTableId = toTableId;
    }

    public List<Menus> getMenus() {
        return menus;
    }

    public void setMenus(List<Menus> menus) {
        this.menus = menus;
    }

}
