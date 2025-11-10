package com.windy.cafemanagement.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.windy.cafemanagement.models.TableBookingDetail;

/**
 * TableBookingDetailRepository interface
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
@Repository
public interface TableBookingDetailRepository extends JpaRepository<TableBookingDetail, Long> {

  /**
   * get table booking detail by table id and tableId and invoiceId
   * 
   * @param tableId
   * @param invoiceId
   * @return Optional<TableBookingDetail>
   */
  @Query("""
          SELECT tbd
          FROM TableBookingDetail tbd
          WHERE tbd.table.tableId = :tableId
            AND tbd.invoice.invoiceId = :invoiceId
            AND tbd.isDeleted = false
            AND tbd.table.isDeleted = false
            AND tbd.invoice.isDeleted = false
      """)
  Optional<TableBookingDetail> findActiveByTableIdAndInvoiceId(@Param("tableId") Long tableId,
      @Param("invoiceId") Long invoiceId);

  /**
   * get table booking detail by table id
   * 
   * @param tableId
   * @param invoiceId
   * @return Optional<TableBookingDetail>
   */
  @Query("""
          SELECT tbd
          FROM TableBookingDetail tbd
          WHERE tbd.table.tableId = :tableId
            AND tbd.isDeleted = false
            AND tbd.table.isDeleted = false
            AND tbd.invoice.isDeleted = false
            AND tbd.invoice.status = com.windy.cafemanagement.enums.InvoiceStatus.UPDATED
      """)
  Optional<TableBookingDetail> findCurrentActiveByTableId(@Param("tableId") Long tableId);
}
