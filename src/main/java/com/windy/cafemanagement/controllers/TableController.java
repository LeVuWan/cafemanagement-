package com.windy.cafemanagement.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.windy.cafemanagement.Services.MenuService;
import com.windy.cafemanagement.Services.TableService;
import com.windy.cafemanagement.dto.OrderTableDto;
import com.windy.cafemanagement.models.Menu;
import com.windy.cafemanagement.models.TableEntity;

@Controller
@RequestMapping("admin/table")
public class TableController {
    private final TableService tableService;
    private final MenuService menuService;

    public TableController(TableService tableService, MenuService menuService) {
        this.tableService = tableService;
        this.menuService = menuService;
    }

    @GetMapping("")
    public String getListTableController(Model model) {
        List<TableEntity> tableEntities = tableService.getAllTableService();
        model.addAttribute("tables", tableEntities);
        return "admin/table/list-table";
    }

    @PostMapping("order")
    @ResponseBody
    public ResponseEntity<?> handleOrderTable(Model model, @RequestBody OrderTableDto orderTableDto) {
        try {
            tableService.orderTableService(orderTableDto);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Đặt bàn thành công!"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "status", "error",
                    "message", "Đặt bàn thất bại: " + e.getMessage()));
        }
    }

    @GetMapping("get-menu")
    public ResponseEntity<?> getMenuAllMenu() {
        System.out.println("Run here");
        try {
            List<Menu> menus = menuService.getAllMenuService("");
            System.out.println("Check: " + menus.size());
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "data", menus,
                    "message", "Lấy thức đơn thành công"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "status", "error",
                    "message", "Đặt bàn thất bại: " + e.getMessage()));
        }
    }
}
