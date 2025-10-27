package com.windy.cafemanagement.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.windy.cafemanagement.models.Voucher;

import jakarta.transaction.Transactional;

@Repository
public interface VoucherRepository extends JpaRepository<Voucher, Long> {
    List<Voucher> findAllByIsDeleted(Boolean isDeleted);

    @Modifying
    @Transactional
    @Query("UPDATE Voucher v SET v.isDeleted = true WHERE v.voucherId = :id")
    void softDeleteById(@Param("id") Long id);

    @Query("SELECT v FROM Voucher v " +
            "WHERE v.isDeleted = false " +
            "AND :date BETWEEN v.startDate AND v.endDate")
    List<Voucher> findActiveVouchersByDate(@Param("date") LocalDate date);
}
