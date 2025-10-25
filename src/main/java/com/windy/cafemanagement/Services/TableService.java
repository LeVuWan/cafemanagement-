package com.windy.cafemanagement.Services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.windy.cafemanagement.Responses.InfoMenuRes;
import com.windy.cafemanagement.Responses.InformationTableRes;
import com.windy.cafemanagement.configs.SecurityUtil;
import com.windy.cafemanagement.dto.ChooseMenuDto;
import com.windy.cafemanagement.dto.Menus;
import com.windy.cafemanagement.dto.OrderTableDto;
import com.windy.cafemanagement.enums.InvoiceStatus;
import com.windy.cafemanagement.enums.TableStatus;
import com.windy.cafemanagement.models.Employee;
import com.windy.cafemanagement.models.Invoice;
import com.windy.cafemanagement.models.InvoiceDetail;
import com.windy.cafemanagement.models.Menu;
import com.windy.cafemanagement.models.MenuDetail;
import com.windy.cafemanagement.models.TableBookingDetail;
import com.windy.cafemanagement.models.TableEntity;
import com.windy.cafemanagement.repositories.EmployeeRepository;
import com.windy.cafemanagement.repositories.InvoiceDetailRepository;
import com.windy.cafemanagement.repositories.InvoiceRepository;
import com.windy.cafemanagement.repositories.MenuRepository;
import com.windy.cafemanagement.repositories.TableBookingDetailRepository;
import com.windy.cafemanagement.repositories.TableRepository;

import jakarta.transaction.Transactional;

@Service
public class TableService {
    private final TableRepository tableRepository;
    private final EmployeeRepository employeeRepository;
    private final TableBookingDetailRepository bookingDetailRepository;
    private final InvoiceRepository invoiceRepository;
    private final MenuRepository menuRepository;
    private final InvoiceDetailRepository invoiceDetailRepository;

    public TableService(TableRepository tableRepository, EmployeeRepository employeeRepository,
            TableBookingDetailRepository bookingDetailRepository, InvoiceRepository invoiceRepository,
            MenuRepository menuRepository, InvoiceDetailRepository invoiceDetailRepository) {
        this.tableRepository = tableRepository;
        this.employeeRepository = employeeRepository;
        this.bookingDetailRepository = bookingDetailRepository;
        this.invoiceRepository = invoiceRepository;
        this.menuRepository = menuRepository;
        this.invoiceDetailRepository = invoiceDetailRepository;
    }

    public List<TableEntity> getAllTableService() {
        return tableRepository.findAllByIsDeleted(false);
    }

    @Transactional
    public void orderTableService(OrderTableDto orderTableDto) {
        TableEntity table = tableRepository.findById(orderTableDto.getTableId())
                .orElseThrow(
                        () -> new RuntimeException("Không tìm thấy hàng hóa có ID: " + orderTableDto.getTableId()));

        if (table.getStatus() != TableStatus.AVAILABLE) {
            throw new RuntimeException("Bàn với id: " + orderTableDto.getTableId() + " đã được sữ dụng");
        }

        Invoice invoice = new Invoice();
        invoice.setTotalAmount(0.0);
        invoice.setTransactionDate(LocalDate.now());
        invoice.setStatus(InvoiceStatus.CREATED);
        invoice.setVoucher(null);
        invoice.setIsDeleted(false);
        invoiceRepository.save(invoice);

        String username = SecurityUtil.getSessionUser();
        Employee employee = employeeRepository.findByUsername(username);

        TableBookingDetail tableBookingDetail = new TableBookingDetail();

        tableBookingDetail.setTable(table);
        tableBookingDetail.setEmployee(employee);
        tableBookingDetail.setInvoice(invoice);
        tableBookingDetail.setCustomerName(orderTableDto.getCustomerName());
        tableBookingDetail.setCustomerPhone(orderTableDto.getCustomerPhone());
        tableBookingDetail.setBookingTime(LocalDateTime.of(orderTableDto.getDateOrder(), orderTableDto.getTimeOrder()));
        tableBookingDetail.setIsDeleted(false);
        bookingDetailRepository.save(tableBookingDetail);

        table.setStatus(TableStatus.RESERVED);
        tableRepository.save(table);
    }

    @Transactional
    public void chooseMenuService(ChooseMenuDto chooseMenuDto) {
        TableEntity table = tableRepository.findById(chooseMenuDto.getTableId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bàn có ID: " + chooseMenuDto.getTableId()));

        if (table.getStatus() == TableStatus.AVAILABLE) {
            throw new RuntimeException("Vui lòng đặt bàn trước khi chọn món");
        }

        table.setStatus(TableStatus.OCCUPIED);

        List<InvoiceStatus> unpaidStatuses = List.of(InvoiceStatus.CREATED, InvoiceStatus.UPDATED);
        Invoice invoice = invoiceRepository
                .findCurrentUnpaidInvoiceByTableId(chooseMenuDto.getTableId(), unpaidStatuses)
                .orElseThrow(() -> new RuntimeException(
                        "Hóa đơn chưa thanh toán của bàn với id " + chooseMenuDto.getTableId() + " không tồn tại."));

        invoice.setStatus(InvoiceStatus.UPDATED);

        if (chooseMenuDto.getMenus() != null && !chooseMenuDto.getMenus().isEmpty()) {
            List<InvoiceDetail> invoiceDetails = new ArrayList<>();
            double totalAmount = invoice.getTotalAmount() != null ? invoice.getTotalAmount() : 0.0;

            for (Menus menuDto : chooseMenuDto.getMenus()) {

                Menu menuExist = menuRepository.findById(menuDto.getMenuId())
                        .orElseThrow(() -> new RuntimeException(
                                "Thực đơn với id: " + menuDto.getMenuId() + " không tồn tại"));

                InvoiceDetail existingDetail = invoiceDetailRepository
                        .findByInvoice_InvoiceIdAndMenu_MenuIdAndIsDeletedFalse(invoice.getInvoiceId(),
                                menuDto.getMenuId());

                if (existingDetail != null) {
                    int newQty = existingDetail.getQuantity() + menuDto.getQuantity();
                    existingDetail.setQuantity(newQty);
                    existingDetail.setTotalPrice(newQty * existingDetail.getMenu().getCurrentPrice());
                    invoiceDetailRepository.save(existingDetail);
                } else {
                    InvoiceDetail detail = new InvoiceDetail();
                    detail.setInvoice(invoice);
                    detail.setMenu(menuExist);
                    detail.setQuantity(menuDto.getQuantity());
                    detail.setTotalPrice(menuExist.getCurrentPrice() * menuDto.getQuantity());
                    detail.setIsDeleted(false);
                    invoiceDetailRepository.save(detail);
                }
            }

            invoiceDetailRepository.saveAll(invoiceDetails);

            invoice.setTotalAmount(totalAmount);
            invoice.setStatus(InvoiceStatus.UPDATED);
            invoiceRepository.save(invoice);
        }
    }

    public InformationTableRes getInformantionTableService(Long tableId) {
        List<InvoiceStatus> unpaidStatuses = List.of(InvoiceStatus.UPDATED);
        Invoice invoice = invoiceRepository
                .findCurrentUnpaidInvoiceByTableId(tableId, unpaidStatuses)
                .orElseThrow(() -> new RuntimeException(
                        "Hóa đơn chưa thanh toán của bàn với id " + tableId + " không tồn tại."));

        List<InfoMenuRes> invoiceDetails = invoiceDetailRepository
                .findMenuAndQuantityByInvoiceId(invoice.getInvoiceId());

        TableBookingDetail tableBookingDetail = bookingDetailRepository
                .findActiveByTableIdAndInvoiceId(tableId, invoice.getInvoiceId())
                .orElseThrow(() -> new RuntimeException(
                        "Không tìm thấy thông tin khách hàng"));

        return new InformationTableRes(tableBookingDetail.getCustomerName(),
                tableBookingDetail.getBookingTime().toLocalTime(), tableBookingDetail.getBookingTime().toLocalDate(),
                invoiceDetails);
    }
}
