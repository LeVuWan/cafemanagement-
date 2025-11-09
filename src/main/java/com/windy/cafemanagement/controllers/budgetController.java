package com.windy.cafemanagement.controllers;

import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.windy.cafemanagement.Responses.GenaralReportRes;
import com.windy.cafemanagement.Services.ExpenseService;
import com.windy.cafemanagement.Services.ReportService;


@Controller
@RequestMapping("admin/bubget")
public class BudgetController {
    private final ReportService reportService;


    public BudgetController(ReportService reportService, ExpenseService expenseService) {
        this.reportService = reportService;
       
    }

    @GetMapping("")
    public String getListBubgetController(Model model) {
        List<GenaralReportRes> reports = reportService.generalReportServiceForBudget();

        model.addAttribute("reports", reports);

        return "admin/budget/list_bubget";
    }

    

}
