package com.congdinh.springbootmvc.controllers;

import java.util.UUID;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.congdinh.springbootmvc.dtos.product.ProductDTO;
import com.congdinh.springbootmvc.services.ProductService;

@Controller
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public String index(Model model) {
        var products = productService.findAll();
        model.addAttribute("products", products);
        return "products/index";
    }

    // Render Create Product form
    @GetMapping("/create")
    public String create() {
        return "products/create";
    }

    // Retrieve Product data from form and save to database
    @PostMapping("/create")
    public String create(@ModelAttribute ProductDTO productDTO) {
        productService.create(productDTO);
        return "redirect:/products";
    }

    // Render Edit Product form
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable UUID id, Model model) {
        var productDTO = productService.findById(id);
        model.addAttribute("productDTO", productDTO);
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
