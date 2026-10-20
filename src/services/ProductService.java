package services;

import model.Category;
import security.BarcodeValidator;
import model.Product;
import model.dto.ProductRequestDTO;
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

    public List<Product> findByBrand(String brand){
        return findAllProducts().stream()
                .filter(p -> p.getBrand().equalsIgnoreCase(brand))
                .toList();
    }

    public Product findByname(String name){
        return findAllProducts().stream()
                .filter(p -> p.getBrand().equalsIgnoreCase(name))
                .findFirst().orElseThrow(() -> new NoSuchElementException("Product not found"));
    }

    public List<Product> findAllProducts(){

        if (productRepository.findAll().isEmpty()){
            System.out.println("No products registered in the catalog");
        }

        return productRepository.findAll();
    }

    public List<Product> findByCategory(String category){

        if (category == null || category.isBlank()){
            return List.of();
        }

        final Category targetCategory;

        try {

            targetCategory = Category.valueOf(category.trim().toUpperCase());
        }catch (IllegalArgumentException e){
            return List.of();
        }

        return findAllProducts().stream()
                .filter(p -> p.getCategory() == targetCategory)
                .toList();
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
