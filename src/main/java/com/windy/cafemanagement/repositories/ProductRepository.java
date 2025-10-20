package com.windy.cafemanagement.repositories;

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

}
