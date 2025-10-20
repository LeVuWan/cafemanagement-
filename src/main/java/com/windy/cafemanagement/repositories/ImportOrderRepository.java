package com.windy.cafemanagement.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.windy.cafemanagement.models.Equipment;
import com.windy.cafemanagement.models.ImportOrder;
import com.windy.cafemanagement.models.Product;

@Repository
public interface ImportOrderRepository extends JpaRepository<ImportOrder, Long> {
    Optional<ImportOrder> findByEquipment(Equipment equipment);

    List<ImportOrder> findByProduct_ProductIdAndIsDeletedFalse(Long productId);

    List<ImportOrder> findByProduct(Product product);
}