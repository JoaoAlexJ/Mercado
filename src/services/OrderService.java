package services;

import repository.Interfaces.OrderRepository;

public class OrderService {

    private final OrderRepository orderRepository;
    private final WalletService walletService;

    public OrderService(OrderRepository orderRepository, WalletService walletService) {
        this.orderRepository = orderRepository;
        this.walletService = walletService;
    }


}
