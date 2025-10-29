package com.windy.cafemanagement.Services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
import com.windy.cafemanagement.dto.Menus;
import com.windy.cafemanagement.dto.MergeTableDto;
import com.windy.cafemanagement.dto.MoveTableDto;
import com.windy.cafemanagement.dto.OrderTableDto;
import com.windy.cafemanagement.enums.InvoiceStatus;
import com.windy.cafemanagement.enums.TableStatus;
import com.windy.cafemanagement.models.Employee;
import com.windy.cafemanagement.models.Invoice;
import com.windy.cafemanagement.models.InvoiceDetail;
import com.windy.cafemanagement.models.Menu;
import com.windy.cafemanagement.models.TableBookingDetail;
import com.windy.cafemanagement.models.TableEntity;
import com.windy.cafemanagement.repositories.EmployeeRepository;
import com.windy.cafemanagement.repositories.InvoiceDetailRepository;
import com.windy.cafemanagement.repositories.InvoiceRepository;
import com.windy.cafemanagement.repositories.MenuRepository;
import com.windy.cafemanagement.repositories.TableBookingDetailRepository;
import com.windy.cafemanagement.repositories.TableRepository;

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

            List<InvoiceDetail> allDetails = invoiceDetailRepository
                    .findAllByInvoice_InvoiceIdAndIsDeletedFalse(invoice.getInvoiceId());

            double totalAmount = allDetails.stream()
                    .mapToDouble(InvoiceDetail::getTotalPrice)
                    .sum();

            invoice.setTotalAmount(totalAmount);

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

    public List<TableInforRes> getTableToMoveService(Long tableId) {
        return tableRepository.findAllActiveExcept(tableId);
    }

    @Transactional
    public void moveTableService(MoveTableDto moveTableDto) {
        TableEntity selectedTable = tableRepository.findById(moveTableDto.getToTableId())
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy bàn muốn chuyển đến"));

        TableEntity fromTable = tableRepository.findById(moveTableDto.getFromTableId())
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy bàn hiện tại"));

        TableBookingDetail currentBooking = bookingDetailRepository
                .findCurrentActiveByTableId(moveTableDto.getFromTableId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Không tìm thấy thông tin chi tiết đặt bàn của bàn với id: " + moveTableDto.getFromTableId()));

        selectedTable.setStatus(TableStatus.OCCUPIED);
        fromTable.setStatus(TableStatus.AVAILABLE);
        tableRepository.saveAll(List.of(selectedTable, fromTable));

        currentBooking.setTable(selectedTable);
        bookingDetailRepository.save(currentBooking);
    }

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

    @Transactional
    public void mergeTableService(MergeTableDto mergeTableDto) {

        TableEntity tableTo = tableRepository.findById(mergeTableDto.getTableToId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bàn cần gộp đến"));

        if (tableTo.getStatus() != TableStatus.AVAILABLE) {
            throw new RuntimeException("Bàn muốn gộp đến đang được sử dụng, không thể gộp");
        }

        tableTo.setStatus(TableStatus.OCCUPIED);
        tableRepository.save(tableTo);

        String username = SecurityUtil.getSessionUser();
        Employee employee = employeeRepository.findByUsername(username);

        Invoice newInvoice = new Invoice();
        newInvoice.setTransactionDate(LocalDate.now());
        newInvoice.setStatus(InvoiceStatus.UPDATED);
        newInvoice.setVoucher(null);
        newInvoice.setIsDeleted(false);
        invoiceRepository.save(newInvoice);

        Map<Long, InvoiceDetail> mergedDetailMap = new HashMap<>();

        List<InvoiceStatus> unpaidStatuses = List.of(InvoiceStatus.UPDATED, InvoiceStatus.CREATED);

        for (Long tableFromId : mergeTableDto.getListIdTableFrom()) {
            TableEntity tableFrom = tableRepository.findById(tableFromId)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy bàn cần gộp với ID: " + tableFromId));

            Invoice oldInvoice = invoiceRepository
                    .findCurrentUnpaidInvoiceByTableId(tableFromId, unpaidStatuses)
                    .orElseThrow(() -> new RuntimeException(
                            "Không tìm thấy hóa đơn chưa thanh toán của bàn ID: " + tableFromId));

            List<InvoiceDetail> oldDetails = invoiceDetailRepository
                    .findAllByInvoice_InvoiceIdAndIsDeletedFalse(oldInvoice.getInvoiceId());

            for (InvoiceDetail detail : oldDetails) {
                Long menuId = detail.getMenu().getMenuId();

                if (mergedDetailMap.containsKey(menuId)) {
                    InvoiceDetail existing = mergedDetailMap.get(menuId);
                    existing.setQuantity(existing.getQuantity() + detail.getQuantity());
                    existing.setTotalPrice(existing.getTotalPrice() + detail.getTotalPrice());
                } else {
                    InvoiceDetail newDetail = new InvoiceDetail();
                    newDetail.setMenu(detail.getMenu());
                    newDetail.setInvoice(newInvoice);
                    newDetail.setQuantity(detail.getQuantity());
                    newDetail.setTotalPrice(detail.getTotalPrice());
                    newDetail.setIsDeleted(false);
                    mergedDetailMap.put(menuId, newDetail);
                }
            }

            oldInvoice.setStatus(InvoiceStatus.CANCELLED);
            invoiceRepository.save(oldInvoice);

            tableFrom.setStatus(TableStatus.AVAILABLE);
            tableRepository.save(tableFrom);
        }

        List<InvoiceDetail> mergedDetails = new ArrayList<>(mergedDetailMap.values());
        invoiceDetailRepository.saveAll(mergedDetails);

        TableBookingDetail bookingDetail = new TableBookingDetail();
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
        invoiceRepository.save(newInvoice);
    }

    @Transactional
    public void cancelTableService(Long tableId) {
        TableEntity tableExist = tableRepository.findById(tableId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bàn với ID: " + tableId));

        List<InvoiceStatus> unpaidStatuses = List.of(InvoiceStatus.UPDATED, InvoiceStatus.CREATED);

        Invoice invoiceExist = invoiceRepository
                .findCurrentUnpaidInvoiceByTableId(tableId, unpaidStatuses)
                .orElseThrow(() -> new RuntimeException(
                        "Không tìm thấy hóa đơn chưa thanh toán của bàn ID: " + tableId));

        List<InvoiceDetail> invoiceDetails = invoiceDetailRepository
                .findAllByInvoice_InvoiceIdAndIsDeletedFalse(invoiceExist.getInvoiceId());

        TableBookingDetail tableBookingDetail = bookingDetailRepository
                .findActiveByTableIdAndInvoiceId(tableId, invoiceExist.getInvoiceId())
                .orElseThrow(() -> new RuntimeException(
                        "Không tìm thấy thông tin khách hàng của bàn ID: " + tableId));

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

    @Transactional
    public void cutTableService(CutTableDto cutTableDto) {
        // Lấy bàn nguồn
        TableEntity tableFrom = tableRepository.findById(cutTableDto.getFromTableId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bàn hiện tại"));

        if (tableFrom.getStatus() == TableStatus.AVAILABLE) {
            throw new RuntimeException("Bàn hiện tại đang trống");
        }

        // Lấy bàn đích
        TableEntity tableTo = tableRepository.findById(cutTableDto.getToTableId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bàn cần tách đến"));

        List<InvoiceStatus> unpaidStatuses = List.of(InvoiceStatus.UPDATED, InvoiceStatus.CREATED);

        // Lấy hóa đơn bàn nguồn
        Invoice fromInvoice = invoiceRepository
                .findCurrentUnpaidInvoiceByTableId(tableFrom.getTableId(), unpaidStatuses)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hóa đơn của bàn hiện tại"));

        // Lấy thông tin đặt bàn nguồn
        TableBookingDetail bookingDetailFromExist = bookingDetailRepository
                .findActiveByTableIdAndInvoiceId(tableFrom.getTableId(), fromInvoice.getInvoiceId())
                .orElseThrow(() -> new RuntimeException(
                        "Không tìm thấy thông tin chi tiết đặt bàn của bàn ID: " + tableFrom.getTableId()));

        // Lấy hoặc tạo hóa đơn bàn đích
        Invoice toInvoice = invoiceRepository
                .findCurrentUnpaidInvoiceByTableId(tableTo.getTableId(), unpaidStatuses)
                .orElseGet(this::createInvoice);

        // Nếu hóa đơn bàn đích chưa được cập nhật món (mới tạo)
        if (toInvoice.getStatus() == InvoiceStatus.CREATED) {
            for (Menus menu : cutTableDto.getMenus()) {
                Menu menuExist = menuRepository.findById(menu.getMenuId())
                        .orElseThrow(() -> new RuntimeException(
                                "Không tìm thấy món ăn với id: " + menu.getMenuId()));

                InvoiceDetail invoiceDetail = new InvoiceDetail();
                invoiceDetail.setMenu(menuExist);
                invoiceDetail.setInvoice(toInvoice);
                invoiceDetail.setQuantity(menu.getQuantity());
                invoiceDetail.setTotalPrice(menu.getQuantity() * menuExist.getCurrentPrice());
                invoiceDetail.setIsDeleted(false);
                invoiceDetailRepository.save(invoiceDetail);
            }

            // Tính lại tổng tiền bàn đích
            List<InvoiceDetail> invoiceDetailOfInvoiceTo = invoiceDetailRepository
                    .findAllByInvoice_InvoiceIdAndIsDeletedFalse(toInvoice.getInvoiceId());

            double totalAmount = invoiceDetailOfInvoiceTo.stream()
                    .mapToDouble(InvoiceDetail::getTotalPrice)
                    .sum();
            toInvoice.setTotalAmount(totalAmount);
            toInvoice.setStatus(InvoiceStatus.UPDATED);
            invoiceRepository.save(toInvoice);

            // Cập nhật bàn đích thành OCCUPIED
            tableTo.setStatus(TableStatus.OCCUPIED);
            tableRepository.save(tableTo);

            // Tạo mới booking detail bàn đích nếu chưa có
            bookingDetailRepository
                    .findActiveByTableIdAndInvoiceId(tableTo.getTableId(), toInvoice.getInvoiceId())
                    .orElseGet(() -> createBookingDetail(
                            tableTo,
                            toInvoice,
                            bookingDetailFromExist.getCustomerName(),
                            bookingDetailFromExist.getCustomerPhone()));

        } else {
            // Nếu hóa đơn bàn đích đã có → cộng dồn món
            List<InvoiceDetail> invoiceDetailOfInvoiceTo = invoiceDetailRepository
                    .findAllByInvoice_InvoiceIdAndIsDeletedFalse(toInvoice.getInvoiceId());

            updateInvoiceDetails(invoiceDetailOfInvoiceTo, cutTableDto.getMenus(), toInvoice);

            double totalAmount = invoiceDetailOfInvoiceTo.stream()
                    .mapToDouble(InvoiceDetail::getTotalPrice)
                    .sum();
            toInvoice.setTotalAmount(totalAmount);
            toInvoice.setStatus(InvoiceStatus.UPDATED);
            invoiceRepository.save(toInvoice);

            invoiceDetailRepository.saveAll(invoiceDetailOfInvoiceTo);
        }

        // Trừ món bên bàn nguồn
        List<InvoiceDetail> invoiceDetailOfInvoiceFrom = invoiceDetailRepository
                .findAllByInvoice_InvoiceIdAndIsDeletedFalse(fromInvoice.getInvoiceId());

        boolean allZero = subtractInvoiceDetails(invoiceDetailOfInvoiceFrom, cutTableDto.getMenus());

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
    }

    private Employee getCurrentEmployee() {
        String username = SecurityUtil.getSessionUser();
        return employeeRepository.findByUsername(username);
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
        TableBookingDetail tableBookingDetail = new TableBookingDetail();

        tableBookingDetail.setTable(table);
        tableBookingDetail.setEmployee(getCurrentEmployee());
        tableBookingDetail.setInvoice(invoice);
        tableBookingDetail.setCustomerName(customerName);
        tableBookingDetail.setCustomerPhone(customerPhone);
        tableBookingDetail.setBookingTime(LocalDateTime.now());
        tableBookingDetail.setIsDeleted(false);
        return bookingDetailRepository.save(tableBookingDetail);
    };

    private void updateInvoiceDetails(List<InvoiceDetail> invoiceDetails,
            List<Menus> menus,
            Invoice invoice) {

        for (Menus menu : menus) {
            boolean found = false;
            for (InvoiceDetail detail : invoiceDetails) {
                if (detail.getMenu().getMenuId().equals(menu.getMenuId())) {
                    found = true;

                    int newQuantity = detail.getQuantity() + menu.getQuantity();
                    detail.setQuantity(newQuantity);

                    double unitPrice = detail.getMenu().getCurrentPrice();
                    detail.setTotalPrice(unitPrice * newQuantity);

                    break;
                }
            }

            if (!found) {
                Menu menuExist = menuRepository.findById(menu.getMenuId())
                        .orElseThrow(() -> new RuntimeException(
                                "Không tìm thấy món ăn với id: " + menu.getMenuId()));

                InvoiceDetail newDetail = new InvoiceDetail();
                newDetail.setMenu(menuExist);
                newDetail.setInvoice(invoice);
                newDetail.setQuantity(menu.getQuantity());
                newDetail.setIsDeleted(false);

                double totalPrice = menuExist.getCurrentPrice() * menu.getQuantity();
                newDetail.setTotalPrice(totalPrice);

                invoiceDetails.add(newDetail);
            }
        }
    }

    private boolean subtractInvoiceDetails(List<InvoiceDetail> invoiceDetails, List<Menus> menus) {
        for (InvoiceDetail detail : invoiceDetails) {
            for (Menus menu : menus) {
                System.out
                        .println("Menu id from: " + detail.getMenu().getMenuId() + "Menu id from: " + menu.getMenuId());
                if (detail.getMenu().getMenuId().equals(menu.getMenuId())) {
                    if (menu.getQuantity() > detail.getQuantity()) {
                        throw new IllegalArgumentException(
                                "Số lượng món '" + detail.getMenu().getMenuId() +
                                        "' cần trừ (" + menu.getQuantity() +
                                        ") nhiều hơn số lượng hiện có (" + detail.getQuantity() + ")");
                    }

                    int newQuantity = detail.getQuantity() - menu.getQuantity();
                    detail.setQuantity(newQuantity);

                    double unitPrice = detail.getMenu().getCurrentPrice();
                    detail.setTotalPrice(unitPrice * newQuantity);

                    break;
                }
            }
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

}
