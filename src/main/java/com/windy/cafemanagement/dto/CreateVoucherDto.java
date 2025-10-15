package com.windy.cafemanagement.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CreateVoucherDto {
    @NotBlank(message = "Voucher name cannot be blank")
    private String voucherName;

    @FutureOrPresent(message = "Start date cannot be in the past")
    private LocalDate startDate;

    @Future(message = "End date must be in the future")
    private LocalDate endDate;

    @NotNull(message = "Discount value cannot be null")
    @DecimalMin(value = "0.0", inclusive = false, message = "Discount must be greater than 0")
    @DecimalMax(value = "100.0", inclusive = false, message = "Discount must be less than 100")
    private Double discountValue;

    public CreateVoucherDto(String voucherName, LocalDate startDate, LocalDate endDate, Double discountValue) {
        this.voucherName = voucherName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.discountValue = discountValue;
    }

    public CreateVoucherDto() {
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

}
