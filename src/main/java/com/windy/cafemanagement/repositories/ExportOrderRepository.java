package com.windy.cafemanagement.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import com.windy.cafemanagement.models.ExportOrder;
import com.windy.cafemanagement.models.Product;

/**
 * ExportOrderRepository interface
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
public interface ExportOrderRepository extends JpaRepository<ExportOrder, Long> {

    /**
     * get list export orders of product
     * 
     * @param product
     * @return List<ExportOrder>
     */
    List<ExportOrder> findByProduct(Product product);

    /**
     * get list export orders of product by product id and isDeleted false
     * 
     * @param productId
     * @return List<ExportOrder>
     */
    List<ExportOrder> findByProduct_ProductIdAndIsDeletedFalse(Long productId);
}