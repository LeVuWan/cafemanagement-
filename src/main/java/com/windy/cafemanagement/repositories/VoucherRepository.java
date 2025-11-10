package com.windy.cafemanagement.repositories;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import jakarta.transaction.Transactional;

import com.windy.cafemanagement.models.Voucher;

/**
 * VoucherRepository interface
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
public interface VoucherRepository extends JpaRepository<Voucher, Long> {

    /**
     * get all voucher by isDeleted false
     * 
     * @param isDeleted
     * 
     * @return List<Voucher>
     */
    List<Voucher> findAllByIsDeleted(Boolean isDeleted);

    /**
     * soft delete voucher by id
     * 
     * @param id
     * 
     */
    @Modifying
    @Transactional
    @Query("UPDATE Voucher v SET v.isDeleted = true WHERE v.voucherId = :id")
    void softDeleteById(@Param("id") Long id);

    /**
     * get active vouchers by date
     * 
     * @param date
     * @return List<Voucher>
     */
    @Query("SELECT v FROM Voucher v " +
            "WHERE v.isDeleted = false " +
            "AND :date BETWEEN v.startDate AND v.endDate")
    List<Voucher> findActiveVouchersByDate(@Param("date") LocalDate date);
}
