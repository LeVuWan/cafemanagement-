package com.windy.cafemanagement.repositories;

import java.time.LocalDate;
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

    @Query(value = "SELECT\r\n" +
            "  d.date AS date,\r\n" +
            "  COALESCE(b.totalIncome, 0) AS totalIncome,\r\n" +
            "  (COALESCE(i.totalImportCost, 0) + COALESCE(e.totalExpense, 0)) AS totalExpense\r\n" +
            "FROM (\r\n" +
            "  SELECT DATE(transaction_date) AS date FROM invoice\r\n" +
            "  UNION\r\n" +
            "  SELECT DATE(import_date) FROM import_order\r\n" +
            "  UNION\r\n" +
            "  SELECT DATE(expense_date) FROM expense\r\n" +
            ") d\r\n" +
            "LEFT JOIN (\r\n" +
            "  SELECT DATE(transaction_date) AS date, SUM(total_amount) AS totalIncome\r\n" +
            "  FROM invoice\r\n" +
            "  WHERE status = 3\r\n" +
            "  GROUP BY DATE(transaction_date)\r\n" +
            ") b ON d.date = b.date\r\n" +
            "LEFT JOIN (\r\n" +
            "  SELECT DATE(import_date) AS date, SUM(total_amount) AS totalImportCost\r\n" +
            "  FROM import_order\r\n" +
            "  GROUP BY DATE(import_date)\r\n" +
            ") i ON d.date = i.date\r\n" +
            "LEFT JOIN (\r\n" +
            "  SELECT DATE(expense_date) AS date, SUM(amount) AS totalExpense\r\n" +
            "  FROM expense\r\n" +
            "  GROUP BY DATE(expense_date)\r\n" +
            ") e ON d.date = e.date\r\n" +
            "WHERE\r\n" +
            "  (:startDate IS NULL OR d.date >= :startDate)\r\n" +
            "  AND (:endDate IS NULL OR d.date <= :endDate)\r\n" +
            "  AND (COALESCE(b.totalIncome, 0) > 0 OR COALESCE(i.totalImportCost, 0) > 0 OR COALESCE(e.totalExpense, 0) > 0)\r\n"
            +
            "ORDER BY d.date", nativeQuery = true)
    List<Object[]> getDailyIncomeExpense(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    @Query(value = "SELECT\r\n" +
            "  d.date AS date,\r\n" +
            "  COALESCE(b.totalIncome, 0) AS totalIncome,\r\n" +
            "  (COALESCE(i.totalImportCost, 0) + COALESCE(e.totalExpense, 0)) AS totalExpense\r\n" +
            "FROM (\r\n" +
            "  SELECT DATE(transaction_date) AS date FROM invoice\r\n" +
            "  UNION\r\n" +
            "  SELECT DATE(import_date) FROM import_order\r\n" +
            "  UNION\r\n" +
            "  SELECT DATE(expense_date) FROM expense\r\n" +
            ") d\r\n" +
            "LEFT JOIN (\r\n" +
            "  SELECT DATE(transaction_date) AS date, SUM(total_amount) AS totalIncome\r\n" +
            "  FROM invoice\r\n" +
            "  WHERE status = 3\r\n" +
            "  GROUP BY DATE(transaction_date)\r\n" +
            ") b ON d.date = b.date\r\n" +
            "LEFT JOIN (\r\n" +
            "  SELECT DATE(import_date) AS date, SUM(total_amount) AS totalImportCost\r\n" +
            "  FROM import_order\r\n" +
            "  GROUP BY DATE(import_date)\r\n" +
            ") i ON d.date = i.date\r\n" +
            "LEFT JOIN (\r\n" +
            "  SELECT DATE(expense_date) AS date, SUM(amount) AS totalExpense\r\n" +
            "  FROM expense\r\n" +
            "  GROUP BY DATE(expense_date)\r\n" +
            ") e ON d.date = e.date\r\n" +
            "WHERE\r\n" +
            "  (COALESCE(b.totalIncome, 0) > 0 OR COALESCE(i.totalImportCost, 0) > 0 OR COALESCE(e.totalExpense, 0) > 0)\r\n"
            +
            "ORDER BY d.date", nativeQuery = true)
    List<Object[]> getDailyIncomeExpense();

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
}
