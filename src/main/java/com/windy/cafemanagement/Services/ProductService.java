package com.windy.cafemanagement.Services;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

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

import jakarta.transaction.Transactional;

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

    public List<Unit> getAllUnitService() {
        return unitRepository.findAll();
    }

    public List<Product> getAllProductService() {
        return productRepository.findAllByIsDeleted(false);
    }

    @Transactional
    public Product addNewProductAndImportOrder(ImportProductDto dto) {
        Unit unit = unitRepository.findById(dto.getUnitId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn vị tính với ID: " + dto.getUnitId()));

        // Tạo Product mới
        Product product = new Product();
        product.setProductName(dto.getProductName());
        product.setQuantity(dto.getQuantity());
        product.setUnitPrice(dto.getUnitPrice());
        product.setUnit(unit);
        product.setIsDeleted(false);

        Product savedProduct = productRepository.save(product);

        String username = SecurityUtil.getSessionUser();
        Employee user = employeeRepository.findByUsername(username);

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
    public ExportOrder createExportOrder(ExportProductDto dto) {
        Product product = productRepository.findByProductIdAndIsDeletedFalse(dto.getProductId())
                .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại hoặc đã bị xóa"));

        if (product.getQuantity() < dto.getQuantity()) {
            throw new RuntimeException("Số lượng tồn kho không đủ để xuất");
        }

        product.setQuantity(product.getQuantity() - dto.getQuantity());
        productRepository.save(product);

        String username = SecurityUtil.getSessionUser();
        Employee employee = employeeRepository.findByUsername(username);

        ExportOrder exportOrder = new ExportOrder();
        exportOrder.setEmployee(employee);
        exportOrder.setProduct(product);
        exportOrder.setExportDate(dto.getExportDate());
        exportOrder.setQuantity(dto.getQuantity().intValue());
        exportOrder.setTotalExportAmount(product.getUnitPrice() * dto.getQuantity());
        exportOrder.setIsDeleted(false);

        return exportOrderRepository.save(exportOrder);
    }

    public List<ImportExportProduct> getImportExportHistory(String keyword) {
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

    public List<Product> getAllProduct() {
        return productRepository.findAllByIsDeleted(false);
    }

    public Product getProductByIdService(Long id) {
        return productRepository.findByProductIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại hoặc đã bị xóa"));
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
        // 1️⃣ Tìm product
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hàng hóa có ID: " + productId));

        // 2️⃣ Đánh dấu Product đã xóa
        product.setIsDeleted(true);
        productRepository.save(product);

        // 3️⃣ Tìm các import/export order liên quan
        List<ImportOrder> importOrders = importOrderRepository.findByProduct_ProductIdAndIsDeletedFalse(productId);
        List<ExportOrder> exportOrders = exportOrderRepository.findByProduct_ProductIdAndIsDeletedFalse(productId);

        // 4️⃣ Đánh dấu isDeleted = true cho chúng
        importOrders.forEach(order -> order.setIsDeleted(true));
        exportOrders.forEach(order -> order.setIsDeleted(true));

        importOrderRepository.saveAll(importOrders);
        exportOrderRepository.saveAll(exportOrders);
    }

}
