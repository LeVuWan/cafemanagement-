package com.windy.cafemanagement.repositories;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.windy.cafemanagement.models.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
  List<Product> findAllByIsDeleted(Boolean isDeleted);

  Optional<Product> findByProductIdAndIsDeletedFalse(Long productId);

  @Query(value = """
      SELECT
          p.product_name AS nameProduct,
          io.import_date AS importDate,
          NULL AS exportDate,
          io.quantity AS quantity,
          u.unit_name AS unit,
          (p.unit_price * io.quantity) AS totalPrice
      FROM product p
      INNER JOIN import_order io ON p.product_id = io.product_id
      INNER JOIN unit u ON p.unit_id = u.unit_id
      WHERE p.is_deleted = false
        AND io.is_deleted = false
        AND (:keyword IS NULL OR p.product_name LIKE CONCAT('%', :keyword, '%'))

      UNION ALL

      SELECT
          p.product_name AS nameProduct,
          NULL AS importDate,
          eo.export_date AS exportDate,
          eo.quantity AS quantity,
          u.unit_name AS unit,
          (p.unit_price * eo.quantity) AS totalPrice
      FROM product p
      INNER JOIN export_order eo ON p.product_id = eo.product_id
      INNER JOIN unit u ON p.unit_id = u.unit_id
      WHERE p.is_deleted = false
        AND eo.is_deleted = false
        AND (:keyword IS NULL OR p.product_name LIKE CONCAT('%', :keyword, '%'))

      ORDER BY COALESCE(importDate, exportDate)
      """, nativeQuery = true)
  List<Map<String, Object>> findImportExportHistoryNative(@Param("keyword") String keyword);

  @Query(value = "SELECT COALESCE(ImportTotals.import_date, ExportTotals.export_date) AS transactionDate, "
      + "COALESCE(ImportTotals.amountImportTotalOfDay, 0) AS amountImportTotalOfDay, "
      + "COALESCE(ExportTotals.amountExportTotalOfDay, 0) AS amountExportTotalOfDay "
      + "FROM ( SELECT import_date, SUM(total_amount) AS amountImportTotalOfDay FROM import_order GROUP BY import_date ) AS ImportTotals "
      + "LEFT JOIN ( SELECT export_date, SUM(total_export_amount) AS amountExportTotalOfDay FROM export_order GROUP BY export_date ) AS ExportTotals "
      + "ON ImportTotals.import_date = ExportTotals.export_date "
      + "UNION "
      + "SELECT COALESCE(ImportTotals.import_date, ExportTotals.export_date) AS transactionDate, "
      + "COALESCE(ImportTotals.amountImportTotalOfDay, 0) AS amountImportTotalOfDay, "
      + "COALESCE(ExportTotals.amountExportTotalOfDay, 0) AS amountExportTotalOfDay "
      + "FROM ( SELECT import_date, SUM(total_amount) AS amountImportTotalOfDay FROM import_order GROUP BY import_date ) AS ImportTotals "
      + "RIGHT JOIN ( SELECT export_date, SUM(total_export_amount) AS amountExportTotalOfDay FROM export_order GROUP BY export_date ) AS ExportTotals "
      + "ON ImportTotals.import_date = ExportTotals.export_date "
      + "WHERE COALESCE(ImportTotals.import_date, ExportTotals.export_date) BETWEEN :startDate AND :endDate "
      + "ORDER BY transactionDate ASC", nativeQuery = true)
  List<Object[]> getImportExportTotalsByDateRange(@Param("startDate") LocalDate startDate,
      @Param("endDate") LocalDate endDate);

  @Query(value = """
      SELECT
          import_date AS importDate,
          SUM(total_amount) AS totalAmount
      FROM import_order
      WHERE import_date BETWEEN :startDate AND :endDate
      GROUP BY import_date
      ORDER BY import_date ASC
      """, nativeQuery = true)
  List<Object[]> getTotalImportAmountGroupedByDate(
      @Param("startDate") LocalDate startDate,
      @Param("endDate") LocalDate endDate);

  @Query(value = """
      SELECT
          export_date AS exportDate,
          SUM(total_export_amount) AS totalExportAmount
      FROM export_order
      WHERE export_date BETWEEN :startDate AND :endDate
      GROUP BY export_date
      ORDER BY export_date ASC
      """, nativeQuery = true)
  List<Object[]> getTotalExportAmountGroupedByDate(
      @Param("startDate") LocalDate startDate,
      @Param("endDate") LocalDate endDate);

}
