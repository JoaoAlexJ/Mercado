package repository;

import model.User.User;
import repository.Interfaces.OrderRepository;
import model.sale.Order;

import java.util.*;

public class OrderRepositoryInMemory implements OrderRepository {

    private final Map<UUID, Order> map = new HashMap<>();

    @Override
    public void save(Order obj) {
        if (obj == null){
            throw new IllegalArgumentException("Order cannot be null.");
        }

        map.put(obj.getId(), obj);
    }

    @Override
    public void delete(UUID id) {

        if (findByID(id).isEmpty()){
            throw new NoSuchElementException("Order no found");
        }
        map.remove(id);
    }

    @Override
    public Optional<Order> findByID(UUID id) {
        return map.values().stream()
                .filter(o -> o.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<Order> findAll() {
        return map.values().stream().toList();
    }

    @Override
    public List<Order> findAllByCustomer(UUID id) {

        return map.values().stream()
                .filter(o -> o.getCustumer().getId().equals(id))
                .toList();
    }
}
