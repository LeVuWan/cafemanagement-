package com.windy.cafemanagement.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.windy.cafemanagement.models.Equipment;
import com.windy.cafemanagement.models.Permission;

@Repository
public interface EquipmentRepository extends JpaRepository<Equipment, Long> {
    List<Equipment> findAllByIsDeleted(Boolean isDeleted);
}
