package repository.Interfaces;

import model.Product;

import java.util.Optional;

public interface ProductRepository extends Repository<Product> {

    Optional<Product> findByBarCode(String barcode);
    void deleteByBarCode(String barcode);
}
