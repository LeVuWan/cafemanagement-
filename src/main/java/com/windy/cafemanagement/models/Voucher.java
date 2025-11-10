package com.windy.cafemanagement.models;

import java.time.LocalDate;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import com.windy.cafemanagement.enums.VoucherStatus;

/**
 * Voucher class
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
public class Voucher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long voucherId;
    private String voucherName;
    private LocalDate startDate;
    private LocalDate endDate;
    private Double discountValue;
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private VoucherStatus status;
    private Boolean isDeleted = false;

    public Voucher() {
    }

    public Voucher(Long voucherId, String voucherName, LocalDate startDate, LocalDate endDate,
            Double discountValue, VoucherStatus status, Boolean isDeleted) {
        this.voucherId = voucherId;
        this.voucherName = voucherName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.discountValue = discountValue;
        this.status = status;
        this.isDeleted = isDeleted;
    }

    public Long getVoucherId() {
        return voucherId;
    }

    public void setVoucherId(Long voucherId) {
        this.voucherId = voucherId;
    }

    public String getVoucherName() {
        return voucherName;
    }

    public void setVoucherName(String voucherName) {
        this.voucherName = voucherName;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Double getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(Double discountValue) {
        this.discountValue = discountValue;
    }

    public VoucherStatus getStatus() {
        return status;
    }

    public void setStatus(VoucherStatus status) {
        this.status = status;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    @Override
    public String toString() {
        return "Voucher [voucherId=" + voucherId + ", voucherName=" + voucherName + ", startDate=" + startDate
                + ", endDate=" + endDate + ", discountValue=" + discountValue + ", status=" + status + ", isDeleted="
                + isDeleted + "]";
    }

}
