package com.windy.cafemanagement.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.windy.cafemanagement.models.TableBookingDetail;

@Repository
public interface TableBookingDetailRepository extends JpaRepository<TableBookingDetail, Long> {
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
