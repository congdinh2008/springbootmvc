package com.congdinh.springbootmvc.services;

import java.util.List;
import org.hibernate.validator.constraints.UUID;

import com.congdinh.springbootmvc.dtos.product.ProductDTO;

public interface ProductService {
    List<ProductDTO> findAll();

    ProductDTO findById(UUID id);

    ProductDTO create(ProductDTO productDTO);

    ProductDTO update(UUID id, ProductDTO productDTO);

    void delete(UUID id);
}
