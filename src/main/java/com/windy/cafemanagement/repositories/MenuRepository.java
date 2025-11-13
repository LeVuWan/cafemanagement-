package com.windy.cafemanagement.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.windy.cafemanagement.models.Menu;
import com.windy.cafemanagement.models.MenuDetail;

/**
 * MenuRepository interface
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
public interface MenuRepository extends JpaRepository<Menu, Long> {

    /**
     * get all menu by keyword search and isDeleted false
     * 
     * @param keyword
     * @return List<Menu>
     */
    @Query("""
            SELECT m
            FROM Menu m
            WHERE m.dishName LIKE CONCAT('%', :keyword, '%')
            AND m.isDeleted = FALSE
            """)
    List<Menu> findAllMenu(@Param("keyword") String keyword);

    @Query("""
            SELECT md
            FROM MenuDetail md
            JOIN md.menu m
            WHERE m.isDeleted = FALSE
              AND md.isDeleted = FALSE
              AND m.menuId = :menuId
            """)
    List<MenuDetail> findMenuDetailByMenu(@Param("menuId") Long menuId);
}
