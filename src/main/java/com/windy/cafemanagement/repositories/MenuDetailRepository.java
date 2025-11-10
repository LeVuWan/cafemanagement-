package com.windy.cafemanagement.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.windy.cafemanagement.models.MenuDetail;

/**
 * MenuDetailRepository interface
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
public interface MenuDetailRepository extends JpaRepository<MenuDetail, Long> {

    /**
     * get menu details by menu id and isDeleted false
     * 
     * @param menuId
     * @return List<MenuDetail>
     */
    List<MenuDetail> findByMenu_MenuIdAndIsDeletedFalse(Long menuId);
}
