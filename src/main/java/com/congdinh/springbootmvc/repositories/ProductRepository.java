package com.congdinh.springbootmvc.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.congdinh.springbootmvc.entities.Product;

@Repository
@Transactional
public interface ProductRepository extends JpaRepository<Product, UUID>, JpaSpecificationExecutor<Product> {

    List<Product> findByNameContainingIgnoreCase(String name);

    List<Product> findByName(String name);

    List<Product> findByPriceGreaterThan(double price);

    Product getFirstByOrderByPriceDesc();

    // Get top product by price order by desc
    List<Product> findTop10ByOrderByPriceDesc();

    double getStockById(UUID id);

    List<Product> findByNameAndPriceGreaterThan(String name, double price);

    List<Product> findByCategoryIsNull();

    // SELECT * FROM products JOIN categories ON products.category_id = categories.id WHERE categories.name = ?
    List<Product> findByCategoryName(String name);
}
