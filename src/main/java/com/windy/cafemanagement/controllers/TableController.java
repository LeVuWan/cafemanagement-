package com.windy.cafemanagement.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("admin/table")
public class TableController {
    @GetMapping("")
    public String getListTableController(Model model) {
        return "admin/table/list-table";
    }
}

