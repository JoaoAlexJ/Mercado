package Flow;

import model.Category;
import model.Product;
import model.ProductRequestDTO;
import printer.Printer;
import security.BarcodeValidator;
import security.UserSession;
import services.ProductService;
import services.UserService;

import javax.print.attribute.standard.PrinterName;
import java.math.BigDecimal;
import java.util.Scanner;
import java.util.UUID;

public class AdmFlow {

    private UserService userService;
    private ProductService productService;
    private Scanner scanner;

    public AdmFlow(UserService userService, ProductService productService, Scanner scanner) {
        this.userService = userService;
        this.productService = productService;
        this.scanner = scanner;
    }

    //--------------------//


    public void start(){

        if (!UserSession.isAuthenticated()){
            throw new SecurityException("No users connected.");
        }

        if (!UserSession.isAdm()){
            throw new SecurityException("This flow is for administrators only");
        }


        Printer.printMainProductMenu();
        byte mainChoice = scanner.nextByte();
        Printer.lineBreak();
        Printer.line();

        scanner.nextLine();

        switch (mainChoice){

            case 1:

                boolean doIt = true;
                while (doIt){

                    Printer.printProductMenuAdm();
                    byte productChoice = scanner.nextByte();

                    scanner.nextLine();

                    Printer.lineBreak();
                    Printer.line();

                    switch (productChoice){

                        case 1:

                           registerProduct();
                           Printer.lineBreak();
                           Printer.line();

                           break;

                        case 2:

                            for (Product p : productService.findAllProducts()){

                                Printer.printProduct(p);
                            }

                            Printer.lineBreak();
                            break;

                        case 3:

                            System.out.print("Provide ID: ");
                            UUID id = UUID.fromString(scanner.nextLine());

                            Product p = productService.findProductById(id);

                            Printer.printProduct(p);
                            Printer.lineBreak();
                            Printer.line();

                            break;

                        case 4:

                            System.out.print("Provide barcode: ");
                            String barcodeInput = scanner.nextLine();

                            Product p1 = productService.findProductByBarcode(barcodeInput);
                            Printer.printProduct(p1);

                            Printer.lineBreak();
                            Printer.line();
                            break;

                        case 5:

                            System.out.print("Provide ID: ");
                            UUID idDel = UUID.fromString(scanner.nextLine());

                            Product pDel = productService.deleteById(idDel);

                            System.out.println("Product Deleted");
                            Printer.printProduct(pDel);

                            Printer.lineBreak();
                            Printer.line();
                            break;

                        case 6:

                            System.out.print("Provide barcode: ");
                            String barcodeDel = scanner.nextLine();

                            Product pDelBar = productService.deleteByBarcode(barcodeDel);

                            System.out.println("Product Deleted");
                            Printer.printProduct(pDelBar);

                            Printer.lineBreak();
                            Printer.line();

                            break;

                        case 7:
                            doIt = false;
                            break;

                        default:
                            System.err.println("Invalid choice");
                            break;

                    }

                }

                break;

            case 2:

                System.out.println("");



        }

    }

    private void registerProduct(){

        System.out.print("Product name: ");
        String name = scanner.nextLine();

        System.out.print("Categoria: ");
        String category = scanner.nextLine();

        Category categoryOfc = Category.valueOf(category.trim().toUpperCase());

        System.out.print("Brand: ");
        String brand = scanner.nextLine();

        System.out.print("Barcode: ");
        String barcode = scanner.nextLine();

        if (!BarcodeValidator.isValid(barcode)){
            throw new IllegalArgumentException("Invalid barcode");
        }

        System.out.print("Pice: ");
        BigDecimal price = scanner.nextBigDecimal();

        System.out.print("Estoque: ");
        int stok = scanner.nextInt();

        scanner.nextLine();

        ProductRequestDTO productRequestDTO = new ProductRequestDTO(
                name,categoryOfc, brand, barcode, price, stok
        );

        Product p = productService.registerProduct(productRequestDTO);

        Printer.lineBreak();
        Printer.line();

        System.out.println("Product registered!");
        Printer.printProduct(p);

    }
}
