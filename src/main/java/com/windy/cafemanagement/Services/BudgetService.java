package com.windy.cafemanagement.Services;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.windy.cafemanagement.Responses.FinancialSummaryRes;
import com.windy.cafemanagement.repositories.FinancialSummaryRepository;

@Service
public class BudgetService {
    private final FinancialSummaryRepository financialSummaryRepository;

    public BudgetService(FinancialSummaryRepository financialSummaryRepository) {
        this.financialSummaryRepository = financialSummaryRepository;
    }

    public List<FinancialSummaryRes> getFinancialSummary(LocalDate startDate, LocalDate endDate) {
        List<Object[]> rows = financialSummaryRepository.getFinancialSummaryRaw(startDate, endDate);

        return rows.stream()
                .map(r -> new FinancialSummaryRes(
                        ((Date) r[0]).toLocalDate(),
                        ((Number) r[1]).doubleValue(),
                        ((Number) r[2]).doubleValue()))
                .collect(Collectors.toList());
    }
}
