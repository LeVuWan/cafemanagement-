package com.windy.cafemanagement.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.windy.cafemanagement.models.Invoice;

public interface FinancialSummaryRepository extends JpaRepository<Invoice, Long> {
    @Query(value = """
                      SELECT
                          d.`date` AS `date`,
                          IFNULL(b.totalIncome, 0) AS totalIncome,
                          (IFNULL(i.totalImportCost, 0) + IFNULL(e.totalExpense, 0)) AS totalExpense
                      FROM (
                          SELECT DATE(transaction_date) AS `date` FROM invoice
                          UNION
                          SELECT DATE(import_date) FROM import_order
                          UNION
                          SELECT DATE(expense_date) FROM expense
                      ) d
                      LEFT JOIN (
                          SELECT
                              DATE(transaction_date) AS `date`,
                              SUM(total_amount) AS totalIncome
                          FROM invoice
                          WHERE status = 3
                          GROUP BY DATE(transaction_date)
                      ) b ON d.`date` = b.`date`
                      LEFT JOIN (
                          SELECT
                              DATE(import_date) AS `date`,
                              SUM(total_amount) AS totalImportCost
                          FROM import_order
                          GROUP BY DATE(import_date)
                      ) i ON d.`date` = i.`date`
                      LEFT JOIN (
                          SELECT
                              DATE(expense_date) AS `date`,
                              SUM(amount) AS totalExpense
                          FROM expense
                          GROUP BY DATE(expense_date)
                      ) e ON d.`date` = e.`date`
                      WHERE d.`date` BETWEEN :startDate AND :endDate
                      AND (
                        IFNULL(b.totalIncome, 0) > 0
                        OR IFNULL(i.totalImportCost, 0) > 0
                        OR IFNULL(e.totalExpense, 0) > 0
                         )
                      ORDER BY d.`date`
                      """, nativeQuery = true)
    List<Object[]> getFinancialSummaryRaw(@Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
}
