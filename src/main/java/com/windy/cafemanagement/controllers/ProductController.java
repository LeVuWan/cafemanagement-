package com.windy.cafemanagement.controllers;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.persistence.EntityNotFoundException;

import com.windy.cafemanagement.Responses.ImportExportProduct;
import com.windy.cafemanagement.Services.ProductService;
import com.windy.cafemanagement.dto.EditProductDto;
import com.windy.cafemanagement.dto.ExportProductDto;
import com.windy.cafemanagement.dto.ImportProductDto;
import com.windy.cafemanagement.models.Product;
import com.windy.cafemanagement.models.Unit;
import com.windy.cafemanagement.repositories.ProductRepository;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin/product")
/**
 * ProductController
 *
 * Version 1.0
 *
 * Date: 12-11-2013
 *
 * Copyright
 *
 * Modification Logs:
 * DATE AUTHOR DESCRIPTION
 * -----------------------------------------------------------------------
 */
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * Show the import product form.
     * 
     * @param model the model for the view
     * @return view name for import product
     */
    @GetMapping("/import")
    public String getFormCreateProduct(Model model) {
        try {
            model.addAttribute("importProduct", new ImportProductDto());
            model.addAttribute("units", productService.getAllUnitService());
            return "admin/product/import-product";
        } catch (Exception ex) {
            logger.error("Unexpected error while loading import product form: {}", ex.getMessage(), ex);
            model.addAttribute("errorMessage", "Không thể mở form nhập sản phẩm: " + ex.getMessage());
            return "admin/errors/500-error";
        }
    }

    /**
     * Handle import product submission.
     * 
     * @param model            the model for the view
     * @param importProductDto the import payload
     * @param bindingResult    validation result
     * @return redirect on success or view on validation/error
     */
    @PostMapping("/import")
    public String createProduct(Model model, @Valid @ModelAttribute("importProduct") ImportProductDto importProductDto,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("importProduct", importProductDto);
            model.addAttribute("units", productService.getAllUnitService());
            return "admin/product/import-product";
        }
        try {
            productService.addNewProductAndImportOrder(importProductDto);
            return "redirect:/admin/product?success=export";
        } catch (IllegalArgumentException | NullPointerException ex) {
            logger.error("Invalid import product data: {}", ex.getMessage(), ex);
            model.addAttribute("errorMessage", "Dữ liệu không hợp lệ: " + ex.getMessage());
            return "admin/errors/500-error";
        } catch (SecurityException ex) {
            logger.error("Không tìm thấy user đang đăng nhập: {}", ex.getMessage(), ex);
            model.addAttribute("errorMessage", "Không tìm thấy user đang đăng nhập: " + ex.getMessage());
            return "admin/errors/500-error";
        } catch (Exception ex) {
            logger.error("Unexpected error while importing product: {}", ex.getMessage(), ex);
            model.addAttribute("errorMessage", "Nhập sản phẩm thất bại: " + ex.getMessage());
            return "admin/errors/500-error";
        }
    }

    /**
     * Show export product form for given product id.
     * 
     * @param model the model for the view
     * @param id    the product id
     * @return view name for export product
     */
    @GetMapping("/export/{id}")
    public String getFormExportProduct(Model model, @PathVariable("id") Long id) {
        try {
            Product product = productService.getProductByIdService(id);
            ExportProductDto dto = new ExportProductDto();
            dto.setProductId(product.getProductId());

            model.addAttribute("product", product);
            model.addAttribute("exportProduct", dto);
            return "admin/product/export-product";
        } catch (EntityNotFoundException ex) {
            logger.error("Product not found for export id {}: {}", id, ex.getMessage(), ex);
            model.addAttribute("errorMessage", "Không tìm thấy sản phẩm: " + ex.getMessage());
            return "admin/errors/500-error";
        } catch (Exception ex) {
            logger.error("Unexpected error while loading export form for id {}: {}", id, ex.getMessage(), ex);
            model.addAttribute("errorMessage", "Không thể mở form xuất hàng: " + ex.getMessage());
            return "admin/errors/500-error";
        }
    }

    /**
     * Handle export product submission.
     * 
     * @param model            the model for the view
     * @param exportProductDto the export payload
     * @param bindingResult    validation result
     * @return redirect on success or view on validation/error
     */
    @PostMapping("/export")
    public String exportProduct(Model model,
            @Valid @ModelAttribute("exportProduct") ExportProductDto exportProductDto,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            Product product = productService.getProductByIdService(exportProductDto.getProductId());
            model.addAttribute("product", product);
            model.addAttribute("exportProduct", exportProductDto);
            return "admin/product/export-product";
        }

        try {
            productService.createExportOrder(exportProductDto);
            return "redirect:/admin/product?success=edit";

        } catch (IllegalArgumentException | NullPointerException ex) {
            logger.error("Invalid export product data: {}", ex.getMessage(), ex);
            model.addAttribute("errorMessage", "Dữ liệu không hợp lệ: " + ex.getMessage());
            return "admin/errors/500-error";
        } catch (Exception ex) {
            logger.error("Unexpected error while exporting product: {}", ex.getMessage(), ex);
            model.addAttribute("errorMessage", "Xuất sản phẩm thất bại: " + ex.getMessage());
            return "admin/errors/500-error";
        }
    }

    /**
     * Show import/export history page.
     * 
     * @param keyword optional search keyword
     * @param model   the model for the view
     * @return view name for history
     */
    @GetMapping("/history-import-export")
    public String showHistory(@RequestParam(required = false) String keyword, Model model) {
        try {
            List<ImportExportProduct> histories = productService.getImportExportHistory(keyword);
            model.addAttribute("histories", histories);
            model.addAttribute("keyword", keyword);
            return "admin/product/import-export-product";
        } catch (Exception ex) {
            logger.error("Unexpected error while loading import/export history: {}", ex.getMessage(), ex);
            model.addAttribute("errorMessage", "Không thể tải lịch sử: " + ex.getMessage());
            return "admin/errors/500-error";
        }
    }

    /**
     * List all products page.
     * 
     * @param model the model for the view
     * @return view name for product list
     */
    @GetMapping("")
    public String listProductController(Model model) {
        try {
            model.addAttribute("products", productService.getAllProduct());
            return "admin/product/list-product";
        } catch (Exception ex) {
            logger.error("Unexpected error while listing products: {}", ex.getMessage(), ex);
            model.addAttribute("errorMessage", "Lấy danh sách sản phẩm thất bại: " + ex.getMessage());
            return "admin/errors/500-error";
        }
    }

    /**
     * Show edit product form.
     * 
     * @param model the model for the view
     * @param id    the product id
     * @return view name for edit product
     */
    @GetMapping("/edit/{id}")
    public String getFormEditController(Model model, @PathVariable("id") Long id) {
        try {
            Product product = productService.getProductByIdService(id);
            List<Unit> units = productService.getAllUnitService();

            EditProductDto dto = new EditProductDto();
            dto.setProductId(product.getProductId());
            dto.setProductName(product.getProductName());
            dto.setUnitPrice(product.getUnitPrice());
            dto.setUnitId(product.getUnit().getUnitId());

            model.addAttribute("product", dto);
            model.addAttribute("units", units);
            return "admin/product/edit-product";
        } catch (EntityNotFoundException ex) {
            logger.error("Product not found for edit id {}: {}", id, ex.getMessage(), ex);
            model.addAttribute("errorMessage", "Không tìm thấy sản phẩm: " + ex.getMessage());
            return "admin/errors/500-error";
        } catch (Exception ex) {
            logger.error("Unexpected error while loading edit form for id {}: {}", id, ex.getMessage(), ex);
            model.addAttribute("errorMessage", "Không thể mở form chỉnh sửa sản phẩm: " + ex.getMessage());
            return "admin/errors/500-error";
        }
    }

    /**
     * Handle edit product submission.
     * 
     * @param model          the model for the view
     * @param editProductDto the edited product payload
     * @param bindingResult  validation result
     * @return redirect on success or view on validation/error
     */
    @PostMapping("/edit")
    public String editProductController(Model model,
            @Valid @ModelAttribute("product") EditProductDto editProductDto,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("units", productService.getAllUnitService());
            return "admin/product/edit-product";
        }

        try {
            productService.updateProductService(editProductDto);
            return "redirect:/admin/product?success=edit";
        } catch (IllegalArgumentException | NullPointerException ex) {
            logger.error("Invalid product data while updating: {}", ex.getMessage(), ex);
            model.addAttribute("errorMessage", "Dữ liệu không hợp lệ: " + ex.getMessage());
            return "admin/errors/500-error";
        } catch (EntityNotFoundException ex) {
            logger.error("Product not found for edit id {}: ", ex.getMessage(), ex);
            model.addAttribute("errorMessage", "Không tìm thấy sản phẩm: " + ex.getMessage());
            return "admin/errors/500-error";
        } catch (Exception ex) {
            logger.error("Unexpected error while updating product: {}", ex.getMessage(), ex);
            model.addAttribute("errorMessage", "Cập nhật sản phẩm thất bại: " + ex.getMessage());
            return "admin/errors/500-error";
        }
    }

    /**
     * Show import product exist form for a product.
     * 
     * @param model the model for the view
     * @param id    the product id
     * @return view name for import existing product
     */
    @GetMapping("/import-product-exist/{id}")
    public String getFormImportProductExistController(
            Model model, @PathVariable("id") Long id) {
        try {
            Product product = productService.getProductByIdService(id);
            ExportProductDto dto = new ExportProductDto();
            dto.setProductId(product.getProductId());

            model.addAttribute("product", product);
            model.addAttribute("exportProduct", dto);
            return "admin/product/import-product-exist";
        } catch (EntityNotFoundException ex) {
            logger.error("Product not found for import-exist id {}: {}", id, ex.getMessage(), ex);
            model.addAttribute("errorMessage", "Không tìm thấy sản phẩm: " + ex.getMessage());
            return "admin/errors/500-error";
        } catch (Exception ex) {
            logger.error("Unexpected error while loading import-exist form for id {}: {}", id, ex.getMessage(), ex);
            model.addAttribute("errorMessage", "Không thể mở form: " + ex.getMessage());
            return "admin/errors/500-error";
        }
    }

    /**
     * Import to an existing product.
     * 
     * @param model            the model for the view
     * @param exportProductDto the import payload
     * @param bindingResult    validation result
     * @return redirect on success or view on validation/error
     */
    @PostMapping("/import-product-exist")
    public String importProductExistProduct(Model model,
            @Valid @ModelAttribute("exportProduct") ExportProductDto exportProductDto,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            Product product = productService.getProductByIdService(exportProductDto.getProductId());
            model.addAttribute("product", product);
            model.addAttribute("exportProduct", exportProductDto);
            return "admin/product/import-product-exist";
        }

        try {
            productService.importExistingProduct(exportProductDto);
            return "redirect:/admin/product?success=edit";
        } catch (IllegalArgumentException | NullPointerException ex) {
            logger.error("Invalid data while importing to existing product: {}", ex.getMessage(), ex);
            model.addAttribute("errorMessage", "Dữ liệu không hợp lệ: " + ex.getMessage());
            return "admin/errors/500-error";
        } catch (EntityNotFoundException ex) {
            logger.error("Product not found for import-exist id {}: {}", ex.getMessage(), ex);
            model.addAttribute("errorMessage", "Không tìm thấy sản phẩm: " + ex.getMessage());
            return "admin/errors/500-error";
        } catch (Exception ex) {
            logger.error("Unexpected error while importing to existing product: {}", ex.getMessage(), ex);
            model.addAttribute("errorMessage", "Nhập hàng thất bại: " + ex.getMessage());
            return "admin/errors/500-error";
        }
    }

    /**
     * Soft-delete a product by id.
     * 
     * @param model the model for the view
     * @param id    the product id to delete
     * @return redirect to product list or error view on failure
     */
    @GetMapping("/delete/{id}")
    public String deleteProduct(Model model, @PathVariable("id") Long id) {
        try {
            productService.softDeleteProduct(id);
            return "redirect:/admin/product";
        } catch (EntityNotFoundException ex) {
            logger.error("Product not found for delete id {}: {}", id, ex.getMessage(), ex);
            model.addAttribute("errorMessage", "Không tìm thấy sản phẩm để xóa: " + ex.getMessage());
            return "admin/errors/500-error";
        } catch (NullPointerException ex) {
            logger.error("Invalid data while importing to existing product: {}", ex.getMessage(), ex);
            model.addAttribute("errorMessage", "Dữ liệu không hợp lệ: " + ex.getMessage());
            return "admin/errors/500-error";
        } catch (Exception ex) {
            logger.error("Unexpected error while deleting product id {}: {}", id, ex.getMessage(), ex);
            model.addAttribute("errorMessage", "Xóa sản phẩm thất bại: " + ex.getMessage());
            return "admin/errors/500-error";
        }
    }

}
