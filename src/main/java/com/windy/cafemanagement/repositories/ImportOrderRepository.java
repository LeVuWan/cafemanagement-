package com.windy.cafemanagement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.windy.cafemanagement.models.ImportOrder;

@Repository
public interface ImportOrderRepository extends JpaRepository<ImportOrder, Long> {
}