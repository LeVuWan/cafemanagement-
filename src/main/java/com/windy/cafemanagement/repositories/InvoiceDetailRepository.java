package com.windy.cafemanagement.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.windy.cafemanagement.Responses.InfoMenuRes;
import com.windy.cafemanagement.models.InvoiceDetail;

public interface InvoiceDetailRepository extends JpaRepository<InvoiceDetail, Long> {
  InvoiceDetail findByInvoice_InvoiceIdAndMenu_MenuIdAndIsDeletedFalse(Long InvoiceId, Long menuId);

  @Query("""
          SELECT new com.windy.cafemanagement.Responses.InfoMenuRes(m.menuId, m.dishName, id.quantity)
          FROM InvoiceDetail id
          JOIN id.menu m
          JOIN id.invoice i
          WHERE i.invoiceId = :invoiceId
            AND i.status = 1
            AND i.isDeleted = false
            AND id.isDeleted = false
            AND m.isDeleted = false
      """)
  List<InfoMenuRes> findMenuAndQuantityByInvoiceId(@Param("invoiceId") Long invoiceId);

  List<InvoiceDetail> findAllByInvoice_InvoiceIdAndIsDeletedFalse(Long invoiceId);
}
