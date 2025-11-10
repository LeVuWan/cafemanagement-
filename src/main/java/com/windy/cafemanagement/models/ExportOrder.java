package com.windy.cafemanagement.models;

import java.time.LocalDate;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

/**
 * ExportOrder class
 *
 * Version 1.0
 *
 * Date: 10-11-2025
 *
 * Copyright 
 *
 * Modification Logs:
 * DATE                 AUTHOR          DESCRIPTION
 * -----------------------------------------------------------------------
 * 10-11-2025         VuLQ            Create
 */
@Entity
public class ExportOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long exportOrderId;

    @ManyToOne
    @JoinColumn(name = "employeeId")
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "productId")
    private Product product;

    private Double totalExportAmount;
    private LocalDate exportDate;
    private Integer quantity;
    private Boolean isDeleted;

    public ExportOrder() {
    }

    public ExportOrder(Long exportOrderId, Employee employee, Product product, Double totalExportAmount,
            LocalDate exportDate, Integer quantity, Boolean isDeleted) {
        this.exportOrderId = exportOrderId;
        this.employee = employee;
        this.product = product;
        this.totalExportAmount = totalExportAmount;
        this.exportDate = exportDate;
        this.quantity = quantity;
        this.isDeleted = isDeleted;
    }

    public Long getExportOrderId() {
        return exportOrderId;
    }

    public void setExportOrderId(Long exportOrderId) {
        this.exportOrderId = exportOrderId;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Double getTotalExportAmount() {
        return totalExportAmount;
    }

    public void setTotalExportAmount(Double totalExportAmount) {
        this.totalExportAmount = totalExportAmount;
    }

    public LocalDate getExportDate() {
        return exportDate;
    }

    public void setExportDate(LocalDate exportDate) {
        this.exportDate = exportDate;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

}