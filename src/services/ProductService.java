package services;

import Auth.BarcodeValidator;
import model.Product;
import model.ProductRequestDTO;
import repository.ProductRepository;
import repository.ProductRepositoryInMemory;

import java.lang.classfile.Opcode;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class ProductService {

    private ProductRepository productRepository;

    public ProductService(ProductRepositoryInMemory productRepositoryInMemory) {
        this.productRepository = Objects.requireNonNull(productRepositoryInMemory);
    }

    //-----------------//

    public Product registerProduct(ProductRequestDTO data){

        if (!BarcodeValidator.isValid(data.barCode())){
            throw new IllegalArgumentException("Invalid barcode");
        }

        if (productRepository.findByBarCode(data.barCode()).isPresent()){
            throw new IllegalArgumentException("Product already registered with barcode: "+data.barCode());
        }


        UUID id = UUID.randomUUID();

        Product newProduct = new Product(
                id,
                data.name(),
                data.category(),
                data.brand(),
                data.barCode(),
                data.price(),
                data.stock());

        productRepository.save(newProduct);
        return newProduct;


    }
}
