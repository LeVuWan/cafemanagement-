package com.windy.cafemanagement.repositories;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.windy.cafemanagement.models.Equipment;
import com.windy.cafemanagement.models.ImportOrder;
import com.windy.cafemanagement.models.Product;

/**
 * ImportOrderRepository interface
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
public interface ImportOrderRepository extends JpaRepository<ImportOrder, Long> {

    /**
     * get import order by equipment
     * @param equipment
     * @return List<ImportOrder>
     */
    List<ImportOrder> findByEquipment(Equipment equipment);

    /**
     * get import order by product id and isDeleted false
     * @param equipment
     * @return List<ImportOrder>
     */
    List<ImportOrder> findByProduct_ProductIdAndIsDeletedFalse(Long productId);

    /**
     * get import order by product
     * @param product
     * @return List<ImportOrder>
     */
    List<ImportOrder> findByProduct(Product product);
}