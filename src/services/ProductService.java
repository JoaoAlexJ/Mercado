package services;

import model.Product.Category;
import security.BarcodeValidator;
import model.Product.Product;
import model.dto.ProductRequestDTO;
import repository.Interfaces.ProductRepository;

import java.util.*;
import java.util.function.Function;

public class ProductService {

    private final ProductRepository productRepository;
    private final Function<Product, String> toProductName = Product::getName;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = Objects.requireNonNull(productRepository);
    }

    public void reduceStock(UUID id, int quantity){

        findProductById(id).reduceStock(quantity);
    }

    public void increaseStrock(UUID id, int quantity){

        findProductById(id).increaseStock(quantity);
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

    //------------------------- Finds ----------------------------//


    public Product findProductByBarcode(String barcode){
        return productRepository.findByBarCode(barcode).orElseThrow(()-> new NoSuchElementException("Product not found"));
    }

    public Product findProductById(UUID id){
        return productRepository.findByID(id).orElseThrow(()-> new NoSuchElementException("Product not found"));
    }

    public Product findByname(String name){
        return findAllProducts().stream()
                .filter(p -> p.getBrand().equalsIgnoreCase(name))
                .findFirst().orElseThrow(() -> new NoSuchElementException("Product not found"));
    }

    //------------------------- Find lists ----------------------------//
    public List<Product> findByBrand(String brand){
        return findAllProducts().stream()
                .filter(p -> p.getBrand().equalsIgnoreCase(brand))
                .sorted(Comparator.comparing(toProductName))
                .toList();
    }

    public List<Product> findAllProducts(){

        if (productRepository.findAll().isEmpty()){
            System.out.println("No products registered in the catalog");
        }

        return productRepository.findAll().stream()
                .sorted(Comparator.comparing(toProductName))
                .toList();
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
                .sorted(Comparator.comparing(toProductName))
                .toList();
    }

    //------------------------- Find sorted by price ----------------------------//

    public List<Product> sortedByPrice(List<Product> list, SortOrder sortOrder){

        if (sortOrder == null){
            throw new IllegalArgumentException("SortOrder cannot be null.");
        }

        if (list == null){
            throw new IllegalArgumentException("List cannot be null");
        }

        if (list.isEmpty()){
            return List.of();
        }


        Comparator<Product> priceComparator = Comparator.comparing(Product::getPrice);

        if (sortOrder == SortOrder.DESC){
            priceComparator = priceComparator.reversed();
        }

        return list.stream().sorted(priceComparator).toList();
    }

    public List<Product> findAllProductsByPrice(SortOrder sortOrder){
        return sortedByPrice(findAllProducts(), sortOrder);
    }

    //------------------------- Delete ----------------------------//

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
