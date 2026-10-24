package repository.Interfaces;

import model.User.User;
import model.sale.Order;

import java.util.List;
import java.util.UUID;

public interface OrderRepository extends Repository<Order>{

    List<Order> findAllByCustomer(UUID id);


}

