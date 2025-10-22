package com.windy.cafemanagement.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class MenuDTO {
    private Long menuId;
    @NotBlank(message = "Tên thực đơn không để trống")
    private String name;
    @NotNull(message = "Giá không được để trống")
    private Double price;
    private List<MenuIngredientDTO> ingredients;

    public MenuDTO() {
    }

    public MenuDTO(String name, Double price, List<MenuIngredientDTO> ingredients, Long menuId) {
        this.name = name;
        this.price = price;
        this.ingredients = ingredients;
        this.menuId = menuId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public List<MenuIngredientDTO> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<MenuIngredientDTO> ingredients) {
        this.ingredients = ingredients;
    }

    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }

}
