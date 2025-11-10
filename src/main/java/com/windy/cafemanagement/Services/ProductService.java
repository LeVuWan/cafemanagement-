package com.windy.cafemanagement.Services;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

import com.windy.cafemanagement.Responses.ImportExportProduct;
import com.windy.cafemanagement.configs.SecurityUtil;
import com.windy.cafemanagement.dto.EditProductDto;
import com.windy.cafemanagement.dto.ExportProductDto;
import com.windy.cafemanagement.dto.ImportProductDto;
import com.windy.cafemanagement.models.Employee;
import com.windy.cafemanagement.models.ExportOrder;
import com.windy.cafemanagement.models.ImportOrder;
import com.windy.cafemanagement.models.Product;
import com.windy.cafemanagement.models.Unit;
import com.windy.cafemanagement.repositories.EmployeeRepository;
import com.windy.cafemanagement.repositories.ExportOrderRepository;
import com.windy.cafemanagement.repositories.ImportOrderRepository;
import com.windy.cafemanagement.repositories.ProductRepository;
import com.windy.cafemanagement.repositories.UnitRepository;

/**
 * ProductService
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
 * 10-11-2025 VuLQ Create
 */
@Service
public class ProductService {
    private final UnitRepository unitRepository;
    private final ProductRepository productRepository;
    private final ImportOrderRepository importOrderRepository;
    private final EmployeeRepository employeeRepository;
    private final ExportOrderRepository exportOrderRepository;

    public ProductService(UnitRepository unitRepository, ImportOrderRepository importOrderRepository,
            ProductRepository productRepository, EmployeeRepository employeeRepository,
            ExportOrderRepository exportOrderRepository) {
        this.unitRepository = unitRepository;
        this.importOrderRepository = importOrderRepository;
        this.productRepository = productRepository;
        this.employeeRepository = employeeRepository;
        this.exportOrderRepository = exportOrderRepository;
    }

    /**
     * get list unit
     * 
     * @return List<Unit>
     * @throws DataAccessException
     */
    public List<Unit> getAllUnitService() throws DataAccessException {
        return unitRepository.findAll();
    }

    /**
     * get list product by isDeleted = false
     * 
     * @return List<Product>
     * @throws DataAccessException
     */
    public List<Product> getAllProductService() throws DataAccessException {
        return productRepository.findAllByIsDeleted(false);
    }

    /**
     * add new product and create import order
     * 
     * @param dto
     * @return Product
     * @throws DataAccessException, EntityNotFoundException, SecurityException
     */
    @Transactional
    public Product addNewProductAndImportOrder(ImportProductDto dto)
            throws DataAccessException, EntityNotFoundException, SecurityException {
        Unit unit = unitRepository.findById(dto.getUnitId())
                .orElseThrow(
                        () -> new EntityNotFoundException("Không tìm thấy đơn vị tính với ID: " + dto.getUnitId()));

        Product product = new Product();
        product.setProductName(dto.getProductName());
        product.setQuantity(dto.getQuantity());
        product.setUnitPrice(dto.getUnitPrice());
        product.setUnit(unit);
        product.setIsDeleted(false);

        Product savedProduct = productRepository.save(product);

        String username = SecurityUtil.getSessionUser();

        if (username == null) {
            throw new SecurityException("Người dùng chưa đăng nhập");
        }

        Employee user = employeeRepository.findByUsername(username);

        if (user == null) {
            throw new EntityNotFoundException("Không tìm thấy nhân viên với username: " + username);
        }

        ImportOrder importOrder = new ImportOrder();
        importOrder.setProduct(savedProduct);
        importOrder.setEquipment(null);
        importOrder.setImportDate(dto.getPurchaseDate());
        importOrder.setQuantity(dto.getQuantity());
        importOrder.setTotalAmount(dto.getQuantity() * dto.getUnitPrice());
        importOrder.setEmployee(user);
        importOrder.setIsDeleted(false);
        importOrderRepository.save(importOrder);

        return savedProduct;
    }

    @Transactional
    public ExportOrder createExportOrder(ExportProductDto dto)
            throws DataAccessException, NullPointerException, SecurityException {
        Product product = productRepository.findByProductIdAndIsDeletedFalse(dto.getProductId())
                .orElseThrow(() -> new NullPointerException("Sản phẩm không tồn tại hoặc đã bị xóa"));

        if (product.getQuantity() < dto.getQuantity()) {
            throw new RuntimeException("Số lượng tồn kho không đủ để xuất");
        }

        product.setQuantity(product.getQuantity() - dto.getQuantity());
        productRepository.save(product);

        String username = SecurityUtil.getSessionUser();

        if (username == null) {
            throw new SecurityException("Người dùng chưa đăng nhập");
        }

        Employee user = employeeRepository.findByUsername(username);

        if (user == null) {
            throw new NullPointerException("Không tìm thấy nhân viên với username: " + username);
        }

        ExportOrder exportOrder = new ExportOrder();
        exportOrder.setEmployee(user);
        exportOrder.setProduct(product);
        exportOrder.setExportDate(dto.getExportDate());
        exportOrder.setQuantity(dto.getQuantity().intValue());
        exportOrder.setTotalExportAmount(product.getUnitPrice() * dto.getQuantity());
        exportOrder.setIsDeleted(false);

        return exportOrderRepository.save(exportOrder);
    }

    public List<ImportExportProduct> getImportExportHistory(String keyword) throws DataAccessException{
        if (keyword != null && keyword.trim().isEmpty()) {
            keyword = null;
        }

        List<Map<String, Object>> result = productRepository.findImportExportHistoryNative(keyword);

        return result.stream()
                .map(row -> new ImportExportProduct(
                        (String) row.get("nameProduct"),
                        row.get("importDate") != null ? ((java.sql.Date) row.get("importDate")).toLocalDate() : null,
                        row.get("exportDate") != null ? ((java.sql.Date) row.get("exportDate")).toLocalDate() : null,
                        row.get("quantity") != null ? ((Number) row.get("quantity")).intValue() : null,
                        (String) row.get("unit"),
                        row.get("totalPrice") != null ? ((Number) row.get("totalPrice")).doubleValue() : null))
                .collect(Collectors.toList());
    }


    public List<Product> getAllProduct() throws DataAccessException{
        return productRepository.findAllByIsDeleted(false);
    }

    public Product getProductByIdService(Long id) {
        return productRepository.findByProductIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new EntityNotFoundException("Sản phẩm không tồn tại hoặc đã bị xóa"));
    }

    public void updateProductService(EditProductDto dto) {
        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hàng hóa với id: " + dto.getProductId()));

        product.setProductName(dto.getProductName());
        product.setUnitPrice(dto.getUnitPrice());
        product.setUnit(unitRepository.findById(dto.getUnitId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn vị tính!")));

        productRepository.save(product);

        List<ImportOrder> importOrders = importOrderRepository.findByProduct(product);

        for (ImportOrder importOrder : importOrders) {
            double newTotal = importOrder.getQuantity() * product.getUnitPrice();
            importOrder.setTotalAmount(newTotal);
        }

        importOrderRepository.saveAll(importOrders);

        List<ExportOrder> exportOrders = exportOrderRepository.findByProduct(product);

        for (ExportOrder exportOrder : exportOrders) {
            double newTotal = exportOrder.getQuantity() * product.getUnitPrice();
            exportOrder.setTotalExportAmount(newTotal);
        }
        exportOrderRepository.saveAll(exportOrders);

    }

    @Transactional
    public void importExistingProduct(ExportProductDto dto) {
        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hàng hóa có ID: " + dto.getProductId()));

        Double newQuantity = product.getQuantity() + dto.getQuantity();
        product.setQuantity(newQuantity);

        double totalAmount = dto.getQuantity() * product.getUnitPrice();
        String username = SecurityUtil.getSessionUser();
        Employee employee = employeeRepository.findByUsername(username);

        ImportOrder importOrder = new ImportOrder();
        importOrder.setProduct(product);
        importOrder.setQuantity(dto.getQuantity());
        importOrder.setImportDate(dto.getExportDate());
        importOrder.setTotalAmount(totalAmount);
        importOrder.setEmployee(employee);
        importOrder.setIsDeleted(false);

        importOrderRepository.save(importOrder);
        productRepository.save(product);
    }

    @Transactional
    public void softDeleteProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hàng hóa có ID: " + productId));

        product.setIsDeleted(true);
        productRepository.save(product);

        List<ImportOrder> importOrders = importOrderRepository.findByProduct_ProductIdAndIsDeletedFalse(productId);
        List<ExportOrder> exportOrders = exportOrderRepository.findByProduct_ProductIdAndIsDeletedFalse(productId);

        importOrders.forEach(order -> order.setIsDeleted(true));
        exportOrders.forEach(order -> order.setIsDeleted(true));

        importOrderRepository.saveAll(importOrders);
        exportOrderRepository.saveAll(exportOrders);
    }

}
