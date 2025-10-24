package com.windy.cafemanagement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.windy.cafemanagement.models.TableBookingDetail;

public interface TableBookingDetailRepository extends JpaRepository<TableBookingDetail, Long> {

}
