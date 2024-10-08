package com.congdinh.springbootmvc.services;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.congdinh.springbootmvc.dtos.category.CategoryDTO;
import com.congdinh.springbootmvc.dtos.product.ProductCreateDTO;
import com.congdinh.springbootmvc.dtos.product.ProductDTO;
import com.congdinh.springbootmvc.entities.Category;
import com.congdinh.springbootmvc.entities.Product;
import com.congdinh.springbootmvc.repositories.ProductRepository;

import jakarta.persistence.criteria.Predicate;

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
            productDTO.setPrice(product.getPrice());
            productDTO.setStock(product.getStock());
            productDTO.setImage(product.getImage());

            if (product.getCategory() != null) {
                productDTO.setCategoryId(product.getCategory().getId());

                // Convert category to categoryDTO
                var categoryDTO = new CategoryDTO();
                categoryDTO.setId(product.getCategory().getId());
                categoryDTO.setName(product.getCategory().getName());
                categoryDTO.setDescription(product.getCategory().getDescription());

                // Set categoryDTO to productDTO
                productDTO.setCategory(categoryDTO);
            }

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
        productDTO.setPrice(product.getPrice());
        productDTO.setStock(product.getStock());
        productDTO.setImage(product.getImage());

        if (product.getCategory() != null) {
            productDTO.setCategoryId(product.getCategory().getId());

            // Convert category to categoryDTO
            var categoryDTO = new CategoryDTO();
            categoryDTO.setId(product.getCategory().getId());
            categoryDTO.setName(product.getCategory().getName());
            categoryDTO.setDescription(product.getCategory().getDescription());

            // Set categoryDTO to productDTO
            productDTO.setCategory(categoryDTO);
        }

        return productDTO;
    }

    @Override
    public ProductDTO create(ProductCreateDTO productCreateDTO) {
        // Kiem tra productDTO null
        if (productCreateDTO == null) {
            throw new IllegalArgumentException("ProductDTO is required");
        }

        // Convert ProductDTO to Product
        var product = new Product();
        product.setName(productCreateDTO.getName());
        product.setDescription(productCreateDTO.getDescription());
        product.setPrice(productCreateDTO.getPrice());
        product.setStock(productCreateDTO.getStock());
        product.setImage(productCreateDTO.getImage());

        // Kiem tra xem category co duoc select hay khong
        // Neu co thi product co category va can set category cho product do
        if (productCreateDTO.getCategoryId() != null) {
            var category = new Category();
            category.setId(productCreateDTO.getCategoryId());
            product.setCategory(category);
        }

        // Save product
        product = productRepository.save(product);

        // Convert Product to ProductDTO
        var newProductDTO = new ProductDTO();
        newProductDTO.setId(product.getId());
        newProductDTO.setName(product.getName());
        newProductDTO.setDescription(product.getDescription());
        newProductDTO.setPrice(product.getPrice());
        newProductDTO.setStock(product.getStock());
        newProductDTO.setImage(product.getImage());

        // Neu product co category thi set category id cho productDTO
        if (product.getCategory() != null) {
            newProductDTO.setCategoryId(product.getCategory().getId());
        }

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
        product.setPrice(productDTO.getPrice());
        product.setStock(productDTO.getStock());
        product.setImage(productDTO.getImage());

        // Kiem tra xem category co duoc select hay khong
        // Neu co thi product co category va can set category cho product do de update
        if (productDTO.getCategoryId() != null) {
            var category = new Category();
            category.setId(productDTO.getCategoryId());
            product.setCategory(category);
        }

        // Save product => update
        product = productRepository.save(product);

        // Convert Product to ProductDTO
        var updatedProductDTO = new ProductDTO();
        updatedProductDTO.setId(product.getId());
        updatedProductDTO.setName(product.getName());
        updatedProductDTO.setDescription(product.getDescription());
        updatedProductDTO.setPrice(product.getPrice());
        updatedProductDTO.setStock(product.getStock());
        updatedProductDTO.setImage(product.getImage());

        // Neu product co category thi set category id cho productDTO
        if (product.getCategory() != null) {
            updatedProductDTO.setCategoryId(product.getCategory().getId());
        }

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

    @Override
    public List<ProductDTO> search(String keyword) {
        if (keyword == null) {
            return findAll();
        }

        var categories = productRepository.findByNameContainingIgnoreCase(keyword);

        var productDTOs = categories.stream().map(product -> {
            var productDTO = new ProductDTO();
            productDTO.setId(product.getId());
            productDTO.setName(product.getName());
            productDTO.setDescription(product.getDescription());
            productDTO.setPrice(product.getPrice());
            productDTO.setStock(product.getStock());
            productDTO.setImage(product.getImage());

            if (product.getCategory() != null) {
                productDTO.setCategoryId(product.getCategory().getId());

                // Convert category to categoryDTO
                var categoryDTO = new CategoryDTO();
                categoryDTO.setId(product.getCategory().getId());
                categoryDTO.setName(product.getCategory().getName());
                categoryDTO.setDescription(product.getCategory().getDescription());

                // Set categoryDTO to productDTO
                productDTO.setCategory(categoryDTO);
            }

            return productDTO;
        }).toList();

        return productDTOs;
    }

    @Override
    public Page<ProductDTO> search(String keyword, Pageable pageable) {
        Specification<Product> specification = (root, query, criteriaBuilder) -> {
            if (keyword == null) {
                return null;
            }

            // WHERE name LIKE %keyword% OR description LIKE %keyword%
            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + keyword.toLowerCase() + "%"),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("description")),
                            "%" + keyword.toLowerCase() + "%"));
        };

        Page<Product> products = productRepository.findAll(specification, pageable);

        Page<ProductDTO> productDTOs = products.map(product -> {
            var productDTO = new ProductDTO();
            productDTO.setId(product.getId());
            productDTO.setName(product.getName());
            productDTO.setDescription(product.getDescription());
            productDTO.setPrice(product.getPrice());
            productDTO.setStock(product.getStock());
            productDTO.setImage(product.getImage());

            if (product.getCategory() != null) {
                productDTO.setCategoryId(product.getCategory().getId());

                // Convert category to categoryDTO
                var categoryDTO = new CategoryDTO();
                categoryDTO.setId(product.getCategory().getId());
                categoryDTO.setName(product.getCategory().getName());
                categoryDTO.setDescription(product.getCategory().getDescription());

                // Set categoryDTO to productDTO
                productDTO.setCategory(categoryDTO);
            }

            return productDTO;
        });

        return productDTOs;
    }

    @Override
    public Page<ProductDTO> search(String keyword, String categoryName, Pageable pageable) {
        Specification<Product> specification = (root, query, criteriaBuilder) -> {
            if (keyword == null) {
                return null;
            }

            // WHERE name LIKE %keyword%
            Predicate namePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("name")),
                    "%" + keyword.toLowerCase() + "%");

            // WHERE description LIKE %keyword%
            Predicate descriptionPredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("description")),
                    "%" + keyword.toLowerCase() + "%");

            // WHERE name LIKE %keyword% OR description LIKE %keyword%
            Predicate keywordPredicate = criteriaBuilder.or(namePredicate, descriptionPredicate);

            if(categoryName == null || categoryName.isBlank()) {
                return keywordPredicate;
            }

            // WHERE description LIKE %keyword%
            Predicate categoryNamePredicate = criteriaBuilder.equal(
                    criteriaBuilder.lower(root.get("category").get("name")),
                    categoryName.toLowerCase());

            // WHERE (name LIKE %keyword% OR description LIKE %keyword%) AND category.name = categoryName
            return criteriaBuilder.and(keywordPredicate, categoryNamePredicate);
        };

        Page<Product> products = productRepository.findAll(specification, pageable);

        Page<ProductDTO> productDTOs = products.map(product -> {
            var productDTO = new ProductDTO();
            productDTO.setId(product.getId());
            productDTO.setName(product.getName());
            productDTO.setDescription(product.getDescription());
            productDTO.setPrice(product.getPrice());
            productDTO.setStock(product.getStock());
            productDTO.setImage(product.getImage());

            if (product.getCategory() != null) {
                productDTO.setCategoryId(product.getCategory().getId());

                // Convert category to categoryDTO
                var categoryDTO = new CategoryDTO();
                categoryDTO.setId(product.getCategory().getId());
                categoryDTO.setName(product.getCategory().getName());
                categoryDTO.setDescription(product.getCategory().getDescription());

                // Set categoryDTO to productDTO
                productDTO.setCategory(categoryDTO);
            }

            return productDTO;
        });

        return productDTOs;
    }
}
