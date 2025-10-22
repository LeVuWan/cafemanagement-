package com.windy.cafemanagement.controllers;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
public class ProductController {

    private final ProductRepository productRepository;
    private final ProductService productService;

    public ProductController(ProductService productService, ProductRepository productRepository) {
        this.productService = productService;
        this.productRepository = productRepository;
    }

    @GetMapping("/import")
    public String getFormCreateProduct(Model model) {
        model.addAttribute("importProduct", new ImportProductDto());
        model.addAttribute("units", productService.getAllUnitService());
        return "admin/product/import-product";
    }

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
        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("products", importProductDto);
            model.addAttribute("units", productService.getAllUnitService());
            return "admin/product/export-product";
        }
    }

    @GetMapping("/export/{id}")
    public String getFormExportProduct(Model model, @PathVariable("id") Long id) {
        try {
            Product product = productService.getProductByIdService(id);
            ExportProductDto dto = new ExportProductDto();
            dto.setProductId(product.getProductId());

            model.addAttribute("product", product);
            model.addAttribute("exportProduct", dto);
            return "admin/product/export-product";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("products", productService.getAllProduct());
            return "admin/product/list-product";
        }
    }

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

        } catch (RuntimeException e) {
            Product product = productService.getProductByIdService(exportProductDto.getProductId());
            model.addAttribute("product", product);
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("exportProduct", exportProductDto);
            return "admin/product/export-product";
        }
    }

    @GetMapping("/history-import-export")
    public String showHistory(@RequestParam(required = false) String keyword, Model model) {
        List<ImportExportProduct> histories = productService.getImportExportHistory(keyword);
        model.addAttribute("histories", histories);
        model.addAttribute("keyword", keyword);
        return "admin/product/import-export-product";
    }

    @GetMapping("")
    public String listProductController(Model model) {
        model.addAttribute("products", productService.getAllProduct());
        return "admin/product/list-product";
    }

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
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("products", productService.getAllProduct());
            return "admin/product/list-product";
        }
    }

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
        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("units", productService.getAllUnitService());
            return "admin/product/edit-product";
        }
    }

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
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("products", productService.getAllProduct());
            return "admin/product/list-product";
        }
    }

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
        } catch (RuntimeException e) {
            Product product = productService.getProductByIdService(exportProductDto.getProductId());
            model.addAttribute("product", product);
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("exportProduct", exportProductDto);
            return "admin/product/import-product-exist";
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(Model model, @PathVariable("id") Long id) {
        try {
            productService.softDeleteProduct(id);
            return "redirect:/admin/product";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("products", productService.getAllProduct());
            return "admin/product/list-product";
        }
    }

}
