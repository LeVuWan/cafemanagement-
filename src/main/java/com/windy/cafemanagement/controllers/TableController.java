package com.windy.cafemanagement.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.windy.cafemanagement.Services.TableService;

@Controller
@RequestMapping("admin/table")
public class TableController {
    private final TableService tableService;

    public TableController(TableService tableService) {
        this.tableService = tableService;
    }

    @GetMapping("")
    public String getListTableController(Model model) {
        model.addAttribute("tables", tableService.getAllTableService());
        return "admin/table/list-table";
    }
}
