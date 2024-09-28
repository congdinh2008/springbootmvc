package com.congdinh.springbootmvc.services;

import java.util.List;

import org.hibernate.validator.constraints.UUID;
import org.springframework.stereotype.Service;

import com.congdinh.springbootmvc.dtos.product.ProductDTO;
import com.congdinh.springbootmvc.entities.Product;
import com.congdinh.springbootmvc.repositories.ProductRepository;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<ProductDTO> findAll() {
        var categories = productRepository.findAll();

        var productDTOs = categories.stream().map(product -> {
            var productDTO = new ProductDTO();
            productDTO.setId(product.getId());
            productDTO.setName(product.getName());
            productDTO.setDescription(product.getDescription());
            return productDTO;
        }).toList();

        return productDTOs;
    }

    @Override
    public ProductDTO findById(UUID id) {
        var product = productRepository.findById(id).orElse(null);

        if (product == null) {
            return null;
        }

        var productDTO = new ProductDTO();
        productDTO.setId(product.getId());
        productDTO.setName(product.getName());
        productDTO.setDescription(product.getDescription());

        return productDTO;
    }

    @Override
    public ProductDTO create(ProductDTO productDTO) {
        // Kiem tra productDTO null
        if (productDTO == null) {
            throw new IllegalArgumentException("ProductDTO is required");
        }

        // Convert ProductDTO to Product
        var product = new Product();
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());

        // Save product
        product = productRepository.save(product);

        // Convert Product to ProductDTO
        var newProductDTO = new ProductDTO();
        newProductDTO.setId(product.getId());
        newProductDTO.setName(product.getName());
        newProductDTO.setDescription(product.getDescription());

        return newProductDTO;
    }

    @Override
    public ProductDTO update(UUID id, ProductDTO productDTO) {
        if (productDTO == null) {
            throw new IllegalArgumentException("ProductDTO is required");
        }

        // Find product by id - Managed
        var product = productRepository.findById(id).orElse(null);

        if (product == null) {
            throw new IllegalArgumentException("Product not found");
        }

        // Update product
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());

        // Save product => update
        product = productRepository.save(product);

        // Convert Product to ProductDTO
        var updatedProductDTO = new ProductDTO();
        updatedProductDTO.setId(product.getId());
        updatedProductDTO.setName(product.getName());
        updatedProductDTO.setDescription(product.getDescription());

        return updatedProductDTO;
    }

    @Override
    public void delete(UUID id) {
        // Find product by id - Managed
        var product = productRepository.findById(id).orElse(null);

        if (product == null) {
            throw new IllegalArgumentException("Product not found");
        }

        // Delete product
        productRepository.delete(product);
    }
}
