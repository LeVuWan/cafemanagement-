package com.windy.cafemanagement.configs;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.windy.cafemanagement.Services.EmployeeService;

/**
 * DataInitializer class
 *
 * Version 1.0
 *
 * Date: 10-11-2025
 *
 * Copyright
 *
 * Modification Logs:
 * DATE AUTHOR DESCRIPTION
 * -----------------------------------------------------------------------
 * 14-11-2025 VuLQ Create
 */
@Component
public class DataInitializer implements ApplicationRunner {
    private final EmployeeService employeeService;

    public DataInitializer(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        employeeService.initializeDataService();
    }
}
