package com.windy.cafemanagement.controllers;

import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import jakarta.persistence.EntityNotFoundException;

import com.windy.cafemanagement.Services.MenuService;
import com.windy.cafemanagement.Services.ProductService;
import com.windy.cafemanagement.dto.MenuDTO;
import com.windy.cafemanagement.models.Menu;
import com.windy.cafemanagement.models.MenuDetail;
import com.windy.cafemanagement.models.Product;

@Controller
@RequestMapping("/admin/menu")
/**
 * MenuController
 *
 * Version 1.0
 *
 * Date: 12-11-2013
 *
 * Copyright
 *
 * Modification Logs:
 * DATE AUTHOR DESCRIPTION
 * -----------------------------------------------------------------------
 */
public class MenuController {
    private static final Logger logger = LoggerFactory.getLogger(MenuController.class);

    private final ProductService productService;
    private final MenuService menuService;

    public MenuController(ProductService productService, MenuService menuService) {
        this.productService = productService;
        this.menuService = menuService;
    }

    /**
     * Show the menu listing page.
     * 
     * @param model   the model for the view
     * @param keyword optional search keyword
     * @return view name for menu list
     * @throws Exception when retrieval fails
     */
    @GetMapping("")
    public String getListMenuController(Model model, @RequestParam(required = false) String keyword) {
        try {
            List<Menu> menus = menuService.getAllMenuService(keyword);
            model.addAttribute("menus", menus);
            return "/admin/menu/list-menu";
        } catch (Exception ex) {
            logger.error("Unexpected error while fetching menus: {}", ex.getMessage(), ex);
            model.addAttribute("errorMessage", "Lấy danh sách menu thất bại: " + ex.getMessage());
            return "admin/errors/500-error";
        }
    }

    /**
     * Show the create-menu form.
     * 
     * @param model the model for the view
     * @return view name for creating a menu
     * @throws Exception when loading products fails
     */
    @GetMapping("create")
    public String getCreateMenuForm(Model model) {
        try {
            model.addAttribute("products", productService.getAllProduct());
            return "admin/menu/create-menu";
        } catch (Exception ex) {
            logger.error("Unexpected error while loading create menu form: {}", ex.getMessage(), ex);
            model.addAttribute("errorMessage", "Không thể mở form tạo menu: " + ex.getMessage());
            return "admin/errors/500-error";
        }
    }

    /**
     * Create a new menu (API).
     * 
     * @param menuDTO the menu payload
     * @return JSON response with status and redirect path
     */
    @PostMapping("create")
    @ResponseBody
    public ResponseEntity<?> createMenuController(@RequestBody MenuDTO menuDTO) {
        try {
            menuService.createMenuService(menuDTO);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "redirect", "/admin/menu",
                    "message", "Tạo menu thành công"));
        } catch (IllegalArgumentException | NullPointerException ex) {
            logger.error("Invalid menu data: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "status", "error",
                    "message", "Dữ liệu không hợp lệ: " + ex.getMessage()));
        } catch (Exception ex) {
            logger.error("Unexpected error while creating menu: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "error",
                    "message", "Tạo menu thất bại: " + ex.getMessage()));
        }
    }

    /**
     * Show the edit-menu form for the given id.
     * 
     * @param model the model for the view
     * @param id    the menu id
     * @return view name for editing a menu
     * @throws Exception when loading menu data fails
     */
    @GetMapping("edit/{id}")
    public String getEditMenuForm(Model model, @PathVariable("id") Long id) {
        try {
            Menu menu = menuService.getMenuByIdService(id);
            List<MenuDetail> menuDetails = menuService.getListMenuDetailByMenu(id);
            List<Product> products = productService.getAllProduct();

            model.addAttribute("menu", menu);
            model.addAttribute("menuDetails", menuDetails);
            model.addAttribute("products", products);
            return "admin/menu/edit-menu";
        } catch (EntityNotFoundException ex) {
            logger.error("Menu not found for id {}: {}", id, ex.getMessage(), ex);
            model.addAttribute("errorMessage", "Không tìm thấy menu: " + ex.getMessage());
            return "admin/errors/500-error";
        } catch (Exception ex) {
            logger.error("Unexpected error while loading edit menu form for id {}: {}", id, ex.getMessage(), ex);
            model.addAttribute("errorMessage", "Không thể mở form chỉnh sửa menu: " + ex.getMessage());
            return "admin/errors/500-error";
        }
    }

    /**
     * Update an existing menu (API).
     * 
     * @param menuDTO the updated menu payload
     * @return JSON response with status and redirect path
     */
    @PostMapping("edit")
    @ResponseBody
    public ResponseEntity<?> editMenu(@RequestBody MenuDTO menuDTO) {
        try {
            menuService.updateMenuService(menuDTO);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "redirect", "/admin/menu",
                    "message", "Cập nhật menu thành công"));
        } catch (EntityNotFoundException ex) {
            logger.error("Menu not found while updating: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "status", "error",
                    "message", "Không tìm thấy menu: " + ex.getMessage()));
        } catch (IllegalArgumentException | NullPointerException ex) {
            logger.error("Invalid data while updating menu: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "status", "error",
                    "message", "Dữ liệu không hợp lệ: " + ex.getMessage()));
        } catch (Exception ex) {
            logger.error("Unexpected error while updating menu: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "error",
                    "message", "Cập nhật menu thất bại: " + ex.getMessage()));
        }
    }

    /**
     * Delete a menu and redirect to the menu list.
     * 
     * @param model the model for the view
     * @param id    the menu id to delete
     * @return redirect to menu list or error view on failure
     */
    @GetMapping("delete/{id}")
    public String deleteMenu(Model model, @PathVariable("id") Long id) {
        try {
            menuService.deleteMenuById(id);
            return "redirect:/admin/menu";
        } catch (EntityNotFoundException ex) {
            logger.error("Menu not found for delete id {}: {}", id, ex.getMessage(), ex);
            model.addAttribute("errorMessage", "Không tìm thấy menu để xóa: " + ex.getMessage());
            return "admin/errors/500-error";
        } catch (Exception ex) {
            logger.error("Unexpected error while deleting menu id {}: {}", id, ex.getMessage(), ex);
            model.addAttribute("errorMessage", "Xóa menu thất bại: " + ex.getMessage());
            return "admin/errors/500-error";
        }
    }
}
