package com.congdinh.springbootmvc.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.congdinh.springbootmvc.entities.Category;

// Register this interface as a Spring Data JPA repository as a bean
@Repository
public interface CategoryRepository extends JpaRepository<Category, UUID> {
    Category findByName(String name);
}
