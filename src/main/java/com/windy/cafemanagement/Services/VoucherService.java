package com.windy.cafemanagement.Services;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.windy.cafemanagement.dto.CreateVoucherDto;
import com.windy.cafemanagement.models.Voucher;
import com.windy.cafemanagement.repositories.VoucherRepository;

/**
 * VoucherService
 *
 * Version 1.0
 *
 * Date: 11-11-2025
 *
 * Copyright
 *
 * Modification Logs:
 * DATE AUTHOR DESCRIPTION
 * -----------------------------------------------------------------------
 */
@Service
public class VoucherService {
    private final VoucherRepository voucherRepository;

    public VoucherService(VoucherRepository voucherRepository) {
        this.voucherRepository = voucherRepository;
    }

    /**
     * Get all vouchers that are not soft-deleted
     * 
     * @return list of active Voucher
     */
    public List<Voucher> getAllVouchersService() {
        return voucherRepository.findAllByIsDeleted(false);
    }

    /**
     * save a new voucher
     * 
     * @param createVoucherDto
     * @throws NullPointerException when dto is null
     */
    public void saveVoucherService(CreateVoucherDto createVoucherDto) {
        if (createVoucherDto == null) {
            throw new NullPointerException("createVoucherDto not found");
        }

        voucherRepository.save(createVoucherDtoToVoucher(createVoucherDto));
    }

    /**
     * find voucher by id
     * 
     * @param id
     * @return Voucher
     * @throws NullPointerException when id is null
     * @throws RuntimeException     when voucher not found
     */
    public Voucher findVoucherById(Long id) {
        if (id == null) {
            throw new NullPointerException("id not found");
        }

        return voucherRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Voucher not found"));
    }

    /**
     * Update an existing voucher's fields when values present in updatedVoucher
     * 
     * @param id             id of voucher to update
     * @param updatedVoucher voucher object containing updated values
     * @throws NullPointerException when id or updatedVoucher is null
     * @throws RuntimeException     when voucher with given id does not exist
     */
    public void updateVoucher(Long id, Voucher updatedVoucher) {
        if (id == null) {
            throw new NullPointerException("id not found");
        }

        if (updatedVoucher == null) {
            throw new NullPointerException("updatedVoucher not found");
        }

        Voucher existing = voucherRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Voucher not found"));

        if (isNotBlank(updatedVoucher.getVoucherName())) {
            existing.setVoucherName(updatedVoucher.getVoucherName());
        }

        if (isNotBlank(updatedVoucher.getStartDate() + "")) {
            existing.setStartDate(updatedVoucher.getStartDate());
        }

        if (isNotBlank(updatedVoucher.getEndDate() + "")) {
            existing.setEndDate(updatedVoucher.getEndDate());
        }

        if (isNotBlank(updatedVoucher.getDiscountValue() + "")) {
            existing.setDiscountValue(updatedVoucher.getDiscountValue());
        }
        voucherRepository.save(existing);
    }

    /**
     * Soft-delete a voucher by id
     * 
     * @param id id of voucher to delete
     * @throws NullPointerException when id is null
     */
    public void deleteVoucherService(Long id) {
        if (id == null) {
            throw new NullPointerException("id not found");
        }

        voucherRepository.softDeleteById(id);
    }

    /**
     * Get currently active vouchers by current date
     * 
     * @return list of Voucher active today
     */
    public List<Voucher> getVoucherByTimeService() {
        return voucherRepository.findActiveVouchersByDate(LocalDate.now());
    }

    /**
     * Convert CreateVoucherDto to Voucher entity
     * 
     * @param createVoucherDto DTO containing voucher data
     * @return Voucher entity
     * @throws NullPointerException when createVoucherDto is null
     */
    private Voucher createVoucherDtoToVoucher(CreateVoucherDto createVoucherDto) {
        if (createVoucherDto == null) {
            throw new NullPointerException("createVoucherDto not found");
        }

        Voucher voucher = new Voucher();
        voucher.setVoucherName(createVoucherDto.getVoucherName());
        voucher.setStartDate(createVoucherDto.getStartDate());
        voucher.setEndDate(createVoucherDto.getEndDate());
        voucher.setDiscountValue(createVoucherDto.getDiscountValue());
        voucher.setIsDeleted(false);
        return voucher;
    }

    private boolean isNotBlank(String value) {
        return value != null && !value.trim().isEmpty();
    }
}
