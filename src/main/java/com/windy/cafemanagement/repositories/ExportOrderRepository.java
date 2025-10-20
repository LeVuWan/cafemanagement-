package com.windy.cafemanagement.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.windy.cafemanagement.models.ExportOrder;
import com.windy.cafemanagement.models.Product;

public interface ExportOrderRepository extends JpaRepository<ExportOrder, Long> {
    List<ExportOrder> findByProduct(Product product);

    List<ExportOrder> findByProduct_ProductIdAndIsDeletedFalse(Long productId);
}