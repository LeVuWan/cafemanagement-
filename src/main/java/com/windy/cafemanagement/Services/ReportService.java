package com.windy.cafemanagement.Services;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.windy.cafemanagement.Responses.GenaralReportRes;
import com.windy.cafemanagement.Responses.ImportByDateReportRes;
import com.windy.cafemanagement.Responses.ImportExportRes;
import com.windy.cafemanagement.Responses.TotalInvoiceByDateRes;
import com.windy.cafemanagement.repositories.ExpenseRepository;
import com.windy.cafemanagement.repositories.InvoiceRepository;
import com.windy.cafemanagement.repositories.ProductRepository;

/**
 * ReportService
 *
 * Version 1.0
 *
 * Date: 10-11-2025
 *
 * Copyright
 *
 * Modification Logs:
 * DATE AUTHOR DESCRIPTION
 * -----------------------------------------------------------------------
 * 11-11-2025 VuLQ Create
 */
@Service
public class ReportService {
        private final InvoiceRepository invoiceRepository;
        private final ProductRepository productRepository;
        private final ExpenseRepository expenseRepository;

        public ReportService(InvoiceRepository invoiceRepository, ProductRepository productRepository,
                        ExpenseRepository expenseRepository) {
                this.invoiceRepository = invoiceRepository;
                this.productRepository = productRepository;
                this.expenseRepository = expenseRepository;
        }

        /**
         * get profit from invoice and spending from expense, import order
         * 
         * @param from, to
         * @return List<GenaralReportRes>
         * @throws NullPointerException
         */
        public List<GenaralReportRes> generalReportService(LocalDate from, LocalDate to) {
                if (from == null || to == null) {
                        throw new NullPointerException("from or to not found");
                }

                List<Object[]> reportData = invoiceRepository.getDailyIncomeExpense(from, to);

                return reportData.stream()
                                .map(record -> new GenaralReportRes(
                                                record[0].toString(),
                                                record[1] != null ? ((Number) record[1]).doubleValue() : 0.0,
                                                record[2] != null ? ((Number) record[2]).doubleValue() : 0.0))
                                .collect(Collectors.toList());
        }

        /**
         * get profit from invoice and spending from expense, import order
         * 
         * @param from, to
         * @return List<GenaralReportRes>
         * @throws NullPointerException
         */
        public List<GenaralReportRes> generalReportServiceForBudget() {
                List<Object[]> reportData = invoiceRepository.getDailyIncomeExpense();

                return reportData.stream()
                                .map(record -> new GenaralReportRes(
                                                record[0].toString(),
                                                record[1] != null ? ((Number) record[1]).doubleValue() : 0.0,
                                                record[2] != null ? ((Number) record[2]).doubleValue() : 0.0))
                                .collect(Collectors.toList());
        }

        /**
         * get spending from import order
         * 
         * @param from, to
         * @return List<ImportExportRes>
         * @throws NullPointerException
         */
        public List<ImportExportRes> importExportReportService(LocalDate from, LocalDate to) {
                if (from == null || to == null) {
                        throw new NullPointerException("from or to not found");
                }
                List<Object[]> reportData = productRepository.getImportExportTotalsByDateRange(from, to);
                return reportData.stream()
                                .map(record -> new ImportExportRes(record[0].toString(),
                                                record[1] != null ? ((Number) record[1]).doubleValue() : 0.0,
                                                record[2] != null ? ((Number) record[2]).doubleValue() : 0.0))
                                .collect(Collectors.toList());
        }

        /**
         * get spending from import order
         * 
         * @param startDate, endDate
         * @return List<ImportByDateReportRes>
         * @throws NullPointerException
         */
        public List<ImportByDateReportRes> getImportOderByDateService(LocalDate startDate, LocalDate endDate) {
                if (startDate == null || endDate == null) {
                        throw new NullPointerException("startDate or endDate not found");
                }
                List<Object[]> reportData = productRepository.getTotalExportAmountGroupedByDate(startDate, endDate);

                return reportData.stream()
                                .map(record -> new ImportByDateReportRes(
                                                record[0].toString(),
                                                record[1] != null ? ((Number) record[1]).doubleValue() : 0.0))
                                .collect(Collectors.toList());
        }

        /**
         * get spending from export order
         * 
         * @param startDate, endDate
         * @return List<ImportByDateReportRes>
         * @throws NullPointerException
         */
        public List<ImportByDateReportRes> getExportOderByDateService(LocalDate startDate, LocalDate endDate) {
                if (startDate == null || endDate == null) {
                        throw new NullPointerException("startDate or endDate not found");
                }
                List<Object[]> reportData = productRepository.getTotalImportAmountGroupedByDate(startDate, endDate);

                return reportData.stream()
                                .map(record -> new ImportByDateReportRes(
                                                record[0].toString(),
                                                record[1] != null ? ((Number) record[1]).doubleValue() : 0.0))
                                .collect(Collectors.toList());
        }

        /**
         * get profit from invoice
         * 
         * @param startDate, endDate
         * @return List<TotalInvoiceByDateRes>
         * @throws NullPointerException
         */
        public List<TotalInvoiceByDateRes> getInvoiceTotalByDateService(LocalDate startDate, LocalDate endDate) {
                if (startDate == null || endDate == null) {
                        throw new NullPointerException("startDate or endDate not found");
                }
                List<Object[]> reportData = invoiceRepository.getInvoiceTotalsByDate(startDate, endDate);

                return reportData.stream()
                                .map(record -> new TotalInvoiceByDateRes(
                                                record[0].toString(),
                                                record[1] != null ? ((Number) record[1]).doubleValue() : 0.0))
                                .collect(Collectors.toList());
        }

        /**
         * get spending from expense
         * 
         * @param startDate, endDate
         * @return List<TotalInvoiceByDateRes>
         * @throws NullPointerException
         */
        public List<TotalInvoiceByDateRes> getTotalEnpenseByDateService(LocalDate startDate, LocalDate endDate) {
                if (startDate == null || endDate == null) {
                        throw new NullPointerException("startDate or endDate not found");
                }
                List<Object[]> reportData = expenseRepository.findByExpenseDateBetween(startDate, endDate);
                return reportData.stream()
                                .map(record -> new TotalInvoiceByDateRes(
                                                record[0].toString(),
                                                record[1] != null ? ((Number) record[1]).doubleValue() : 0.0))
                                .collect(Collectors.toList());
        }
}
