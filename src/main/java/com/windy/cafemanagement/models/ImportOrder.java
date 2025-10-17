package com.windy.cafemanagement.models;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class ImportOrder {
    @Id
    private Long importOrderId;

    @ManyToOne
    @JoinColumn(name = "employeeId")
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "productId")
    private Product product;

    @ManyToOne
    @JoinColumn
    private Equipment equipment;

    private LocalDate importDate;
    private Double totalAmount;
    private Integer quantity;

    public ImportOrder() {
    }

    public ImportOrder(Long importOrderId, Employee employee, Product product, Equipment equipment,
            LocalDate importDate, Double totalAmount, Integer quantity) {
        this.importOrderId = importOrderId;
        this.employee = employee;
        this.product = product;
        this.equipment = equipment;
        this.importDate = importDate;
        this.totalAmount = totalAmount;
        this.quantity = quantity;
    }

    public Long getImportOrderId() {
        return importOrderId;
    }

    public void setImportOrderId(Long importOrderId) {
        this.importOrderId = importOrderId;
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

    public Equipment getEquipment() {
        return equipment;
    }

    public void setEquipment(Equipment equipment) {
        this.equipment = equipment;
    }

    public LocalDate getImportDate() {
        return importDate;
    }

    public void setImportDate(LocalDate importDate) {
        this.importDate = importDate;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

}
