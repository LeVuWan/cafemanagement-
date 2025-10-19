package com.windy.cafemanagement.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.windy.cafemanagement.models.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findAllByIsDeleted(Boolean isDeleted);

    Optional<Product> findByProductIdAndIsDeletedFalse(Long productId);
}
