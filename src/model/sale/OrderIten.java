package model.sale;

import model.Product.Product;

import java.math.BigDecimal;
import java.util.UUID;

public class OrderIten {

    private final UUID id;
    private final UUID idProduct;
    private final String nameProduct;
    private final BigDecimal unitPrice;
    private final String barcode;
    private final String brand;
    private int quantity;

    public OrderIten(Product product, int quantity) {
        this.id = UUID.randomUUID();
        this.idProduct = product.getId();
        this.nameProduct = product.getName();
        this.unitPrice = product.getPrice();
        this.barcode = product.getBarCode();
        this.brand = product.getBrand();
        this.quantity = quantity;
    }

    //------------------------------------//

    public BigDecimal getSubtotal(){
        return this.unitPrice.multiply(BigDecimal.valueOf(quantity));
    }


    public void decreaseQuantity(int quantityRemove){
        if (quantityRemove <= 0 || quantityRemove > this.quantity){
            throw new IllegalArgumentException("Invalid quantity");
        }

        this.quantity -= quantityRemove;
    }

    public void increaseQuantity(int quantity){
        if (quantity <= 0){
            throw new IllegalArgumentException("Invalid quantity");
        }

        this.quantity += quantity;
    }


    //------------------------------------//

    public String getBarcode() {
        return barcode;
    }

    public UUID getId() {
        return id;
    }

    public UUID getIdProduct() {
        return idProduct;
    }

    public String getNameProduct() {
        return nameProduct;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getBrand() {
        return brand;
    }
}
