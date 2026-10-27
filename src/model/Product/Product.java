package model.Product;

import java.math.BigDecimal;
import java.util.UUID;

public class Product {

    private final UUID id;
    private String name;
    private final Category category;
    private String brand;
    private final String barCode;

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

    public void reduceStock(int quantity){
        if (quantity <= 0 || quantity > this.stock){
            throw new IllegalArgumentException("quantity invalid");
        }

        this.stock -= quantity;
    }

    public void increaseStock(int quantity){
        if (quantity <= 0 ){
            throw new IllegalArgumentException("quantity invalid");
        }

        this.stock += quantity;
    }

    //---------------------//

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

    //---------------------//

    public void updateDetails(String name, BigDecimal price){

        if (name == null || name.isBlank()){
            throw new IllegalArgumentException("Invalid name.");
        }

        this.name = name;
        this.price = price;
    }

}
