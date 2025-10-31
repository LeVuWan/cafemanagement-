package com.windy.cafemanagement.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("admin/bubget")
public class budgetController {
    @GetMapping("")
    public String getListBubgetController() {
        return "admin/budget/list_bubget";
    }
}
