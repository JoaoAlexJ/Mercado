package model;

import java.math.BigDecimal;
import java.util.UUID;

public class Product {

    private UUID id;
    private String name;
    private Category category;
    private String brand;
    private String barCode;

    private BigDecimal price;
    private int stock;

    public Product(UUID id, String name, Category category, String brand, String barCode, BigDecimal price, int stock) {

        this.id = id;
        this.name = name;
        this.category = category;
        this.brand = brand;
        this.barCode = barCode;
        this.price = price;
        this.stock = stock;
    }


    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Category getCategory() {
        return category;
    }

    public String getBrand() {
        return brand;
    }

    public String getBarCode() {
        return barCode;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }
}
