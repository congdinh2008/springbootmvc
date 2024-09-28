package com.congdinh.springbootmvc.controllers;

import java.util.UUID;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.congdinh.springbootmvc.dtos.product.ProductCreateDTO;
import com.congdinh.springbootmvc.dtos.product.ProductDTO;
import com.congdinh.springbootmvc.services.CategoryService;
import com.congdinh.springbootmvc.services.ProductService;

@Controller
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;
    private final CategoryService categoryService;

    public ProductController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @GetMapping
    public String index(
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "2") int size,
            Model model) {
        var pageable = PageRequest.of(page, size);
        var products = productService.search(keyword, pageable);
        // Keep keyword in input field
        model.addAttribute("keyword", keyword);
        model.addAttribute("products", products);
        return "products/index";
    }

    // Render Create Product form
    @GetMapping("/create")
    public String create(Model model) {
        var categories = categoryService.findAll();
        model.addAttribute("categories", categories);
        return "products/create";
    }

    // Retrieve Product data from form and save to database
    @PostMapping("/create")
    public String create(@ModelAttribute ProductCreateDTO productCreateDTO) {
        productService.create(productCreateDTO);
        return "redirect:/products";
    }

    // Render Edit Product form
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable UUID id, Model model) {
        var productDTO = productService.findById(id);
        model.addAttribute("productDTO", productDTO);

        // Get all categories
        var categories = categoryService.findAll();
        model.addAttribute("categories", categories);
        return "products/edit";
    }

    // Retrieve Product data from form and save to database
    @PostMapping("/edit/{id}")
    public String edit(@PathVariable UUID id, @ModelAttribute ProductDTO productDTO) {
        productService.update(id, productDTO);
        return "redirect:/products";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable UUID id) {
        productService.delete(id);
        return "redirect:/products";
    }
}
