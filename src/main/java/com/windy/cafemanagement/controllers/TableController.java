package com.windy.cafemanagement.controllers;

import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import jakarta.persistence.EntityNotFoundException;

import com.windy.cafemanagement.Responses.InformationTableRes;
import com.windy.cafemanagement.Responses.TableInforRes;
import com.windy.cafemanagement.Responses.TableToMergeRes;
import com.windy.cafemanagement.Services.MenuService;
import com.windy.cafemanagement.Services.TableService;
import com.windy.cafemanagement.dto.ChooseMenuDto;
import com.windy.cafemanagement.dto.CutTableDto;
import com.windy.cafemanagement.dto.MergeTableDto;
import com.windy.cafemanagement.dto.MoveTableDto;
import com.windy.cafemanagement.dto.OrderTableDto;
import com.windy.cafemanagement.dto.PaymentDto;
import com.windy.cafemanagement.models.Menu;
import com.windy.cafemanagement.models.TableEntity;

@Controller
@RequestMapping("admin/table")
/**
 * TableController
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
public class TableController {
    private static final Logger logger = LoggerFactory.getLogger(TableController.class);

    private final TableService tableService;
    private final MenuService menuService;

    public TableController(TableService tableService, MenuService menuService) {
        this.tableService = tableService;
        this.menuService = menuService;
    }

    /**
     * Display the table listing page.
     * 
     * @param model the Spring MVC model
     * @return the view name for the table list
     * 
     */
    @GetMapping("")
    public String getListTableController(Model model) {
        try {
            List<TableEntity> tableEntities = tableService.getAllTableService();
            model.addAttribute("tables", tableEntities);
            return "admin/table/list-table";
        } catch (Exception ex) {
            logger.error("Unexpected error while listing tables: {}", ex.getMessage(), ex);
            model.addAttribute("errorMessage", "Lấy danh sách bàn thất bại: " + ex.getMessage());
            return "admin/errors/500-error";
        }
    }

    /**
     * Place an order on a table.
     * 
     * @param orderTableDto the order payload
     * @return ResponseEntity containing status and message
     * 
     */
    @PostMapping("order")
    @ResponseBody
    public ResponseEntity<?> handleOrderTable(@RequestBody OrderTableDto orderTableDto) {
        try {
            tableService.orderTableService(orderTableDto);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Đặt bàn thành công!"));
        } catch (EntityNotFoundException ex) {
            logger.error("Order target not found: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "status", "error",
                    "message", "Không tìm thấy mục đặt bàn: " + ex.getMessage()));
        } catch (IllegalArgumentException | NullPointerException ex) {
            logger.error("Invalid order data: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "status", "error",
                    "message", "Dữ liệu đặt bàn không hợp lệ: " + ex.getMessage()));
        } catch (Exception ex) {
            logger.error("Unexpected error while ordering table: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "error",
                    "message", "Đặt bàn thất bại: " + ex.getMessage()));
        }
    }

    /**
     * Retrieve all menu items.
     * 
     * @return ResponseEntity containing the menu list
     * 
     */
    @GetMapping("get-menu")
    public ResponseEntity<?> getMenuAllMenu() {
        try {
            List<Menu> menus = menuService.getAllMenuService("");
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "data", menus,
                    "message", "Lấy thức đơn thành công"));
        } catch (Exception ex) {
            logger.error("Unexpected error while fetching menus: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "error",
                    "message", "Lấy thực đơn thất bại: " + ex.getMessage()));
        }
    }

    /**
     * Choose menu items for a table/order.
     * 
     * @param chooseMenuDto the chosen menu payload
     * @return ResponseEntity containing status and message
     * 
     */
    @PostMapping("choose-menu")
    @ResponseBody
    public ResponseEntity<?> chooseMenuController(@RequestBody ChooseMenuDto chooseMenuDto) {
        try {
            tableService.chooseMenuService(chooseMenuDto);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Chọn thực đơn thành công"));
        } catch (EntityNotFoundException ex) {
            logger.error("Menu or table not found while choosing menu: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "status", "error",
                    "message", "Không tìm thấy mục: " + ex.getMessage()));
        } catch (IllegalArgumentException | NullPointerException ex) {
            logger.error("Invalid data while choosing menu: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "status", "error",
                    "message", "Dữ liệu không hợp lệ: " + ex.getMessage()));
        } catch (Exception ex) {
            logger.error("Unexpected error while choosing menu: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "error",
                    "message", "Chọn thực đơn thất bại: " + ex.getMessage()));
        }
    }

    /**
     * Get detailed information for a table.
     * 
     * @param tableId the id of the table
     * @return ResponseEntity with table information
     * 
     */
    @GetMapping("get-information-table/{id}")
    public ResponseEntity<?> getInformationTable(@PathVariable("id") Long tableId) {
        try {
            InformationTableRes data = tableService.getInformantionTableService(tableId);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "data", data,
                    "message", "Lấy thức đơn thành công"));
        } catch (EntityNotFoundException ex) {
            logger.error("Table information not found for id {}: {}", tableId, ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "status", "error",
                    "message", "Không tìm thấy thông tin bàn: " + ex.getMessage()));
        } catch (IllegalArgumentException | NullPointerException ex) {
            logger.error("Invalid table id {}: {}", tableId, ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "status", "error",
                    "message", "Id bàn không hợp lệ: " + ex.getMessage()));
        } catch (Exception ex) {
            logger.error("Unexpected error while fetching table information for id {}: {}", tableId, ex.getMessage(),
                    ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "error",
                    "message", "Xem thông tin bàn thất bại: " + ex.getMessage()));
        }
    }

    /**
     * Get candidate tables for moving an order.
     * 
     * @param tableId the id of the table to move
     * @return ResponseEntity with the list of candidate tables
     * 
     */
    @GetMapping("get-table-move/{id}")
    public ResponseEntity<?> getTableToMove(@PathVariable("id") Long tableId) {
        try {
            List<TableInforRes> data = tableService.getTableToMoveService(tableId);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "data", data,
                    "message", "Lấy thông tin bàn muốn chuyển thành công"));
        } catch (EntityNotFoundException ex) {
            logger.error("Tables to move not found for id {}: {}", tableId, ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "status", "error",
                    "message", "Không tìm thấy thông tin bàn: " + ex.getMessage()));
        } catch (IllegalArgumentException | NullPointerException ex) {
            logger.error("Invalid table id for move {}: {}", tableId, ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "status", "error",
                    "message", "Id bàn không hợp lệ: " + ex.getMessage()));
        } catch (Exception ex) {
            logger.error("Unexpected error while getting tables to move for id {}: {}", tableId, ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "error",
                    "message", "Lấy thông tin bàn muốn chuyển thất bại: " + ex.getMessage()));
        }
    }

    /**
     * Move an order from one table to another.
     * 
     * @param moveTableDto the move table payload
     * @return ResponseEntity containing status and message
     * 
     */
    @PostMapping("move-table")
    @ResponseBody
    public ResponseEntity<?> moveTableContorller(@RequestBody MoveTableDto moveTableDto) {
        try {
            logger.debug("moveTable called: {}", moveTableDto);
            tableService.moveTableService(moveTableDto);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Chuyển bàn thành công"));
        } catch (EntityNotFoundException ex) {
            logger.error("Target table not found while moving: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "status", "error",
                    "message", "Không tìm thấy bàn mục tiêu: " + ex.getMessage()));
        } catch (IllegalArgumentException | NullPointerException ex) {
            logger.error("Invalid move data: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "status", "error",
                    "message", "Dữ liệu chuyển bàn không hợp lệ: " + ex.getMessage()));
        } catch (Exception ex) {
            logger.error("Unexpected error while moving table: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "error",
                    "message", "Chuyển bàn thất bại: " + ex.getMessage()));
        }
    }

    /**
     * Get data needed to merge tables.
     * 
     * @return ResponseEntity with merge candidates
     * 
     */
    @GetMapping("get-table-merge")
    public ResponseEntity<?> getTableToMerger() {
        try {
            TableToMergeRes data = tableService.getTableToMergeService();
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "data", data,
                    "message", "Lấy thông tin bàn để gộp thành công"));
        } catch (Exception ex) {
            logger.error("Unexpected error while getting tables to merge: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "error",
                    "message", "Lấy thông tin bàn để gộp thất bại: " + ex.getMessage()));
        }
    }

    /**
     * Merge two or more tables together.
     * 
     * @param mergeTableDto the merge payload
     * @return ResponseEntity containing status and message
     * 
     */
    @PostMapping("merge-table")
    @ResponseBody
    public ResponseEntity<?> mergeTableController(@RequestBody MergeTableDto mergeTableDto) {
        try {
            tableService.mergeTableService(mergeTableDto);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Gộp bàn thành công"));
        } catch (EntityNotFoundException ex) {
            logger.error("Table not found while merging: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "status", "error",
                    "message", "Không tìm thấy bàn để gộp: " + ex.getMessage()));
        } catch (IllegalArgumentException | NullPointerException ex) {
            logger.error("Invalid merge data: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "status", "error",
                    "message", "Dữ liệu gộp bàn không hợp lệ: " + ex.getMessage()));
        } catch (Exception ex) {
            logger.error("Unexpected error while merging tables: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "error",
                    "message", "Gộp bàn thất bại: " + ex.getMessage()));
        }
    }

    /**
     * Cancel a table (clear its order/reservation).
     * 
     * @param tableId the id of the table to cancel
     * @return ResponseEntity containing status and message
     * 
     */
    @PostMapping("cancel-table/{id}")
    @ResponseBody
    public ResponseEntity<?> cancelTableController(@PathVariable("id") Long tableId) {
        try {
            tableService.cancelTableService(tableId);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Hủy bàn thành công"));
        } catch (EntityNotFoundException ex) {
            logger.error("Table not found to cancel id {}: {}", tableId, ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "status", "error",
                    "message", "Không tìm thấy bàn để hủy: " + ex.getMessage()));
        } catch (Exception ex) {
            logger.error("Unexpected error while canceling table id {}: {}", tableId, ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "error",
                    "message", "Hủy bàn thất bại: " + ex.getMessage()));
        }
    }

    /**
     * Split/cut a table's order into another table.
     * 
     * @param cutTableDto the cut payload
     * @return ResponseEntity containing status and message
     * 
     */
    @PostMapping("cut-table")
    @ResponseBody
    public ResponseEntity<?> cutTableController(@RequestBody CutTableDto cutTableDto) {
        try {
            tableService.cutTableService(cutTableDto);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Cắt bàn thành công"));
        } catch (EntityNotFoundException ex) {
            logger.error("Table not found while cutting: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "status", "error",
                    "message", "Không tìm thấy bàn: " + ex.getMessage()));
        } catch (IllegalArgumentException | NullPointerException ex) {
            logger.error("Invalid cut data: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "status", "error",
                    "message", "Dữ liệu cắt bàn không hợp lệ: " + ex.getMessage()));
        } catch (Exception ex) {
            logger.error("Unexpected error while cutting table: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "error",
                    "message", "Cắt bàn thất bại: " + ex.getMessage()));
        }
    }

    /**
     * Process payment for a table's order.
     * 
     * @param paymentDto the payment payload
     * @return ResponseEntity containing status and message
     * 
     */
    @PostMapping("payment")
    @ResponseBody
    public ResponseEntity<?> paymentController(@RequestBody PaymentDto paymentDto) {
        try {
            tableService.paymentService(paymentDto);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Thanh toán thành công"));
        } catch (EntityNotFoundException ex) {
            logger.error("Payment target not found: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "status", "error",
                    "message", "Không tìm thấy mục thanh toán: " + ex.getMessage()));
        } catch (IllegalArgumentException | NullPointerException ex) {
            logger.error("Invalid payment data: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "status", "error",
                    "message", "Dữ liệu thanh toán không hợp lệ: " + ex.getMessage()));
        } catch (Exception ex) {
            logger.error("Unexpected error while processing payment: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "error",
                    "message", "Thanh toán thất bại: " + ex.getMessage()));
        }
    }
}
