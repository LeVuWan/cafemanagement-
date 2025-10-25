package com.windy.cafemanagement.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.windy.cafemanagement.Responses.InformationTableRes;
import com.windy.cafemanagement.Services.MenuService;
import com.windy.cafemanagement.Services.TableService;
import com.windy.cafemanagement.dto.ChooseMenuDto;
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
    public ResponseEntity<?> handleOrderTable(@RequestBody OrderTableDto orderTableDto) {
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
        try {
            List<Menu> menus = menuService.getAllMenuService("");
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

    @PostMapping("choose-menu")
    @ResponseBody
    public ResponseEntity<?> chooseMenuController(@RequestBody ChooseMenuDto chooseMenuDto) {
        try {
            tableService.chooseMenuService(chooseMenuDto);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Chọn thực đơn thành công"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "status", "error",
                    "message", "Đặt bàn thất bại: " + e.getMessage()));
        }
    }

    @GetMapping("get-information-table/{id}")
    public ResponseEntity<?> getInformationTable(@PathVariable("id") Long tableId) {
        System.out.println("Check tableId " + tableId);
        try {
            InformationTableRes data = tableService.getInformantionTableService(tableId);
            System.out.println("Check data: " + data.toString());
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "data", data,
                    "message", "Lấy thức đơn thành công"));
        } catch (Exception e) {
            System.out.println("Check error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "status", "error",
                    "message", "Xem thông tin bàn thất bại: " + e.getMessage()));
        }
    }
}
