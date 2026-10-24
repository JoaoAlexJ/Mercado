package Flow;

import model.Product.Category;
import model.User.User;
import model.User.Wallet;
import model.sale.Cart;
import printer.Printer;
import security.UserSession;
import services.ProductService;
import services.UserService;
import services.WalletService;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.UUID;

public class CustomerFlow {

    private final UserService userService;
    private final ProductService productService;
    private final WalletService walletService;
    private final Scanner scanner;

    private final Cart cart;

    public CustomerFlow(UserService userService, ProductService productService, Scanner scanner, WalletService walletService, Cart cart) {
        this.userService = userService;
        this.productService = productService;
        this.scanner = scanner;
        this.walletService = walletService;
        this.cart = cart;
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

    private void productFlow(){

        System.out.println("====== Menu Products ======");

        boolean runFlowProduct = true;
        while (runFlowProduct){


            Printer.printProductMenuCustomer();
            byte choiceRunFlow = MainFlow.readByte(scanner);

            Printer.lineBreak();
            switch (choiceRunFlow){

                case 1:

                    productService.findAllProducts().forEach(Printer::printProduct);

                    Printer.lineBreak();
                    Printer.line();
                    break;

                case 2:

                    System.out.print("Provide ID: ");

                    UUID id = null;

                    try {

                        id = UUID.fromString(scanner.nextLine());

                        Printer.printProduct(productService.findProductById(id));

                    }catch (IllegalArgumentException e){
                        System.out.println(e.getMessage());

                    }catch (NoSuchElementException e){
                        System.out.println("Product not found.");
                    }

                    Printer.lineBreak();
                    Printer.line();
                    break;

                case 3:

                    System.out.print("Provide Category: ");
                    String category = scanner.nextLine();

                    try {

                        productService.findByCategory(category).forEach(Printer::printProduct);
                    }catch (NullPointerException e){
                        System.out.println("No products found for category: "+category);
                        System.out.println("Available categories: "+ Arrays.toString(Category.values()));
                    }

                    Printer.lineBreak();
                    Printer.line();
                    break;

                case 4:

                    System.out.print("Provide brand: ");
                    String brand = scanner.nextLine();

                    try {

                        productService.findByBrand(brand).forEach(Printer::printProduct);
                    }catch (NullPointerException e){
                        System.out.println("No products found for brand: "+brand);
                    }

                    Printer.lineBreak();
                    Printer.line();
                    break;

                case 5:

                    System.out.print("Provide name: ");
                    String name = scanner.nextLine();

                    try {

                        Printer.printProduct(productService.findByname(name));
                    }catch (NoSuchElementException e){
                        System.out.println("Error: "+e.getMessage());
                    }

                    Printer.lineBreak();
                    Printer.line();
                    break;

                case 6:

                    System.out.print("Provide barcode: ");
                    String barcode = scanner.nextLine();

                    try {

                        Printer.printProduct(productService.findProductByBarcode(barcode));
                    }catch (NoSuchElementException e){
                        System.out.println("Error: "+e.getMessage());
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

            Wallet wallet = walletService.findByUserID(UserSession.getCurrentUser().getId());
            Printer.printWalletMenu(wallet);
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
        Printer.line();


    }
    private void settingsFlow(){

        boolean runSettings = true;
        while (runSettings){

            Printer.printSettingsCustomerMenu();
            byte choiceSettings = MainFlow.readByte(scanner);

            switch (choiceSettings) {


                case 1:

                    walletFlow();
                    break;

                case 2:

                    User u = UserSession.getCurrentUser();
                    Printer.printUser(u);
                    Printer.lineBreak();

                    break;

                case 3:

                    changePassword();
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
}
