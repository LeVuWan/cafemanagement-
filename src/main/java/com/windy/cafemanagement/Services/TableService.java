package com.windy.cafemanagement.Services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

import com.windy.cafemanagement.Responses.InfoMenuRes;
import com.windy.cafemanagement.Responses.InformationTableRes;
import com.windy.cafemanagement.Responses.TableInforRes;
import com.windy.cafemanagement.Responses.TableToMergeRes;
import com.windy.cafemanagement.configs.SecurityUtil;
import com.windy.cafemanagement.dto.ChooseMenuDto;
import com.windy.cafemanagement.dto.CutTableDto;
import com.windy.cafemanagement.dto.Menu;
import com.windy.cafemanagement.dto.MergeTableDto;
import com.windy.cafemanagement.dto.MoveTableDto;
import com.windy.cafemanagement.dto.OrderTableDto;
import com.windy.cafemanagement.dto.PaymentDto;
import com.windy.cafemanagement.enums.InvoiceStatus;
import com.windy.cafemanagement.enums.TableStatus;
import com.windy.cafemanagement.models.Employee;
import com.windy.cafemanagement.models.Invoice;
import com.windy.cafemanagement.models.InvoiceDetail;
import com.windy.cafemanagement.models.InvoiceDetailId;
import com.windy.cafemanagement.models.TableBookingDetail;
import com.windy.cafemanagement.models.TableBookingDetailId;
import com.windy.cafemanagement.models.TableEntity;
import com.windy.cafemanagement.models.Voucher;
import com.windy.cafemanagement.repositories.EmployeeRepository;
import com.windy.cafemanagement.repositories.InvoiceDetailRepository;
import com.windy.cafemanagement.repositories.InvoiceRepository;
import com.windy.cafemanagement.repositories.MenuRepository;
import com.windy.cafemanagement.repositories.TableBookingDetailRepository;
import com.windy.cafemanagement.repositories.TableRepository;
import com.windy.cafemanagement.repositories.VoucherRepository;

/**
 * TableService
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
public class TableService {
    private static final Logger logger = LoggerFactory.getLogger(TableService.class);
    private final TableRepository tableRepository;
    private final EmployeeRepository employeeRepository;
    private final TableBookingDetailRepository bookingDetailRepository;
    private final InvoiceRepository invoiceRepository;
    private final MenuRepository menuRepository;
    private final InvoiceDetailRepository invoiceDetailRepository;
    private final VoucherRepository voucherRepository;

    public TableService(TableRepository tableRepository, EmployeeRepository employeeRepository,
            TableBookingDetailRepository bookingDetailRepository, InvoiceRepository invoiceRepository,
            MenuRepository menuRepository, InvoiceDetailRepository invoiceDetailRepository,
            VoucherRepository voucherRepository) {
        this.tableRepository = tableRepository;
        this.employeeRepository = employeeRepository;
        this.bookingDetailRepository = bookingDetailRepository;
        this.invoiceRepository = invoiceRepository;
        this.menuRepository = menuRepository;
        this.invoiceDetailRepository = invoiceDetailRepository;
        this.voucherRepository = voucherRepository;
    }

    /**
     * get all table isDeleted = false
     * 
     * @return List<TableEntity>
     */
    public List<TableEntity> getAllTableService() {
        return tableRepository.findAllByIsDeleted(false);
    }

    /**
     * order a table (create booking and invoice)
     * 
     * @param orderTableDto
     * @throws NullPointerException, RuntimeException
     */
    @Transactional
    public void orderTableService(OrderTableDto orderTableDto) {
        // Validate input
        if (orderTableDto == null) {
            throw new NullPointerException("orderTableDto not found");
        }

        if (orderTableDto.getDurationMinutes() > 30) {
            throw new IllegalArgumentException("Thời gian đặt trước không đọc quá 30 phút");
        }

        TableEntity table = tableRepository.findById(orderTableDto.getTableId())
                .orElseThrow(
                        () -> new EntityNotFoundException(
                                "Không tìm thấy hàng hóa có ID: " + orderTableDto.getTableId()));

        if (table.getStatus() != TableStatus.AVAILABLE) {
            throw new RuntimeException("Bàn với id: " + orderTableDto.getTableId() + " đã được sữ dụng");
        }

        Invoice invoice = createInvoice();

        String username = SecurityUtil.getSessionUser();

        if (username == null) {
            throw new SecurityException("Người dùng chưa đăng nhập");
        }

        Employee employee = getCurrentEmployee();

        TableBookingDetail tableBookingDetail = new TableBookingDetail();
        TableBookingDetailId id = new TableBookingDetailId(table.getTableId(), employee.getEmployeeId(),
                invoice.getInvoiceId());

        tableBookingDetail.setTableBookingDetailId(id);
        tableBookingDetail.setTable(table);
        tableBookingDetail.setEmployee(employee);
        tableBookingDetail.setInvoice(invoice);
        tableBookingDetail.setCustomerName(orderTableDto.getCustomerName());
        tableBookingDetail.setCustomerPhone(orderTableDto.getCustomerPhone());
        tableBookingDetail.setBookingTime(LocalDateTime.now().plusMinutes(orderTableDto.getDurationMinutes()));
        tableBookingDetail.setIsDeleted(false);
        System.out.println("Check time after convert: " + tableBookingDetail.getBookingTime());
        bookingDetailRepository.save(tableBookingDetail);

        table.setStatus(TableStatus.RESERVED);
        tableRepository.save(table);
    }

    /**
     * choose menu for a table (add/update invoice details)
     * 
     * @param chooseMenuDto
     * @throws NullPointerException, EntityNotFoundException, RuntimeException
     */
    @Transactional
    public void chooseMenuService(ChooseMenuDto chooseMenuDto) {

        // Validate input
        if (chooseMenuDto == null) {
            throw new NullPointerException("chooseMenuDto not found");
        }

        TableEntity table = tableRepository.findById(chooseMenuDto.getTableId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bàn có ID: " + chooseMenuDto.getTableId()));

        table.setStatus(TableStatus.OCCUPIED);

        List<InvoiceStatus> UNPAID_STATUSES = List.of(InvoiceStatus.CREATED, InvoiceStatus.UPDATED);

        Invoice invoice = invoiceRepository
                .findCurrentUnpaidInvoiceByTableId(chooseMenuDto.getTableId(), UNPAID_STATUSES)
                .orElseGet(() -> {
                    Invoice newInvoice = createInvoice();
                    TableBookingDetail bookingDetail = createBookingDetail(table, newInvoice, "", "");
                    return newInvoice;
                });

        invoice.setStatus(InvoiceStatus.UPDATED);

        List<InvoiceDetail> toSave = new ArrayList<>();

        for (Menu menuDto : chooseMenuDto.getMenu()) {
            com.windy.cafemanagement.models.Menu menuExist = menuRepository.findById(menuDto.getMenuId())
                    .orElseThrow(() -> new EntityNotFoundException(""));

            InvoiceDetail existingDetail = invoiceDetailRepository
                    .findByInvoice_InvoiceIdAndMenu_MenuIdAndIsDeletedFalse(invoice.getInvoiceId(),
                            menuDto.getMenuId());

            if (existingDetail != null) {
                int newQty = existingDetail.getQuantity() + menuDto.getQuantity();
                existingDetail.setQuantity(newQty);
                existingDetail.setTotalPrice(newQty * existingDetail.getMenu().getCurrentPrice());
                toSave.add(existingDetail);
            } else {
                InvoiceDetail detail = new InvoiceDetail();
                InvoiceDetailId id = new InvoiceDetailId(invoice.getInvoiceId(), menuDto.getMenuId());
                detail.setId(id);
                detail.setInvoice(invoice);
                detail.setMenu(menuExist);
                detail.setQuantity(menuDto.getQuantity());
                detail.setTotalPrice(menuExist.getCurrentPrice() * menuDto.getQuantity());
                detail.setIsDeleted(false);
                toSave.add(detail);
            }
        }

        invoiceDetailRepository.saveAll(toSave);

        List<InvoiceDetail> allDetails = invoiceDetailRepository
                .findAllByInvoice_InvoiceIdAndIsDeletedFalse(invoice.getInvoiceId());

        double totalAmount = allDetails.stream()
                .mapToDouble(InvoiceDetail::getTotalPrice)
                .sum();

        invoice.setTotalAmount(totalAmount);
        invoice.setStatus(InvoiceStatus.UPDATED);
        invoiceRepository.save(invoice);
    }

    /**
     * get information of table including customer and menu list
     * 
     * @param tableId
     * @return InformationTableRes
     * @throws NullPointerException, EntityNotFoundException
     */
    public InformationTableRes getInformantionTableService(Long tableId) {
        if (tableId == null) {
            throw new NullPointerException("tableId not found");
        }

        List<InvoiceStatus> unpaidStatuses = List.of(InvoiceStatus.UPDATED);
        Invoice invoice = invoiceRepository
                .findCurrentUnpaidInvoiceByTableId(tableId, unpaidStatuses)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Hóa đơn chưa thanh toán của bàn với id " + tableId + " không tồn tại."));

        List<InfoMenuRes> invoiceDetails = invoiceDetailRepository
                .findMenuAndQuantityByInvoiceId(invoice.getInvoiceId());

        TableBookingDetail tableBookingDetail = bookingDetailRepository
                .findActiveByTableIdAndInvoiceId(tableId, invoice.getInvoiceId())
                .orElseThrow(() -> new RuntimeException(
                        "Không tìm thấy thông tin khách hàng"));

        return new InformationTableRes(tableBookingDetail.getCustomerName(),
                tableBookingDetail.getBookingTime().toLocalTime(), tableBookingDetail.getBookingTime().toLocalDate(),
                invoiceDetails, tableBookingDetail.getCustomerPhone());
    }

    /**
     * get tables available to move to (excluding given table)
     * 
     * @param tableId
     * @return List<TableInforRes>
     * @throws NullPointerException
     */
    public List<TableInforRes> getTableToMoveService(Long tableId) {
        if (tableId == null) {
            throw new NullPointerException("tableId not found");
        }
        List<TableStatus> status = List.of(TableStatus.AVAILABLE);
        return tableRepository.findAllActiveExcept(tableId, status);
    }

    /**
     * get tables available to move to (excluding given table)
     * 
     * @param tableId
     * @return List<TableInforRes>
     * @throws NullPointerException
     */
    public List<TableInforRes> getTableToCutService(Long tableId) {
        if (tableId == null) {
            throw new NullPointerException("tableId not found");
        }
        List<TableStatus> status = List.of(TableStatus.AVAILABLE, TableStatus.OCCUPIED);
        return tableRepository.findAllActiveExcept(tableId, status);
    }

    /**
     * move a booking from one table to another
     * 
     * @param moveTableDto
     * @throws NullPointerException, EntityNotFoundException
     */
    @Transactional
    public void moveTableService(MoveTableDto moveTableDto) {
        if (moveTableDto == null) {
            throw new NullPointerException("moveTableDto not found");
        }

        TableEntity selectedTable = tableRepository.findById(moveTableDto.getToTableId())
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy bàn muốn chuyển đến"));

        if (selectedTable.getStatus() != TableStatus.AVAILABLE) {
            throw new IllegalArgumentException("Bàn được chọn hiện tại không trống");
        }

        TableEntity fromTable = tableRepository.findById(moveTableDto.getFromTableId())
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy bàn hiện tại"));

        if (fromTable.getStatus() == TableStatus.AVAILABLE) {
            throw new IllegalArgumentException("Bàn hiện tại đang không được sử dụng");
        }

        TableBookingDetail currentBooking = getBookingDetailByTableIdAndStatusInvoice(moveTableDto.getFromTableId(),
                List.of(InvoiceStatus.CREATED, InvoiceStatus.UPDATED),
                TableStatus.OCCUPIED);

        selectedTable.setStatus(TableStatus.OCCUPIED);
        fromTable.setStatus(TableStatus.AVAILABLE);
        tableRepository.saveAll(List.of(selectedTable, fromTable));

        currentBooking.setIsDeleted(true);
        bookingDetailRepository.save(currentBooking);

        TableBookingDetailId newId = new TableBookingDetailId();
        newId.setTableId(selectedTable.getTableId());
        newId.setEmployeeId(currentBooking.getTableBookingDetailId().getEmployeeId());
        newId.setInvoiceId(currentBooking.getTableBookingDetailId().getInvoiceId());

        TableBookingDetail newBooking = new TableBookingDetail();
        newBooking.setTableBookingDetailId(newId);
        newBooking.setTable(selectedTable);
        newBooking.setEmployee(currentBooking.getEmployee());
        newBooking.setInvoice(currentBooking.getInvoice());
        newBooking.setCustomerName(currentBooking.getCustomerName());
        newBooking.setCustomerPhone(currentBooking.getCustomerPhone());
        newBooking.setBookingTime(currentBooking.getBookingTime());
        newBooking.setIsDeleted(false);

        bookingDetailRepository.save(newBooking);
    }

    /**
     * get tables grouped for merge operation
     * 
     * @return TableToMergeRes
     */
    public TableToMergeRes getTableToMergeService() {
        List<TableEntity> tables = tableRepository.findAllByIsDeleted(false);

        TableToMergeRes tableToMerge = new TableToMergeRes();
        tableToMerge.setTablesFrom(new ArrayList<>());
        tableToMerge.setTablesTo(new ArrayList<>());

        for (TableEntity table : tables) {
            if (table.getStatus() == TableStatus.AVAILABLE) {
                tableToMerge.getTablesFrom().add(new TableInforRes(table.getTableId(), table.getTableName()));
            } else if (table.getStatus() == TableStatus.OCCUPIED || table.getStatus() == TableStatus.RESERVED) {
                tableToMerge.getTablesTo().add(new TableInforRes(table.getTableId(), table.getTableName()));
            }
        }

        return tableToMerge;
    }

    /**
     * merge multiple tables into one (create new invoice and booking)
     * 
     * @param mergeTableDto
     * @throws NullPointerException, EntityNotFoundException, SecurityException
     */
    @Transactional
    public void mergeTableService(MergeTableDto mergeTableDto) {
        if (mergeTableDto == null) {
            throw new NullPointerException("mergeTableDto not found");
        }

        TableEntity tableTo = getTableEntityById(mergeTableDto.getTableToId());
        if (tableTo.getStatus() != TableStatus.AVAILABLE) {
            throw new IllegalArgumentException("Bàn muốn gộp đến đang được sử dụng, không thể gộp");
        }
        tableTo.setStatus(TableStatus.OCCUPIED);
        tableRepository.save(tableTo);

        Employee employee = getCurrentEmployee();

        if (employee == null) {
            throw new EntityNotFoundException("Không tim thấy nhân viên đang đăng nhập.");
        }

        Invoice newInvoice = createInvoice();

        Map<Long, InvoiceDetail> mergedDetailMap = new HashMap<>();
        List<InvoiceStatus> UNPAID_STATUSES = List.of(InvoiceStatus.CREATED, InvoiceStatus.UPDATED);

        for (Long tableFromId : mergeTableDto.getListIdTableFrom()) {
            TableEntity tableFrom = getTableEntityById(tableFromId);

            Invoice oldInvoice = getInvoiceByTableIdAndStatusInvoice(tableFromId, UNPAID_STATUSES);

            List<InvoiceDetail> oldDetails = oldInvoice.getInvoiceDetails();

            for (InvoiceDetail detail : oldDetails) {
                if (Boolean.TRUE.equals(detail.getIsDeleted()))
                    continue;

                Long menuId = detail.getMenu().getMenuId();

                if (mergedDetailMap.containsKey(menuId)) {
                    InvoiceDetail existing = mergedDetailMap.get(menuId);
                    existing.setQuantity(existing.getQuantity() + detail.getQuantity());
                    existing.setTotalPrice(existing.getTotalPrice() + detail.getTotalPrice());
                } else {
                    InvoiceDetail newDetail = new InvoiceDetail();
                    InvoiceDetailId id = new InvoiceDetailId(newInvoice.getInvoiceId(), menuId);
                    newDetail.setId(id);
                    newDetail.setMenu(detail.getMenu());
                    newDetail.setInvoice(newInvoice);
                    newDetail.setQuantity(detail.getQuantity());
                    newDetail.setTotalPrice(detail.getTotalPrice());
                    newDetail.setIsDeleted(false);
                    mergedDetailMap.put(menuId, newDetail);
                }
            }

            TableBookingDetail oldBookingDetail = getBookingDetailByTableIdAndStatusInvoice(tableFromId,
                    UNPAID_STATUSES,
                    TableStatus.OCCUPIED);

            oldBookingDetail.setIsDeleted(true);
            bookingDetailRepository.save(oldBookingDetail);

            oldInvoice.setStatus(InvoiceStatus.CANCELLED);
            oldInvoice.setIsDeleted(true);
            invoiceRepository.save(oldInvoice);

            tableFrom.setStatus(TableStatus.AVAILABLE);
            tableRepository.save(tableFrom);
        }

        List<InvoiceDetail> mergedDetails = new ArrayList<>(mergedDetailMap.values());
        invoiceDetailRepository.saveAll(mergedDetails);

        TableBookingDetail bookingDetail = new TableBookingDetail();
        TableBookingDetailId bookingId = new TableBookingDetailId();
        bookingId.setTableId(tableTo.getTableId());
        bookingId.setEmployeeId(employee.getEmployeeId());
        bookingId.setInvoiceId(newInvoice.getInvoiceId());
        bookingDetail.setTableBookingDetailId(bookingId);
        bookingDetail.setEmployee(employee);
        bookingDetail.setInvoice(newInvoice);
        bookingDetail.setTable(tableTo);
        bookingDetail.setBookingTime(LocalDateTime.now());
        bookingDetail.setIsDeleted(false);
        bookingDetailRepository.save(bookingDetail);

        double total = mergedDetails.stream()
                .mapToDouble(InvoiceDetail::getTotalPrice)
                .sum();
        newInvoice.setTotalAmount(total);
        newInvoice.setStatus(InvoiceStatus.UPDATED);
        invoiceRepository.save(newInvoice);
    }

    /**
     * cancel booking and invoice for a table
     * 
     * @param tableId
     * @throws NullPointerException, RuntimeException
     */
    @Transactional
    public void cancelTableService(Long tableId) {
        if (tableId == null) {
            throw new NullPointerException("tableId not found");
        }

        TableEntity tableExist = getTableEntityById(tableId);

        List<InvoiceStatus> unpaidStatuses = List.of(InvoiceStatus.UPDATED, InvoiceStatus.CREATED);

        Invoice invoiceExist = getInvoiceByTableIdAndStatusInvoice(tableId, unpaidStatuses);

        List<InvoiceDetail> invoiceDetails = invoiceExist.getInvoiceDetails();

        TableStatus tableStatus = tableExist.getStatus() == TableStatus.OCCUPIED ? TableStatus.OCCUPIED
                : TableStatus.RESERVED;

        TableBookingDetail tableBookingDetail = getBookingDetailByTableIdAndStatusInvoice(tableId, unpaidStatuses,
                tableStatus);

        tableExist.setStatus(TableStatus.AVAILABLE);
        tableRepository.save(tableExist);

        invoiceExist.setStatus(InvoiceStatus.CANCELLED);
        invoiceExist.setIsDeleted(true);
        invoiceRepository.save(invoiceExist);

        for (InvoiceDetail detail : invoiceDetails) {
            detail.setIsDeleted(true);
        }

        invoiceDetailRepository.saveAll(invoiceDetails);

        tableBookingDetail.setIsDeleted(true);

        bookingDetailRepository.save(tableBookingDetail);
    }

    /**
     * cut items from one table invoice to another
     * 
     * @param cutTableDto
     * @throws NullPointerException, RuntimeException
     */
    @Transactional
    public void cutTableService(CutTableDto cutTableDto) {
        if (cutTableDto == null) {
            throw new NullPointerException("cutTableDto not found");
        }

        if (cutTableDto.getFromTableId() == null) {
            throw new NullPointerException("fromTableId not found in cutTableDto");
        }

        if (cutTableDto.getToTableId() == null) {
            throw new NullPointerException("toTableId not found in cutTableDto");
        }

        if (cutTableDto.getMenu() == null || cutTableDto.getMenu().isEmpty()) {
            throw new NullPointerException("menu list not found in cutTableDto");
        }

        if (cutTableDto.getFromTableId().equals(cutTableDto.getToTableId())) {
            throw new IllegalArgumentException("From table and To table must bedifferent");
        }

        // Lấy bàn nguồn
        TableEntity tableFrom = getTableEntityById(cutTableDto.getFromTableId());

        if (tableFrom.getStatus() == TableStatus.AVAILABLE) {
            throw new RuntimeException("Bàn hiện tại đang trống");
        }

        // Lấy bàn đích
        TableEntity tableTo = getTableEntityById(cutTableDto.getToTableId());

        // reuse UNPAID_STATUSES
        List<InvoiceStatus> UNPAID_STATUSES = List.of(InvoiceStatus.CREATED,
                InvoiceStatus.UPDATED);

        // Lấy hóa đơn bàn nguồn
        Invoice fromInvoice = getInvoiceByTableIdAndStatusInvoice(cutTableDto.getFromTableId(),
                UNPAID_STATUSES);

        // Lấy thông tin đặt bàn nguồn
        TableBookingDetail bookingDetailFromExist = getBookingDetailByTableIdAndStatusInvoice(
                cutTableDto.getFromTableId(),
                UNPAID_STATUSES,
                TableStatus.OCCUPIED);

        // Lấy hoặc tạo hóa đơn bàn đích
        Invoice toInvoice = invoiceRepository
                .findCurrentUnpaidInvoiceByTableId(tableTo.getTableId(), UNPAID_STATUSES)
                .orElseGet(this::createInvoice);

        Double totalAmountInvoiceTo = 0.0;

        // Nếu hóa đơn bàn đích chưa được cập nhật món (mới tạo)
        if (toInvoice.getStatus() == InvoiceStatus.CREATED) {
            for (Menu menu : cutTableDto.getMenu()) {
                com.windy.cafemanagement.models.Menu menuExist = menuRepository.findById(menu.getMenuId())
                        .orElseThrow(() -> new EntityNotFoundException(
                                "Không tìm thấy thực đơn với id: " + menu.getMenuId()));
                InvoiceDetail invoiceDetail = new InvoiceDetail();
                InvoiceDetailId id = new InvoiceDetailId(toInvoice.getInvoiceId(),
                        menu.getMenuId());
                invoiceDetail.setId(id);
                invoiceDetail.setMenu(menuExist);
                invoiceDetail.setInvoice(toInvoice);
                invoiceDetail.setQuantity(menu.getQuantity());
                totalAmountInvoiceTo += menu.getQuantity() *
                        menuExist.getCurrentPrice();
                invoiceDetail.setTotalPrice(menu.getQuantity() *
                        menuExist.getCurrentPrice());
                invoiceDetail.setIsDeleted(false);
                invoiceDetailRepository.save(invoiceDetail);
            }

            toInvoice.setTotalAmount(totalAmountInvoiceTo);
            toInvoice.setStatus(InvoiceStatus.UPDATED);
            invoiceRepository.save(toInvoice);

            // Cập nhật bàn đích thành OCCUPIED
            tableTo.setStatus(TableStatus.OCCUPIED);
            tableRepository.save(tableTo);
        } else {
            // Nếu hóa đơn bàn đích đã có → cộng dồn món
            List<InvoiceDetail> invoiceDetailOfInvoiceTo = invoiceDetailRepository
                    .findAllByInvoice_InvoiceIdAndIsDeletedFalse(toInvoice.getInvoiceId());
            totalAmountInvoiceTo = updateInvoiceDetails(invoiceDetailOfInvoiceTo, cutTableDto.getMenu(),
                    toInvoice);

            toInvoice.setTotalAmount(totalAmountInvoiceTo);

            toInvoice.setStatus(InvoiceStatus.UPDATED);

            invoiceRepository.save(toInvoice);

            invoiceDetailRepository.saveAll(invoiceDetailOfInvoiceTo);

        }

        // Trừ món bên bàn nguồn
        List<InvoiceDetail> invoiceDetailOfInvoiceFrom = fromInvoice.getInvoiceDetails();

        boolean allZero = subtractInvoiceDetails(invoiceDetailOfInvoiceFrom,
                cutTableDto.getMenu());

        // Tính lại tổng tiền bàn nguồn
        double totalAmountFrom = invoiceDetailOfInvoiceFrom.stream()
                .mapToDouble(InvoiceDetail::getTotalPrice)
                .sum();
        fromInvoice.setTotalAmount(totalAmountFrom);
        invoiceRepository.save(fromInvoice);
        invoiceDetailRepository.saveAll(invoiceDetailOfInvoiceFrom);

        // Nếu tất cả món đã trừ hết → bàn nguồn trống
        if (allZero) {
            tableFrom.setStatus(TableStatus.AVAILABLE);
            tableRepository.save(tableFrom);

            bookingDetailFromExist.setIsDeleted(true);
            bookingDetailRepository.save(bookingDetailFromExist);

            fromInvoice.setStatus(InvoiceStatus.CANCELLED);
            invoiceRepository.save(fromInvoice);
        }

        // Đánh dấu các món có quantity = 0 là đã xóa
        for (InvoiceDetail detail : invoiceDetailOfInvoiceFrom) {
            if (detail.getQuantity() == 0) {
                detail.setIsDeleted(true);
            }
        }
        invoiceDetailRepository.saveAll(invoiceDetailOfInvoiceFrom);
        createBookingDetail(tableTo, toInvoice,
                bookingDetailFromExist.getCustomerName(),
                bookingDetailFromExist.getCustomerPhone());
    }

    /**
     * process payment for a table's invoice
     * 
     * @param paymentDto
     * @throws NullPointerException, RuntimeException
     */
    @Transactional
    public void paymentService(PaymentDto paymentDto) {
        if (paymentDto == null) {
            throw new NullPointerException("paymentDto not found");
        }

        if (paymentDto.getTableId() == null) {
            throw new NullPointerException("tableId not found in paymentDto");
        }

        TableEntity table = getTableEntityById(paymentDto.getTableId());

        List<InvoiceStatus> unpaidStatuses = List.of(InvoiceStatus.UPDATED);

        Invoice invoiceOfTable = getInvoiceByTableIdAndStatusInvoice(table.getTableId(), unpaidStatuses);

        Double totalAmount = invoiceOfTable.getTotalAmount();

        if (paymentDto.getVoucherId() != null) {
            Voucher voucher = voucherRepository.findById(paymentDto.getVoucherId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy voucher."));

            if (Boolean.TRUE.equals(voucher.getIsDeleted())) {
                throw new RuntimeException("Voucher không hợp lệ hoặc đã được sử dụng.");
            }
            LocalDate now = LocalDate.now();
            if (voucher.getStartDate().isAfter(now) || voucher.getEndDate().isBefore(now)) {
                throw new RuntimeException("Voucher đã hết hạn sử dụng.");
            }

            Double discountRate = voucher.getDiscountValue() / 100;
            Double totalAfterDiscount = totalAmount * (1 - discountRate);

            totalAfterDiscount = Math.round(totalAfterDiscount * 100.0) / 100.0;

            invoiceOfTable.setVoucher(voucher);
            invoiceOfTable.setTotalAmount(totalAfterDiscount);

            voucherRepository.save(voucher);
        }

        else {
            invoiceOfTable.setTotalAmount(totalAmount);
        }

        invoiceOfTable.setStatus(InvoiceStatus.PAID);
        invoiceOfTable.setTransactionDate(LocalDate.now());
        invoiceRepository.save(invoiceOfTable);

        table.setStatus(TableStatus.AVAILABLE);
        tableRepository.save(table);
    }

    private Invoice createInvoice() {
        Invoice invoice = new Invoice();
        invoice.setTotalAmount(0.0);
        invoice.setTransactionDate(LocalDate.now());
        invoice.setStatus(InvoiceStatus.CREATED);
        invoice.setVoucher(null);
        invoice.setIsDeleted(false);
        return invoiceRepository.save(invoice);
    }

    private TableBookingDetail createBookingDetail(TableEntity table, Invoice invoice, String customerName,
            String customerPhone) {
        Employee employee = getCurrentEmployee();
        TableBookingDetail tableBookingDetail = new TableBookingDetail();

        TableBookingDetailId id = new TableBookingDetailId(table.getTableId(), employee.getEmployeeId(),
                invoice.getInvoiceId());

        tableBookingDetail.setTableBookingDetailId(id);
        tableBookingDetail.setTable(table);
        tableBookingDetail.setEmployee(employee);
        tableBookingDetail.setInvoice(invoice);
        tableBookingDetail.setCustomerName(customerName);
        tableBookingDetail.setCustomerPhone(customerPhone);
        tableBookingDetail.setBookingTime(LocalDateTime.now());
        tableBookingDetail.setIsDeleted(false);
        return bookingDetailRepository.save(tableBookingDetail);
    };

    private Double updateInvoiceDetails(List<InvoiceDetail> invoiceDetails,
            List<Menu> menus,
            Invoice invoice) {

        Double totalAmount = 0.0;

        for (Menu menu : menus) {
            boolean found = false;

            for (InvoiceDetail detail : invoiceDetails) {
                // CHỈ kiểm tra món cùng ID và chưa bị xóa
                if (detail.getMenu().getMenuId().equals(menu.getMenuId())) {

                    found = true;

                    int newQuantity = detail.getQuantity() + menu.getQuantity();
                    detail.setQuantity(newQuantity);

                    double unitPrice = detail.getMenu().getCurrentPrice();
                    totalAmount += unitPrice * newQuantity;
                    detail.setTotalPrice(unitPrice * newQuantity);

                    break;
                }
            }

            if (!found) {
                com.windy.cafemanagement.models.Menu menuExist = menuRepository.findById(menu.getMenuId())
                        .orElseThrow(() -> new RuntimeException(
                                "Không tìm thấy món ăn với id: " + menu.getMenuId()));

                InvoiceDetail newDetail = new InvoiceDetail();
                InvoiceDetailId newId = new InvoiceDetailId(invoice.getInvoiceId(), menu.getMenuId());
                newDetail.setId(newId);
                newDetail.setMenu(menuExist);
                newDetail.setInvoice(invoice);
                newDetail.setQuantity(menu.getQuantity());
                newDetail.setIsDeleted(false);

                double totalPrice = menuExist.getCurrentPrice() * menu.getQuantity();
                totalAmount += totalPrice;
                newDetail.setTotalPrice(totalPrice);

                invoiceDetailRepository.save(newDetail);
                invoiceDetails.add(newDetail);
            }
        }

        return totalAmount;
    }

    private boolean subtractInvoiceDetails(List<InvoiceDetail> invoiceDetails, List<Menu> menus) {
        Map<Long, InvoiceDetail> detailMap = invoiceDetails.stream()
                .collect(Collectors.toMap(d -> d.getMenu().getMenuId(), d -> d));

        for (Menu menu : menus) {
            InvoiceDetail detail = detailMap.get(menu.getMenuId());
            if (detail == null) {
                throw new IllegalArgumentException(
                        "Món với id " + menu.getMenuId() + " không tồn tại trên hóa đơn nguồn");
            }
            if (menu.getQuantity() > detail.getQuantity()) {
                throw new IllegalArgumentException("Số lượng cần cắt lớn hơn có sẵn cho menuId " + menu.getMenuId());
            }
            int newQty = detail.getQuantity() - menu.getQuantity();
            detail.setQuantity(newQty);
            detail.setTotalPrice(detail.getMenu().getCurrentPrice() * newQty);
        }

        boolean allZero = true;
        for (InvoiceDetail detail : invoiceDetails) {
            if (detail.getQuantity() > 0) {
                allZero = false;
                break;
            }
        }

        return allZero;
    }

    private TableEntity getTableEntityById(Long id) {
        return tableRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy bàn với id: " + id));
    }

    private Invoice getInvoiceByTableIdAndStatusInvoice(Long tableId, List<InvoiceStatus> UNPAID_STATUSES) {
        return invoiceRepository
                .findCurrentUnpaidInvoiceByTableId(tableId, UNPAID_STATUSES)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Không tìm thấy hóa đơn chưa thanh toán của bàn ID: " + tableId));
    }

    private Employee getCurrentEmployee() {
        String username = SecurityUtil.getSessionUser();
        return employeeRepository.findByUsername(username);
    }

    private TableBookingDetail getBookingDetailByTableIdAndStatusInvoice(Long id, List<InvoiceStatus> invoiceStatus,
            TableStatus tableStatus) {
        return bookingDetailRepository
                .findCurrentActiveByTableId(
                        id,
                        invoiceStatus,
                        tableStatus)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Không tìm thấy thông tin chi tiết đặt bàn với id: " + id));

    }

    /**
     * Change the status of the reserved table
     * 
     * Only release reservations that:
     * 1. Have passed their booking time
     * 2. Have no active invoice or invoice is already deleted
     * 
     * @throws NullPointerException, RuntimeException
     */
    @Transactional
    public void releaseExpiredBookings() {
        try {
            System.out.println("Run here");
            LocalDateTime currentTime = LocalDateTime.now();
            List<TableEntity> tables = tableRepository.findExpiredBookings(currentTime);

            if (tables.isEmpty()) {
                logger.info("No expired bookings found at {}", currentTime);
                return;
            }

            logger.info("Found {} expired bookings to release", tables.size());

            for (TableEntity table : tables) {
                logger.debug("Processing expired booking for table ID: {}", table.getTableId());

                table.setStatus(TableStatus.AVAILABLE);

                table.getTableBookingDetails().stream()
                        .filter(detail -> !detail.getIsDeleted())
                        .forEach(detail -> {
                            detail.setIsDeleted(true);
                            logger.debug("Marked booking detail as deleted for table ID: {}, invoice ID: {}",
                                    table.getTableId(),
                                    detail.getInvoice() != null ? detail.getInvoice().getInvoiceId() : "N/A");

                            if (detail.getInvoice() != null && !detail.getInvoice().getIsDeleted()) {
                                detail.getInvoice().setIsDeleted(true);
                                invoiceRepository.save(detail.getInvoice());
                                logger.debug("Marked invoice as deleted for invoice ID: {}",
                                        detail.getInvoice().getInvoiceId());
                            }
                        });

                tableRepository.save(table);
                logger.info("Released expired booking for table ID: {}, status changed to AVAILABLE",
                        table.getTableId());
            }

            logger.info("Successfully released {} expired bookings", tables.size());
        } catch (Exception e) {
            logger.error("Error occurred while releasing expired bookings", e);
            throw new RuntimeException("Failed to release expired bookings: " + e.getMessage(), e);
        }
    }

}
