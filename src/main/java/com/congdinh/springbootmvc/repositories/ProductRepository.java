package com.congdinh.springbootmvc.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.congdinh.springbootmvc.entities.Product;

@Repository
@Transactional
public interface ProductRepository extends JpaRepository<Product, UUID> {
}
