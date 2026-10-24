package repository.Interfaces;

import model.User;
import sale.Order;

import java.util.List;

public interface OrderRepository extends Repository<Order>{

    List<Order> findAllByCustomer(User custumer);


}

