package com.windy.cafemanagement.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.windy.cafemanagement.models.Expense;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findByIsDeleteFalse();

    @Query("SELECT e.expenseDate AS date, SUM(e.amount) AS totalExpense " +
            "FROM Expense e " +
            "WHERE e.isDelete = false AND e.expenseDate BETWEEN :startDate AND :endDate " +
            "GROUP BY e.expenseDate")
    List<Object[]> findByExpenseDateBetween(@Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
}
