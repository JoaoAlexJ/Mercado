package services;

import model.Product;
import model.ProductRequestDTO;
import repository.ProductRepositoryInMemory;

import java.util.Objects;
import java.util.UUID;

public class ProductService {

    private ProductRepositoryInMemory productRepositoryInMemory;

    public ProductService(ProductRepositoryInMemory productRepositoryInMemory) {
        this.productRepositoryInMemory = Objects.requireNonNull(productRepositoryInMemory);
    }

    //-----------------//

    public ProductRepositoryInMemory getProductRepositoryInMemory() {
        return productRepositoryInMemory;
    }

    public Product registerProduct(ProductRequestDTO data){

        UUID id = UUID.randomUUID();

        Product newProduct = new Product(id, data.name(), data.category(), data.brand(), data.barCode(),
                data.price(), data.stock());

        productRepositoryInMemory.save(newProduct);
        return newProduct;

    }
}
