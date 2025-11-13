package com.windy.cafemanagement.repositories;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.windy.cafemanagement.enums.InvoiceStatus;
import com.windy.cafemanagement.enums.TableStatus;
import com.windy.cafemanagement.models.TableBookingDetail;
import com.windy.cafemanagement.models.TableBookingDetailId;

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
public interface TableBookingDetailRepository extends JpaRepository<TableBookingDetail, TableBookingDetailId> {

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
   * @param tableStatus
   * @return Optional<TableBookingDetail>
   */
  @Query("""
      SELECT tbd
      FROM TableBookingDetail tbd
      WHERE tbd.table.tableId = :tableId
        AND tbd.isDeleted = FALSE
        AND tbd.table.isDeleted = FALSE
        AND tbd.invoice.isDeleted = FALSE
        AND tbd.invoice.status IN :invoiceStatuses
        AND tbd.table.status = :tableStatus
      """)
  Optional<TableBookingDetail> findCurrentActiveByTableId(
      @Param("tableId") Long tableId,
      @Param("invoiceStatuses") List<InvoiceStatus> invoiceStatuses,
      @Param("tableStatus") TableStatus tableStatus);
}
