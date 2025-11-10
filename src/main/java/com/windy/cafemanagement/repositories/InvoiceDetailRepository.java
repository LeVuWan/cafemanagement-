package com.windy.cafemanagement.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.windy.cafemanagement.Responses.InfoMenuRes;
import com.windy.cafemanagement.models.InvoiceDetail;

/**
 * InvoiceDetailRepository interface
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
 * 11-10-2025 VuLQ Create
 */
public interface InvoiceDetailRepository extends JpaRepository<InvoiceDetail, Long> {

  /**
   * get invoice detail by invoice id and menu id and isDeleted false
   * 
   * @param InvoiceId
   * @param menuId
   * @return InvoiceDetail
   */
  InvoiceDetail findByInvoice_InvoiceIdAndMenu_MenuIdAndIsDeletedFalse(Long InvoiceId, Long menuId);

  /**
   * get menu info by invoice id, isDeleted false and status 1
   * 
   * @param InvoiceId
   * @return List<InfoMenuRes>
   */
  @Query("""
          SELECT new com.windy.cafemanagement.Responses.InfoMenuRes(m.menuId, m.dishName, id.quantity, id.totalPrice)
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

  /**
   * get all invoice details by invoice id and isDeleted false
   * 
   * @param invoiceId
   * @return List<InvoiceDetail>
   */
  List<InvoiceDetail> findAllByInvoice_InvoiceIdAndIsDeletedFalse(Long invoiceId);
}
