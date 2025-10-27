package com.windy.cafemanagement.Services;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.windy.cafemanagement.dto.CreateVoucherDto;
import com.windy.cafemanagement.models.Voucher;
import com.windy.cafemanagement.repositories.VoucherRepository;

@Service
public class VoucherService {
    private final VoucherRepository voucherRepository;

    public VoucherService(VoucherRepository voucherRepository) {
        this.voucherRepository = voucherRepository;
    }

    public List<Voucher> getAllVouchersService() {
        return voucherRepository.findAllByIsDeleted(false);
    }

    public void saveVoucherService(CreateVoucherDto createVoucherDto) {
        voucherRepository.save(createVoucherDtoToVoucher(createVoucherDto));
    }

    public Voucher findVoucherById(Long id) {
        return voucherRepository.findById(id).get();
    }

    public void updateVoucher(Long id, Voucher updatedVoucher) {
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

    public void deleteVoucherService(Long id) {
        voucherRepository.softDeleteById(id);
    }

    public List<Voucher> getVoucherByTimeService() {
        return voucherRepository.findActiveVouchersByDate(LocalDate.now());
    }

    private Voucher createVoucherDtoToVoucher(CreateVoucherDto createVoucherDto) {
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
