package com.windy.cafemanagement.repositories;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.windy.cafemanagement.enums.InvoiceStatus;
import com.windy.cafemanagement.models.Invoice;

/**
 * InvoiceRepository interface
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
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

        /**
         * get current unpaid invoice by table id
         * 
         * @param tableId
         * @param statuses
         * @return Optional<Invoice>
         */
        @Query("""
                              SELECT tbd.invoice
                        FROM TableBookingDetail tbd
                        WHERE tbd.table.tableId = :tableId
                          AND tbd.isDeleted = false
                          AND tbd.invoice.isDeleted = false
                          AND tbd.invoice.status IN (:statuses)
                        ORDER BY tbd.bookingTime DESC
                          """)
        Optional<Invoice> findCurrentUnpaidInvoiceByTableId(
                        @Param("tableId") Long tableId,
                        @Param("statuses") List<InvoiceStatus> statuses);

        /**
         * get daily income and expense between start date and end date
         * 
         * @param startDate
         * @param endDate
         * @return List<Object[]>
         */
        @Query(value = "SELECT " +
                        "   t.date AS date, " +
                        "   SUM(t.thu) AS totalIncome, " +
                        "   SUM(t.chi) AS totalExpense " +
                        "FROM ( " +
                        "   SELECT " +
                        "       i.transaction_date AS date, " +
                        "       SUM(i.total_amount) AS thu, " +
                        "       0 AS chi " +
                        "   FROM invoice i " +
                        "   WHERE i.is_deleted = 0 " +
                        "     AND i.status = 3 " +
                        "     AND (:startDate IS NULL OR i.transaction_date >= :startDate) " +
                        "     AND (:endDate IS NULL OR i.transaction_date <= :endDate) " +
                        "   GROUP BY i.transaction_date " +

                        "   UNION ALL " +

                        "   SELECT " +
                        "       io.import_date AS date, " +
                        "       0 AS thu, " +
                        "       SUM(io.total_amount) AS chi " +
                        "   FROM import_order io " +
                        "   WHERE io.is_deleted = 0 " +
                        "     AND (:startDate IS NULL OR io.import_date >= :startDate) " +
                        "     AND (:endDate IS NULL OR io.import_date <= :endDate) " +
                        "   GROUP BY io.import_date " +

                        "   UNION ALL " +

                        "   SELECT " +
                        "       e.expense_date AS date, " +
                        "       0 AS thu, " +
                        "       SUM(e.amount) AS chi " +
                        "   FROM expense e " +
                        "   WHERE e.is_delete = 0 " +
                        "     AND (:startDate IS NULL OR e.expense_date >= :startDate) " +
                        "     AND (:endDate IS NULL OR e.expense_date <= :endDate) " +
                        "   GROUP BY e.expense_date " +
                        ") t " +
                        "GROUP BY t.date " +
                        "ORDER BY t.date", nativeQuery = true)
        List<Object[]> getDailyIncomeExpenseByDate(
                        @Param("startDate") LocalDate startDate,
                        @Param("endDate") LocalDate endDate);

        /**
         * get invoice totals by date between start date and end date
         * 
         * @param startDate
         * @param endDate
         * @return List<Object[]>
         */
        @Query(value = """
                        SELECT
                            transaction_date AS date,
                            SUM(total_amount) AS totalAmount
                        FROM invoice
                        WHERE is_deleted = 0
                          AND status = 3
                          AND transaction_date BETWEEN :startDate AND :endDate
                        GROUP BY transaction_date
                        ORDER BY transaction_date ASC
                        """, nativeQuery = true)
        List<Object[]> getInvoiceTotalsByDate(
                        @Param("startDate") LocalDate startDate,
                        @Param("endDate") LocalDate endDate);

        /**
         * get daily income and expense without date filter
         * 
         * @return List<Object[]>
         */
        @Query(value = "SELECT " +
                        "   t.date AS date, " +
                        "   SUM(t.thu) AS totalIncome, " +
                        "   SUM(t.chi) AS totalExpense " +
                        "FROM ( " +
                        "   SELECT " +
                        "       i.transaction_date AS date, " +
                        "       SUM(i.total_amount) AS thu, " +
                        "       0 AS chi " +
                        "   FROM invoice i " +
                        "   WHERE i.is_deleted = 0 " +
                        "     AND i.status = 3 " +
                        "   GROUP BY i.transaction_date " +

                        "   UNION ALL " +

                        "   SELECT " +
                        "       io.import_date AS date, " +
                        "       0 AS thu, " +
                        "       SUM(io.total_amount) AS chi " +
                        "   FROM import_order io " +
                        "   WHERE io.is_deleted = 0 " +
                        "   GROUP BY io.import_date " +

                        "   UNION ALL " +

                        "   SELECT " +
                        "       e.expense_date AS date, " +
                        "       0 AS thu, " +
                        "       SUM(e.amount) AS chi " +
                        "   FROM expense e " +
                        "   WHERE e.is_delete = 0 " +
                        "   GROUP BY e.expense_date " +
                        ") t " +
                        "GROUP BY t.date " +
                        "ORDER BY t.date", nativeQuery = true)
        List<Object[]> getDailyIncomeExpense();
}
