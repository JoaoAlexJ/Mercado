package services;

import security.BarcodeValidator;
import model.Product;
import model.ProductRequestDTO;
import repository.ProductRepository;

import java.util.*;

public class ProductService {

    private ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = Objects.requireNonNull(productRepository);
    }

    //-----------------//

    public Product registerProduct(ProductRequestDTO data){

        if (!BarcodeValidator.isValid(data.barCode())){
            throw new IllegalArgumentException("Invalid barcode");
        }

        if (productRepository.findByBarCode(data.barCode()).isPresent()){
            throw new IllegalArgumentException("Product already registered with barcode: "+data.barCode()+" Name of product: "+data.name());
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
    //---------------//

    public Product findProductByBarcode(String barcode){
        return productRepository.findByBarCode(barcode).orElseThrow(()-> new NoSuchElementException("Product not found"));
    }

    public Product findProductById(UUID id){
        return productRepository.findByID(id).orElseThrow(()-> new NoSuchElementException("Product not found"));
    }

    public List<Product> findAllProducts(){
        return productRepository.findAll();
    }

    public Product deleteByBarcode(String barcode){

        Product p = findProductByBarcode(barcode);

        if (p == null){
            throw new NoSuchElementException("Product not found");
        }
        productRepository.deleteByBarCode(barcode);
        return p;
    }

    public Product deleteById(UUID id){

        Product p = findProductById(id);

        if (p == null){
            throw new NoSuchElementException("Product not found");
        }
        productRepository.delete(id);
        return p;

    }

}
