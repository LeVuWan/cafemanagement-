package com.windy.cafemanagement.Services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.windy.cafemanagement.configs.SecurityUtil;
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
        product.setQuantity(dto.getQuantity().intValue());
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
        importOrder.setQuantity(dto.getQuantity().intValue());
        importOrder.setTotalAmount(dto.getQuantity() * dto.getUnitPrice());
        importOrder.setEmployee(user);
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

        return exportOrderRepository.save(exportOrder);
    }

}
