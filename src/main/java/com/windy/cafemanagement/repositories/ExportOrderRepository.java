package com.windy.cafemanagement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.windy.cafemanagement.models.ExportOrder;

public interface ExportOrderRepository extends JpaRepository<ExportOrder, Long> {
}