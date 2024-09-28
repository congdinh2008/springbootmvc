package com.congdinh.springbootmvc.services;

import java.util.List;
import java.util.UUID;

import com.congdinh.springbootmvc.dtos.category.CategoryCreateDTO;
import com.congdinh.springbootmvc.dtos.category.CategoryDTO;

public interface CategoryService {
    List<CategoryDTO> findAll();

    CategoryDTO findById(UUID id);

    CategoryDTO create(CategoryCreateDTO categoryCreateDTO);

    CategoryDTO update(UUID id, CategoryDTO categoryDTO);

    void delete(UUID id);
}
