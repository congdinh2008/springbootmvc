package com.congdinh.springbootmvc.controllers;

import java.util.UUID;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.congdinh.springbootmvc.dtos.product.ProductCreateDTO;
import com.congdinh.springbootmvc.dtos.product.ProductDTO;
import com.congdinh.springbootmvc.services.CategoryService;
import com.congdinh.springbootmvc.services.ProductService;

import jakarta.validation.Valid;

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
            @RequestParam(name = "categoryName", required = false) String categoryName,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "5") int size,
            Model model) {
        var pageable = PageRequest.of(page, size);
        var products = productService.search(keyword, categoryName, pageable);
        model.addAttribute("products", products);

        // Current keyword
        model.addAttribute("keyword", keyword);

        // Current page
        model.addAttribute("page", page);

        // Current pageSize
        model.addAttribute("pageSize", size);

        // total pages
        model.addAttribute("totalPages", products.getTotalPages());

        // Total elements
        model.addAttribute("totalElements", products.getTotalElements());

        // Limit page
        model.addAttribute("pageLimit", 2);

        // List of page sizes
        model.addAttribute("pageSizes", new Integer[] { 2, 5, 10, 20, 50, 100 });

        // Get all categories
        var categories = categoryService.findAll();
        model.addAttribute("categories", categories);

        model.addAttribute("categoryName", categoryName);
        return "products/index";
    }

    // Render Create Product form
    @GetMapping("/create")
    public String create(Model model) {
        var product = new ProductCreateDTO();
        model.addAttribute("productCreateDTO", product);

        var categories = categoryService.findAll();
        model.addAttribute("categories", categories);
        return "products/create";
    }

    // Retrieve Product data from form and save to database
    @PostMapping("/create")
    public String create(
            @ModelAttribute @Valid ProductCreateDTO productCreateDTO,
            @RequestParam(name = "imageFile", required = false) MultipartFile imageFile,
            BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            if (productCreateDTO.getCategoryId() == null) {
                bindingResult.rejectValue("categoryId", "category", "Category is required");
            }
            var categories = categoryService.findAll();
            model.addAttribute("categories", categories);
            return "products/create";
        }

        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                byte[] bytes = imageFile.getBytes();
                Path path = Paths.get("src/main/resources/static/images/products/" + imageFile.getOriginalFilename());
                Files.write(path, bytes);
                productCreateDTO.setImage("/images/products/" + imageFile.getOriginalFilename());
            } catch (Exception e) {
                e.printStackTrace();
                model.addAttribute("message", "Failed to upload image");
                var categories = categoryService.findAll();
                model.addAttribute("categories", categories);

                bindingResult.rejectValue("image", "image", "Failed to upload image");
                return "products/create";
            }
        }

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
    public String edit(@PathVariable UUID id, @ModelAttribute ProductDTO productDTO,
            @RequestParam(name = "imageFile", required = false) MultipartFile imageFile, Model model) {

        var oldProduct = productService.findById(id);

        // Case 1: User does not select a new image
        if (imageFile.getOriginalFilename().isEmpty()) {
            productDTO.setImage(oldProduct.getImage());
        } else {
            // Case 2: User selects a new image
            if (imageFile.getOriginalFilename() != null && !imageFile.getOriginalFilename().isEmpty()) {
                try {
                    byte[] bytes = imageFile.getBytes();
                    Path path = Paths
                            .get("src/main/resources/static/images/products/" + imageFile.getOriginalFilename());
                    Files.write(path, bytes);
                    productDTO.setImage("/images/products/" + imageFile.getOriginalFilename());
                } catch (Exception e) {
                    e.printStackTrace();
                    model.addAttribute("message", "Failed to upload image");
                    var categories = categoryService.findAll();
                    model.addAttribute("categories", categories);
                    return "products/create";
                }
            }
        }

        productService.update(id, productDTO);
        return "redirect:/products";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable UUID id) {
        productService.delete(id);
        return "redirect:/products";
    }
}
