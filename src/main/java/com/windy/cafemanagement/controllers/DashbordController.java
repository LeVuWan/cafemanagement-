package com.windy.cafemanagement.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Dashbord Controller
 *
 * Version 1.0
 *
 * Date: 11-10-2025
 *
 * Copyright
 *
 * Modification Logs:
 * DATE AUTHOR DESCRIPTION
 * -----------------------------------------------------------------------
 * 11-10-2025 VuLQ dashbord controller
 */

@Controller
public class DashbordController {

    /**
     * Get dashbord page
     * 
     * @return String
     */
    @GetMapping("/admin")
    public String getDashbordPageController() {
        return "admin/dashbord/dashbord";
    }
}
