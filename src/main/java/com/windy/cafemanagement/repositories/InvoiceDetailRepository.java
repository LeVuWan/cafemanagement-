package com.windy.cafemanagement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.windy.cafemanagement.models.InvoiceDetail;

public interface InvoiceDetailRepository extends JpaRepository<InvoiceDetail, Long> {
    InvoiceDetail findByInvoice_InvoiceIdAndMenu_MenuIdAndIsDeletedFalse(Long InvoiceId, Long menuId);

}
