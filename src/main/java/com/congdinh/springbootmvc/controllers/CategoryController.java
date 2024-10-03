package com.congdinh.springbootmvc.controllers;

import java.util.UUID;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.congdinh.springbootmvc.dtos.category.CategoryCreateDTO;
import com.congdinh.springbootmvc.dtos.category.CategoryDTO;
import com.congdinh.springbootmvc.services.CategoryService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public String index(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false, defaultValue = "name") String sortBy, // Xac dinh truong sap xep
            @RequestParam(required = false, defaultValue = "asc") String order, // Xac dinh chieu sap xep
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "5") Integer size,
            Model model) {
        // Check sort order
        Pageable pageable = null;

        if (order.equals("asc")) {
            pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());
        } else {
            pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());
        }

        // Search category by keyword and paging
        var categories = categoryService.findAll(keyword, pageable);
        model.addAttribute("categories", categories);

        // Passing keyword to view
        model.addAttribute("keyword", keyword);
        // Passing total pages to view
        model.addAttribute("totalPages", categories.getTotalPages());
        // Passing total elements to view
        model.addAttribute("totalElements", categories.getTotalElements());

        // Passing current sortBy to view
        model.addAttribute("sortBy", sortBy);

        // Passing current order to view
        model.addAttribute("order", order);

        model.addAttribute("page", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("pageSizes", new Integer[] { 2, 5, 10, 20, 50, 100 });

        return "categories/index";
    }

    // Render Create Category form
    @GetMapping("/create")
    public String create(Model model) {
        var categoryCreateDTO = new CategoryCreateDTO();
        model.addAttribute("categoryCreateDTO", categoryCreateDTO);
        return "categories/create";
    }

    // Retrieve Category data from form and save to database
    @PostMapping("/create")
    public String create(@ModelAttribute @Valid CategoryCreateDTO categoryCreateDTO,
            BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "categories/create";
        }

        categoryService.create(categoryCreateDTO);
        return "redirect:/categories";
    }

    // Render Edit Category form
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable UUID id, Model model) {
        var categoryDTO = categoryService.findById(id);
        model.addAttribute("categoryDTO", categoryDTO);
        return "categories/edit";
    }

    // Retrieve Category data from form and save to database
    @PostMapping("/edit/{id}")
    public String edit(@PathVariable UUID id, @ModelAttribute CategoryDTO categoryDTO) {
        categoryService.update(id, categoryDTO);
        return "redirect:/categories";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable UUID id) {
        categoryService.delete(id);
        return "redirect:/categories";
    }
}
