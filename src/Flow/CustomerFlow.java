package Flow;

import Exceptions.InsufficientStockException;
import Exceptions.SaleException;
import com.sun.tools.javac.Main;
import model.Product.Category;
import model.Product.Product;
import model.Ultility.FindOrdersByStatus;
import model.User.User;
import model.User.Wallet;
import model.sale.Cart;
import model.sale.Order;
import model.sale.OrderIten;
import printer.Printer;
import security.UserSession;
import services.OrderService;
import services.ProductService;
import services.UserService;
import services.WalletService;

import java.math.BigDecimal;
import java.util.*;

public class CustomerFlow {

    private final UserService userService;
    private final ProductService productService;
    private final WalletService walletService;
    private final Scanner scanner;
    private final OrderService orderService;

    private final Cart cart;

    public CustomerFlow(UserService userService, ProductService productService, Scanner scanner, WalletService walletService,
                        Cart cart, OrderService orderService) {

        this.userService = userService;
        this.productService = productService;
        this.scanner = scanner;
        this.walletService = walletService;
        this.cart = cart;
        this.orderService = orderService;
    }

    public Cart getCart() {
        return cart;
    }

    public void start(){

        if (!UserSession.isAuthenticated()){
            throw new SecurityException("No users connected.");
        }


        boolean runmain = true;
        while (runmain){

            Printer.printMainCustomerMenu();
            byte choiceMain = MainFlow.readByte(scanner);

            switch (choiceMain){

                case 1:

                    settingsFlow();
                    break;

                case 2:
                    productFlow();
                    break;

                case 3:

                    FindOrdersByStatus.byIdCustomer(UserSession.getCurrentUser().getId(), scanner, orderService);
                    break;

                case 4:
                    cartFlow();
                    break;

                case 0:

                    UserSession.logout();
                    runmain = false;
                    break;

                default:
                    System.out.println("Invalid choice");
                    break;

            }

        }

    }

    //--------------- Flows ---------------

    public void cartFlow(){

        boolean inCart = true;
        while (inCart) {

            if (cart.getItens().isEmpty()) {
                System.out.println("The cart is empty.");
                inCart = false;

            } else {

                Printer.printCart(cart);

                System.out.println("1 - Complete purchase");
                System.out.println("2 - remove itens");
                System.out.println("3 - Clean Cart");
                System.out.println("0 - back");
                System.out.print("-> ");
                byte choiceCart = MainFlow.readByte(scanner);

                switch (choiceCart){

                    case 1 -> {


                        System.out.print("Enter your password: ");
                        String password = scanner.nextLine();

                        try {
                            Order order = orderService.completeOrder(password, cart);
                            System.out.println("Purchase completed successfully!");

                            Printer.lineBreak();
                            Printer.line();
                            Printer.printOrder(order);
                            Printer.lineBreak();

                            inCart = false;
                        }catch (SecurityException | IllegalArgumentException | InsufficientStockException | SaleException e){
                            System.out.println(e.getMessage());
                        }
                    }
                    case 2 -> {

                        System.out.println("Enter barcode: ");
                        String barcode = scanner.nextLine();

                        System.out.print("Quantity: ");
                        int quantity = MainFlow.readInt(scanner);

                        Optional<OrderIten> product = cart.getItens().stream()
                                .filter(o -> o.getBarcode().equals(barcode))
                                .findFirst();

                        if (product.isPresent()){

                            try {

                                cart.removeProductByBarcode(barcode, quantity);
                                System.out.println("Item removed Successfully!");
                            }catch (IllegalArgumentException e){
                                System.out.println(e.getMessage());
                            }

                        }else {
                            System.out.println("Product not found");
                        }
                    }
                    case 3 -> cart.clearCart();
                    case 0 -> inCart = false;
                    default -> System.out.println("Invalid choice.");
                }


            }
        }
    }


    private void productFlow(){

        System.out.println("====== Menu Products ======");

        boolean runFlowProduct = true;
        while (runFlowProduct){


            Printer.printProductMenuCustomer();
            byte choiceRunFlow = MainFlow.readByte(scanner);

            Printer.lineBreak();
            switch (choiceRunFlow){

                case 1:

                    Printer.line();
                    MainFlow.menuSortPrice(productService.findAllProducts(), productService, scanner);

                    selectAndAddProduct();
                    Printer.line();

                    break;

                case 2:

                    Printer.line();
                    System.out.print("Provide ID: ");

                    UUID id = null;

                    try {

                        id = UUID.fromString(scanner.nextLine());
                        Product product = productService.findProductById(id);
                        Printer.printProduct(product);

                        Printer.lineBreak();
                        Printer.line();

                        promptAddToCart(product);

                    }catch (IllegalArgumentException | SecurityException e){
                        System.out.println(e.getMessage());

                    }catch (NoSuchElementException e){
                        System.out.println("Product not found.");
                    }

                    Printer.lineBreak();
                    Printer.line();
                    break;

                case 3:

                    Printer.line();
                    System.out.print("Provide Category: ");
                    String category = scanner.nextLine();

                    try {

                        MainFlow.menuSortPrice(productService.findByCategory(category), productService, scanner);
                        Printer.lineBreak();

                        selectAndAddProduct();


                    }catch (NullPointerException e){
                        System.out.println("No products found for category: "+category);
                        System.out.println("Available categories: "+ Arrays.toString(Category.values()));

                    }catch (IllegalArgumentException | SecurityException e){
                        System.out.println(e.getMessage());
                    }

                    Printer.lineBreak();
                    Printer.line();
                    break;

                case 4:

                    Printer.line();
                    System.out.print("Provide brand: ");
                    String brand = scanner.nextLine();

                    try {

                        MainFlow.menuSortPrice(productService.findByBrand(brand), productService, scanner);
                        Printer.lineBreak();

                        selectAndAddProduct();

                    }catch (NullPointerException e){
                        System.out.println("No products found for brand: "+brand);
                    }catch (IllegalArgumentException | SecurityException e){
                        System.out.println(e.getMessage());
                    }

                    Printer.lineBreak();
                    Printer.line();
                    break;

                case 5:

                    Printer.line();
                    System.out.print("Provide name: ");
                    String name = scanner.nextLine();

                    try {

                        Product product = productService.findByname(name);
                        Printer.printProduct(product);

                        Printer.lineBreak();
                        Printer.line();

                        promptAddToCart(product);

                    }catch (NoSuchElementException e){
                        System.out.println("Error: "+e.getMessage());
                    }catch (IllegalArgumentException | SecurityException e){
                        System.out.println(e.getMessage());
                    }

                    Printer.lineBreak();
                    Printer.line();
                    break;

                case 6:

                    Printer.line();
                    System.out.print("Provide barcode: ");
                    String barcode = scanner.nextLine();

                    try {

                        Product product = productService.findProductByBarcode(barcode);
                        Printer.printProduct(product);

                        Printer.lineBreak();
                        Printer.line();

                        promptAddToCart(product);

                    }catch (NoSuchElementException e){
                        System.out.println("Error: "+e.getMessage());
                    }catch (IllegalArgumentException | SecurityException e){
                        System.out.println(e.getMessage());
                    }

                    Printer.lineBreak();
                    Printer.line();
                    break;

                case 0:

                    runFlowProduct = false;
                    break;

                default:
                    System.out.println("Invalid choice");
                    break;
            }
        }


    }

    private void walletFlow(){

        boolean runWallet = true;
        while (runWallet) {

            User user = UserSession.getCurrentUser();
            Wallet wallet = walletService.findByUserID(user.getId());
            Printer.printWalletMenu(wallet, user);
            byte choiceWallet = MainFlow.readByte(scanner);

            switch (choiceWallet) {

                case 1:

                    System.out.print("Provide amount: ");
                    try {
                        BigDecimal b = new BigDecimal(scanner.nextLine().trim().replace(",", "."));
                        walletService.deposit(wallet.getIdUser(), b);

                        Printer.lineBreak();
                        System.out.println("Deposit of $ " + b + " confirmed!");
                        System.out.println("Current balance: " + walletService.findByID(wallet.getId()).getBalance());
                        Printer.lineBreak();

                    }catch (NumberFormatException e){
                        System.out.println("Invalid numeric amount.");
                    }catch (IllegalArgumentException e){
                        System.out.println("Error: "+e.getMessage());
                    }
                    break;

                case 0:
                    runWallet = false;
                    break;

                default:
                    System.out.println("Invalid choice.");
                    break;

            }


        }

    }

    private void settingsFlow(){

        boolean runSettings = true;
        while (runSettings){

            Printer.printSettingsCustomerMenu();
            byte choiceSettings = MainFlow.readByte(scanner);
            Printer.lineBreak();

            switch (choiceSettings) {


                case 1:

                    walletFlow();
                    break;

                case 2:

                    Printer.line();
                    Printer.printUser(UserSession.getCurrentUser());
                    Printer.lineBreak();

                    break;

                case 3:

                    changePassword();
                    break;

                case 4:
                    changeEmail();
                    break;
                case 0:
                    runSettings = false;
                    break;

                default:
                    System.out.println("Invalid choice");
                    break;
            }

        }
    }

    private Scanner sca() {
        return scanner;
    }
    //--------------- Utility ---------------

    private void changePassword(){

        System.out.println("======== Change Password ========");
        System.out.print("Provide the current password: ");
        String currentPassword = scanner.nextLine();

        System.out.print("Provide the new password: ");
        String newPassword = scanner.nextLine();

        try {

            userService.changePassword(currentPassword, newPassword);
            System.out.println("Password updated successfully");
        } catch (IllegalArgumentException e) {

            System.out.println("Erro: " + e.getMessage());
        }

        Printer.lineBreak();

    }

    private void changeEmail(){

        System.out.println("======== Change Email ========");
        System.out.print("Enter new email: ");
        String newEmal = scanner.nextLine();

        System.out.print("Enter your password: ");
        String password = scanner.nextLine();

        try {
            userService.changeEmail(password, newEmal);
            System.out.println("Email updated.");

        }catch (IllegalArgumentException e){
            System.out.println(e.getMessage());
            Printer.lineBreak();
        }

        Printer.lineBreak();

    }

    private void selectAndAddProduct(){

        System.out.println("1 - Add an item from the list cart");
        System.out.println("0 - back");
        System.out.print("-> ");
        byte choice = MainFlow.readByte(scanner);

        if (choice == 1){

            try {

                System.out.print("Enter barCode: ");
                String barcode = scanner.nextLine();

                System.out.print("Enter quantity: ");
                int quantity = MainFlow.readInt(scanner);

                Product product = productService.findProductByBarcode(barcode);
                cart.addProduct(product, quantity);
                System.out.printf("Added %dx '%s' to cart!\n", quantity, product.getName());
            }catch (IllegalArgumentException | NoSuchElementException e){
                System.out.println(e.getMessage());
            }

        }

    }

    private void promptAddToCart(Product p){
        if (p == null){
            throw new IllegalArgumentException("Invalid product");
        }

        System.out.println("1 - Add to cart");
        System.out.println("0 - back");
        byte choice = MainFlow.readByte(scanner);

        if (choice == 1){

            System.out.println("Enter quantity");
            int quantity = MainFlow.readInt(scanner);

            try {
                cart.addProduct(p, quantity);
                Printer.printCart(cart);
            }catch (IllegalArgumentException | SaleException e){
                System.out.println(e.getMessage());
            }

        }
    }



}
