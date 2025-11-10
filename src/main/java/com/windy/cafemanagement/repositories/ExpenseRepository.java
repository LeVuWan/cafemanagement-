package com.windy.cafemanagement.repositories;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.windy.cafemanagement.models.Expense;

/**
 * ExpenseRepository interface
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
public interface ExpenseRepository extends JpaRepository<Expense, Long> {

        /**
         * get data for Job Details page
         * 
         * @param establishmentCode
         * @return
         * @throws DAOException
         */
        List<Expense> findByIsDeleteFalse();

        /**
         * get total expense by date range
         * 
         * @param startDate
         * @param endDate
         * @return List<Object[]>
         */
        @Query("SELECT e.expenseDate AS date, SUM(e.amount) AS totalExpense " +
                        "FROM Expense e " +
                        "WHERE e.isDelete = false AND e.expenseDate BETWEEN :startDate AND :endDate " +
                        "GROUP BY e.expenseDate")

        /**
         * get total expense by date range
         * 
         * @param startDate
         * @param endDate
         * @return List<Object[]>
         */
        List<Object[]> findByExpenseDateBetween(@Param("startDate") LocalDate startDate,
                        @Param("endDate") LocalDate endDate);
}
