package sale;

import Exceptions.SaleException;
import model.OrderIten;
import model.User;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Order {

    private UUID id;
    private User custumer;
    private LocalDate date;
    private BigDecimal value;
    private OrderStatus status;
    private List<OrderIten> cart;

    public Order(UUID id, User customer, List<OrderIten> cart) {
        this.id = id;
        this.custumer = customer;
        this.date = LocalDate.now();
        this.cart = cart != null ? new ArrayList<>(cart) : new ArrayList<>();
        this.status = OrderStatus.OPEN;

        calculateTotalValue();
    }

    public void calculateTotalValue(){

        this.value = cart.stream()
                .map(OrderIten::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void addProduct(OrderIten p){
        if (status != OrderStatus.OPEN){
            throw new SaleException("Cannot modify an order that is not OPEN");
        }

        if (p == null){
            throw new IllegalArgumentException("Product cannot be null.");
        }

        cart.add(p);
        calculateTotalValue();
    }

    public void removeProductByBarcode(String barcode){

        if (status != OrderStatus.OPEN){
            throw new SaleException("Cannot modify an order that is not OPEN");
        }

        cart.removeIf(o -> o.getBarcode().equals(barcode));
        calculateTotalValue();
    }

    //--------------------//

    public void cancelOrder(){
        if (status != OrderStatus.OPEN){
            throw new SaleException("Only open orders can be cancelled");
        }

        this.status = OrderStatus.CANCELED;
    }

    public void payOrder(){
        if (status != OrderStatus.OPEN){
            throw new SaleException("Only open orders can be paid");
        }

        this.status = OrderStatus.PAID;
    }

    //--------------------------//

    public OrderStatus getStatus() {
        return status;
    }

    public UUID getId() {
        return id;
    }

    public User getCustumer() {
        return custumer;
    }

    public LocalDate getDate() {
        return date;
    }

    public BigDecimal getValue() {
        return value;
    }

    public List<OrderIten> getCart() {
        return new ArrayList<>(cart);
    }
}
