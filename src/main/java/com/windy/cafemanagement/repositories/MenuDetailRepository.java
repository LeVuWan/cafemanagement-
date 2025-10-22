package com.windy.cafemanagement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.windy.cafemanagement.models.MenuDetail;

@Repository
public interface MenuDetailRepository extends JpaRepository<MenuDetail, Long> {

}
