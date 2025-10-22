package com.windy.cafemanagement.controllers;

import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.windy.cafemanagement.Services.MenuService;
import com.windy.cafemanagement.Services.ProductService;
import com.windy.cafemanagement.dto.MenuDTO;
import com.windy.cafemanagement.models.Menu;


@Controller
@RequestMapping("/admin/menu")
public class MenuController {
    private final ProductService productService;
    private final MenuService menuService;

    public MenuController(ProductService productService, MenuService menuService) {
        this.productService = productService;
        this.menuService = menuService;
    }

    @GetMapping("")
    public String getListMenuController(Model model, @RequestParam(required = false) String keyword) {
        List<Menu> menus = menuService.getAllMenuService(keyword);
        model.addAttribute("menus", menus);
        return "/admin/menu/list-menu";
    }

    @GetMapping("create")
    public String getCreateMenuForm(Model model) {
        model.addAttribute("products", productService.getAllProduct());
        return "admin/menu/create-menu";
    }

    @PostMapping("create")
    @ResponseBody
    public String createMenuController(Model model, @RequestBody MenuDTO menuDTO) {
        menuService.createMenuService(menuDTO);
        return "/admin/menu";
    }
}
