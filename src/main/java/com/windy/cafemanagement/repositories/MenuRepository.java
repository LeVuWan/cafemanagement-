package com.windy.cafemanagement.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.windy.cafemanagement.models.Menu;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {
    @Query("""
            SELECT m
            FROM Menu m
            WHERE m.dishName LIKE CONCAT('%', :keyword, '%')
            AND m.isDeleted = FALSE
            """)
    List<Menu> findAllMenu(@Param("keyword") String keyword);
}
