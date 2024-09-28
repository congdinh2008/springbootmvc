package com.congdinh.springbootmvc.services;

import java.util.List;
import java.util.UUID;

import com.congdinh.springbootmvc.dtos.product.ProductCreateDTO;
import com.congdinh.springbootmvc.dtos.product.ProductDTO;

public interface ProductService {
    List<ProductDTO> findAll();

    ProductDTO findById(UUID id);

    ProductDTO create(ProductCreateDTO productCreateDTO);

    ProductDTO update(UUID id, ProductDTO productDTO);

    void delete(UUID id);
}
