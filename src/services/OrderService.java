package services;

import Exceptions.InsufficientStockException;
import Exceptions.SaleException;
import model.Product.Product;
import model.User.User;
import model.sale.Cart;
import model.sale.Order;
import model.sale.OrderIten;
import repository.Interfaces.OrderRepository;
import security.PasswordHasher;
import security.UserSession;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

public class OrderService {

    private final OrderRepository orderRepository;
    private final WalletService walletService;
    private final ProductService productService;

    public OrderService(OrderRepository orderRepository, WalletService walletService, ProductService productService) {
        this.orderRepository = orderRepository;
        this.walletService = walletService;
        this.productService = productService;
    }


    public Order completeOrder(String password, Cart cart){

        if (cart == null){
            throw new IllegalArgumentException("Cart cannot be null.");
        }
        if (cart.getItens().isEmpty()){
            throw new SaleException("The cart contains no products");
        }

        User customer = UserSession.getCurrentUser();

        if (customer == null){
            throw new IllegalArgumentException("No authenticated customer associated with this cart.");
        }

        if (!PasswordHasher.checkPassword(password, customer.getPassword())){
            throw new SecurityException("Invalid password, try again.");
        }

        List<String> errors = new ArrayList<>();

        for (OrderIten item : cart.getItens()){

            Product p = productService.findProductById(item.getIdProduct());

            if (p.getStock() < item.getQuantity()){
                errors.add(String.format("%s (Available: %d, in cart: %d)", p.getName(), p.getStock(), item.getQuantity()));
            }
        }

        if (!errors.isEmpty()){
            throw new InsufficientStockException(errors);
        }

        Order order = new Order(UUID.randomUUID(), customer, cart.getItens());

        walletService.withdraw(customer.getId(), order.getValue());
        order.getItens().forEach(o -> productService.reduceStock(o.getIdProduct(), o.getQuantity()));

        order.payOrder();
        cart.clearCart();
        orderRepository.save(order);
        return order;
    }

    public void cancelOrder(UUID id){

        Order order = findById(id);
        order.cancelOrder();

        walletService.deposit(order.getCustumer().getId(), order.getValue());
        order.getItens().forEach(o -> productService.increaseStrock(o.getIdProduct(), o.getQuantity()));

        orderRepository.save(order);



    }

    //---------------------------------//
    public Order findById(UUID id){

        return orderRepository.findByID(id).orElseThrow(() -> new NoSuchElementException("Order no found"));
    }


    public void deleteById(UUID id){
        orderRepository.delete(id);
    }

    public List<Order> findAll(){
        return orderRepository.findAll();
    }

    public List<Order> findByCustomer(UUID id){
        return orderRepository.findAllByCustomer(id);
    }


}
