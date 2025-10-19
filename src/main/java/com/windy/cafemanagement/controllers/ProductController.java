package com.windy.cafemanagement.controllers;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.windy.cafemanagement.Services.ProductService;
import com.windy.cafemanagement.dto.ExportProductDto;
import com.windy.cafemanagement.dto.ImportProductDto;
import com.windy.cafemanagement.models.Product;
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

    @GetMapping("")
    public String getImportAndExportProduct(Model model) {

        return "";
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
            return "redirect:/admin?success=export";
        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("products", importProductDto);
            model.addAttribute("units", productService.getAllUnitService());
            return "admin/product/export-product";
        }
    }

    @GetMapping("/export")
    public String getFormExportProduct(Model model) {
        List<Product> products = productService.getAllProductService();
        model.addAttribute("exportProduct", new ExportProductDto());
        model.addAttribute("products", products);
        return "admin/product/export-product";
    }

    @PostMapping("/export")
    public String exportProduct(Model model,
            @Valid @ModelAttribute("exportProduct") ExportProductDto exportProductDto, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("products", productService.getAllProductService());
            model.addAttribute("exportProduct", exportProductDto);
            return "admin/product/export-product";
        }

        try {
            productService.createExportOrder(exportProductDto);

            return "redirect:/admin?success=export";

        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("products", productService.getAllProductService());
            model.addAttribute("exportProduct", exportProductDto);
            return "admin/product/export-product";
        }
    }

}
