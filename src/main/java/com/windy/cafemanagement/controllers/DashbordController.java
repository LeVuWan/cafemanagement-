package com.windy.cafemanagement.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashbordController {
    @GetMapping("/admin")
    public String getDashbordPageController() {
        return "admin/dashbord/dashbord";
    }
}
