package model.dto;

import model.Category;

import java.math.BigDecimal;

public record ProductRequestDTO(String name, Category category, String brand,
                                String barCode, BigDecimal price, int stock) {


    public ProductRequestDTO {

        if (name == null || name.isBlank()){
            throw new IllegalArgumentException("Product name cannot be null or empty");
        }

        if (category == null){
            throw new IllegalArgumentException("Category cannot be null.");
        }

        if (brand == null || brand.isBlank()){
            throw new IllegalArgumentException("Brand cannot be null or empty");
        }

        if (barCode == null || barCode.isBlank()){
            throw new IllegalArgumentException("Barcode cannot be null or empty");
        }

        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0){
            throw new IllegalArgumentException("Price must be greater than zero");
        }

        if (stock <= 0){
            throw new IllegalArgumentException("Stock must be greater than zero");
        }


        name = name.trim();
        brand = brand.trim();
        barCode = barCode.trim();

    }
}
