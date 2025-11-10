package com.windy.cafemanagement.repositories;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.windy.cafemanagement.models.Permission;

/**
 * PermissionRepository interface
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
public interface PermissionRepository extends JpaRepository<Permission, Long> {
    
    /**
     * get all permission by isDeleted false
     * 
     * @param isDeleted
     * @return List<Permission>
     */
    List<Permission> findAllByIsDeleted(Boolean isDeleted);

    /**
     * get permission by permission id
     * 
     * @param permissionId
     * @return Optional<Permission>
     */
    Optional<Permission> findById(Long permissionId);

}
