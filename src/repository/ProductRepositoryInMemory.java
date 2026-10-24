package repository;

import model.Product.Product;
import repository.Interfaces.ProductRepository;

import java.util.*;

public class ProductRepositoryInMemory implements ProductRepository {

    private final Map<UUID, Product> productMap = new HashMap<>();

    @Override
    public Optional<Product> findByBarCode(String barcode) {

        if (barcode == null || barcode.isBlank()){
            return Optional.empty();
        }

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
            throw new NullPointerException("Invalid product.");
        }

        productMap.put(obj.getId(), obj);
    }

    @Override
    public void delete(UUID id) {

        Product productRemoved = productMap.remove(id);

        if (productRemoved == null){
            throw new NoSuchElementException("Product not found with this ID: "+id);
        }
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
