package com.windy.cafemanagement.dto;

import java.util.List;

/**
 * cut table dto class
 *
 * Version 1.0
 *
 * Date: 10-11-2025
 *
 * Copyright
 *
 * Modification Logs:
 * DATE AUTHOR DESCRIPTION
 * -----------------------------------------------------------------------
 * 10-11-2025 VuLQ Create
 */
public class CutTableDto {
    private Long fromTableId;
    private Long toTableId;
    private List<Menu> menu;

    public CutTableDto() {
    }

    public CutTableDto(Long fromTableId, Long toTableId, List<Menu> menu) {
        this.fromTableId = fromTableId;
        this.toTableId = toTableId;
        this.menu = menu;
    }

    public Long getFromTableId() {
        return fromTableId;
    }

    public void setFromTableId(Long fromTableId) {
        this.fromTableId = fromTableId;
    }

    @Override
    public String toString() {
        return "CutTableDto [fromTableId=" + fromTableId + ", toTableId=" + toTableId + ", menu=" + menu + "]";
    }

    public Long getToTableId() {
        return toTableId;
    }

    public void setToTableId(Long toTableId) {
        this.toTableId = toTableId;
    }

    public List<Menu> getMenus() {
        return menu;
    }

    public void setMenus(List<Menu> menu) {
        this.menu = menu;
    }

}
