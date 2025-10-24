package com.windy.cafemanagement.models;

import java.time.LocalDate;

import com.windy.cafemanagement.enums.InvoiceStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "invoice")
public class Invoice {
    @Id
    @Column(name = "invoice_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long invoiceId;
    private Double totalAmount;
    private LocalDate transactionDate;
    private InvoiceStatus status;
    private Boolean isDeleted;

    @ManyToOne
    @JoinColumn(name = "voucherId")
    private Voucher voucher;

    public Invoice() {
    }

    public Invoice(Long invoiceId, Double totalAmount, LocalDate transactionDate, InvoiceStatus status, Voucher voucher,
            Boolean isDeleted) {
        this.invoiceId = invoiceId;
        this.totalAmount = totalAmount;
        this.transactionDate = transactionDate;
        this.status = status;
        this.voucher = voucher;
        this.isDeleted = isDeleted;
    }

    public Long getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(Long invoiceId) {
        this.invoiceId = invoiceId;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
    }

    public InvoiceStatus getStatus() {
        return status;
    }

    public void setStatus(InvoiceStatus status) {
        this.status = status;
    }

    public Voucher getVoucher() {
        return voucher;
    }

    public void setVoucher(Voucher voucher) {
        this.voucher = voucher;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

}