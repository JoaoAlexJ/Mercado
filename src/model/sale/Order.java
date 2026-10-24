package model.sale;

import Exceptions.SaleException;
import model.User.User;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Order {

    private UUID id;
    private User custumer;
    private LocalDate date;
    private BigDecimal value;
    private OrderStatus status;
    private List<OrderIten> itens;

    public Order(UUID id, User customer, List<OrderIten> itens) {

        if (itens == null || itens.isEmpty()){
            throw new SaleException("The cart contains no product");
        }

        this.id = id;
        this.custumer = customer;
        this.date = LocalDate.now();
        this.itens = new ArrayList<>(itens);
        this.status = OrderStatus.OPEN;
        this.value = itens.stream()
                .map(OrderIten::getSubtotal)
                .reduce( BigDecimal.ZERO, BigDecimal::add);
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

    public List<OrderIten> getItens() {
        return new ArrayList<>(itens);
    }
}
