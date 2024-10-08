package com.congdinh.springbootmvc.dtos.product;

import java.util.UUID;
import org.hibernate.validator.constraints.Length;

import com.congdinh.springbootmvc.dtos.category.CategoryDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    private UUID id;

    @NotNull(message = "Name is required")
    @NotBlank(message = "Name is not empty")
    @Length(min = 3, max = 255, message = "Name must be between 3 and 255 characters")
    private String name;

    @Length(max = 500, message = "Description must be less than 500 characters")
    private String description;

    @NotNull(message = "Price is required")
    @PositiveOrZero(message = "Price must be greater than or equal to 0")
    private double price;

    @NotNull(message = "Stock is required")
    @PositiveOrZero(message = "Stock must be greater than or equal to 0")
    private int stock;

    @Length(max = 255, message = "Image must be less than 255 characters")
    private String image;

    // Selected category
    private UUID categoryId;

    private CategoryDTO category;
}
