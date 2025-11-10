package com.windy.cafemanagement.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.windy.cafemanagement.models.Equipment;

/**
 * EquipmentRepository interface
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
public interface EquipmentRepository extends JpaRepository<Equipment, Long> {
    
    /**
     * get all equipments by isDeleted 
     * 
     * @param isDeleted
     * @return List<Equipment>
     */
    List<Equipment> findAllByIsDeleted(Boolean isDeleted);
}
