package model.sale;

import Exceptions.SaleException;
import model.Product.Product;
import model.User.User;
import security.UserSession;
import services.ProductService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Cart {

    private User customer;
    private BigDecimal value;
    private List<OrderIten> itens;


    public Cart() {

        this.customer = UserSession.getCurrentUser();
        this.itens = new ArrayList<>();
        calculateTotalValue();

    }

    public void calculateTotalValue(){

        this.value = itens.stream()
                .map(OrderIten::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }


    public void addProduct(Product p, int quantity){
        if (p == null){
            throw new IllegalArgumentException("Product cannot be null");
        }

        if (quantity <= 0){
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }

        Optional<OrderIten> orderIten = itens.stream()
                .filter(o -> o.getIdProduct().equals(p.getId()))
                .findFirst();


        int currentQuantity = orderIten.map(OrderIten::getQuantity).orElse(0);
        int totalRequested = currentQuantity + quantity;

        if (totalRequested > p.getStock()){
            throw new SaleException("Insufficient stock. Available: "+p.getStock() +
                    " already in cart: "+currentQuantity);
        }

        if (orderIten.isPresent()){
            orderIten.get().increaseQuantity(quantity);

        }else {
            itens.add(new OrderIten(p, quantity));
        }

        calculateTotalValue();
    }

    public void removeProductByBarcode(String barcode, int quantity) {

        if (quantity <= 0 ){
            throw new IllegalArgumentException("Invalid quantity");
        }

        OrderIten orderIten = itens.stream()
                .filter(o -> o.getBarcode().equals(barcode))
                .findFirst()
                .orElseThrow(() -> new SaleException("OrdenIten not found"));


        orderIten.decreaseQuantity(quantity);
        itens.removeIf(o -> o.getQuantity() == 0);
        calculateTotalValue();

    }

    public void clearCart(){
        this.itens.clear();
        this.value = BigDecimal.ZERO;
    }

    public void setCustomer(User customer) {
       if (customer == null){
           throw new IllegalArgumentException("Invalid Customer.");
       }

        if(customer != UserSession.getCurrentUser()){
            throw new SaleException("The cart needs to log in using the user from the session");
        }

        this.customer = customer;
    }

    public User getCustomer() {
        return customer;
    }

    public List<OrderIten> getItens() {
        return new ArrayList<>(itens);
    }

    public BigDecimal getValue() {
        return value;
    }
}
