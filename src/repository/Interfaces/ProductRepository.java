package repository.Interfaces;

import model.Product.Product;

import java.util.Optional;

public interface ProductRepository extends Repository<Product> {

    Optional<Product> findByBarCode(String barcode);
    void deleteByBarCode(String barcode);
}
