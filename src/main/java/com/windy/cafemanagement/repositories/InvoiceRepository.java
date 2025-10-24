package com.windy.cafemanagement.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.windy.cafemanagement.enums.InvoiceStatus;
import com.windy.cafemanagement.models.Invoice;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    @Query("""
                SELECT tbd.invoice
                FROM TableBookingDetail tbd
                WHERE tbd.table.tableId = :tableId
                  AND tbd.isDeleted = false
                  AND tbd.invoice.isDeleted = false
                  AND tbd.invoice.status IN (:statuses)
            """)
    Optional<Invoice> findCurrentUnpaidInvoiceByTableId(
            @Param("tableId") Long tableId,
            @Param("statuses") List<InvoiceStatus> statuses);
}
