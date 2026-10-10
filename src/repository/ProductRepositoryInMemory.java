package repository;

import model.Product;

import java.util.*;

public class ProductRepositoryInMemory implements ProductRepository {

    private final Map<UUID, Product> productMap = new HashMap<>();

    @Override
    public Optional<Product> findByBarCode(String barcode) {

        return productMap.values().stream()
                .filter(product -> product.getBarCode().equalsIgnoreCase(barcode))
                .findFirst();
    }

    @Override
    public void deleteByBarCode(String barcode) {

        productMap.values().removeIf(p -> p.getBarCode().equalsIgnoreCase(barcode));

    }

    @Override
    public void save(Product obj) {

        if (obj == null){
            throw new NullPointerException("Não é possível cadastrar um produto nulo.");
        }

        productMap.put(obj.getId(), obj);
    }

    @Override
    public void delete(UUID id) {

        productMap.remove(id);
    }

    @Override
    public Optional<Product> findByID(UUID id) {


        return Optional.ofNullable(productMap.get(id));
    }

    @Override
    public List<Product> findAll() {

        return new ArrayList<>(productMap.values());
    }
}
