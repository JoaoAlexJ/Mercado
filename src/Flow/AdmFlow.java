package Flow;

import model.Product.Category;
import model.Product.Product;
import model.Ultility.FindOrdersByStatus;
import model.dto.ProductRequestDTO;
import model.dto.UserRequestDTO;
import model.sale.Order;
import model.sale.OrderStatus;
import printer.Printer;
import security.UserSession;
import services.OrderService;
import services.ProductService;
import services.SortOrder;
import services.UserService;

import javax.print.attribute.standard.PrinterLocation;
import java.math.BigDecimal;
import java.util.*;

public class AdmFlow {

    private final UserService userService;
    private final ProductService productService;
    private final Scanner scanner;
    private final OrderService orderService;

    public AdmFlow(UserService userService, ProductService productService, Scanner scanner, OrderService orderService) {
        this.userService = userService;
        this.productService = productService;
        this.scanner = scanner;
        this.orderService = orderService;
    }

    //--------------------//


    public void start(){

        if (!UserSession.isAuthenticated()){
            throw new SecurityException("No users connected.");
        }

        if (!UserSession.isAdm()){
            throw new SecurityException("This flow is for administrators only");
        }


        boolean runningMain = true;
        while (runningMain){

            Printer.printMainAdmMenu();
            byte mainChoice = MainFlow.readByte(scanner);
            Printer.lineBreak();
            Printer.line();

            switch (mainChoice){

                case 1:

                    runProductMenu();
                    break;

                case 2:

                    runUserMenu();
                    break;

                case 3:

                    runOrdersMenu();
                    break;

                case 0:

                    UserSession.logout();
                    runningMain = false;
                    break;

                default:
                    System.out.println("Invalid choice.");
                    Printer.line();
                    break;

            }

        }

    }



    //--------------------------------------- Registers ---------------------------------------

    private void registerProduct(){

        try {

            System.out.print("Product name: ");
            String name = scanner.nextLine();

            System.out.print("Categoria: ");
            String category = scanner.nextLine();

            Category categoryOfc = null;

            try{
                categoryOfc = Category.valueOf(category.trim().toUpperCase());

            } catch (IllegalArgumentException e){
                System.err.println("Invalid category, Available categorys: ");
                Arrays.stream(Category.values()).forEach(System.err::println);
                return;
            }

            System.out.print("Brand: ");
            String brand = scanner.nextLine();

            System.out.print("Barcode: ");
            String barcode = scanner.nextLine();

            System.out.print("Price: ");
            BigDecimal price = new BigDecimal(scanner.nextLine().trim().replace(",", ".")) ;

            System.out.print("Estoque: ");
            int stock = Integer.parseInt(scanner.nextLine());


            ProductRequestDTO productRequestDTO = new ProductRequestDTO(
                    name,categoryOfc, brand, barcode, price, stock
            );

            Product p = productService.registerProduct(productRequestDTO);

            Printer.lineBreak();
            Printer.line();
            System.out.println("Product registered!");
            Printer.printProduct(p);

        }catch (NumberFormatException e ) {
            System.out.println("Invalid numeric value for price or stock.");
        }catch (IllegalArgumentException e ){
            System.out.println("Validation failed: "+e.getMessage());
        }

    }

    private void registerUserAdm(){

        System.out.print("Name: ");
        String name = scanner.nextLine();

        System.out.print("Email: ");
        String email = scanner.nextLine();

        System.out.print("Password: ");
        String password = scanner.nextLine();

        UserRequestDTO dto = new UserRequestDTO(name, email, password);

        userService.registerAdm(dto);
        System.out.println("Registered user!");

        Printer.lineBreak();
        Printer.line();
    }

    private void registerCustomer(){

        System.out.print("Name: ");
        String name = scanner.nextLine();

        System.out.print("Email: ");
        String email = scanner.nextLine();

        System.out.print("Password: ");
        String password = scanner.nextLine();

        UserRequestDTO dto = new UserRequestDTO(name, email, password);

        userService.registerCustomer(dto);
        System.out.println("Registered user!");

        Printer.lineBreak();
        Printer.line();


    }


    //--------------------------------------- Running Menu ---------------------------------------

    private void runOrdersMenu(){
        boolean runOrders = true;
        while (runOrders){

            System.out.println("----------- \uD83D\uDCE6 Orders -----------");
            System.out.println("1 - find order by ID");
            System.out.println("2 - find customers orders");
            System.out.println("3 - find all orders");
            System.out.println("4 - cancel order");
            System.out.println("0 - back");
            System.out.print("-> ");
            byte choiceOrders = MainFlow.readByte(scanner);

            switch (choiceOrders){

                case 1 -> {

                    System.out.print("Enter ID: ");
                    String id = scanner.nextLine();

                    try {

                        Printer.printOrder(orderService.findById(UUID.fromString(id)));

                    }catch (IllegalArgumentException e){
                        System.out.println(e.getMessage());
                    }
                }
                case 2 -> {

                    System.out.print("Enter the customer ID: ");
                    String id = scanner.nextLine();

                    try {

                        FindOrdersByStatus.byIdCustomer(UUID.fromString(id), scanner, orderService);

                    }catch (IllegalArgumentException | NoSuchElementException e){
                        System.out.println(e.getMessage());
                    }
                }

                case 3 -> {

                    try {

                        FindOrdersByStatus.inMemory(scanner, orderService);
                    }catch (IllegalArgumentException | NoSuchElementException e){
                        System.out.println(e.getMessage());
                    }

                }
                case 4 -> {

                    System.out.print("Enter order ID: ");
                    String id = scanner.nextLine();

                    try {
                        orderService.cancelOrder(UUID.fromString(id));
                    }catch (IllegalArgumentException | NoSuchElementException e ){
                        System.out.println(e.getMessage());
                    }
                }
                case 0 -> runOrders = false;
                default -> System.out.println("Invalid choice");

            }

        }



    }

    private void runProductMenu(){

        boolean runningProduct = true;
        while (runningProduct){

            Printer.printProductMenuAdm();
            byte productChoice = MainFlow.readByte(scanner);

            switch (productChoice){

                case 1:

                    registerProduct();
                    Printer.lineBreak();
                    Printer.line();

                    break;

                case 2:

                    Printer.line();
                    MainFlow.menuSortPrice(productService.findAllProducts(), productService, scanner);
                    Printer.lineBreak();
                    break;

                case 3:

                    Printer.line();
                    System.out.print("Provide Category: ");
                    String categoryInput = scanner.nextLine();

                    try {

                        MainFlow.menuSortPrice(productService.findByCategory(categoryInput), productService, scanner);

                    }catch (NullPointerException e){
                        System.out.println("No products found for category: "+categoryInput);
                        System.out.println("Available categories: "+ Arrays.toString(Category.values()));
                    }

                    Printer.lineBreak();

                    break;

                case 4:

                    System.out.print("Provide ID: ");
                    try {
                        UUID id = UUID.fromString(scanner.nextLine());

                        Product p = productService.findProductById(id);


                        Printer.printProduct(p);
                    }catch (IllegalArgumentException e){
                        System.err.println("Invalid ID format.");

                    }catch (NoSuchElementException e){
                        System.err.println("Product not found");

                    }


                    Printer.lineBreak();
                    Printer.line();

                    break;

                case 5:

                    Printer.line();
                    System.out.print("Provide barcode: ");
                    String barcodeInput = scanner.nextLine();

                    try {

                         Printer.printProduct(productService.findProductByBarcode(barcodeInput));

                    }catch (NoSuchElementException e ){
                        System.err.println("Product not found");

                    }

                    Printer.lineBreak();
                    Printer.line();
                    break;

                case 6:

                    Printer.line();
                    System.out.println("Provide the brand: ");
                    String brand = scanner.nextLine();

                    MainFlow.menuSortPrice(productService.findByBrand(brand), productService, scanner);

                    Printer.line();
                    Printer.lineBreak();
                    break;

                case 7:

                    Printer.line();
                    System.out.print("Provide the name: ");
                    String name = scanner.nextLine();

                    try {

                        Printer.printProduct(productService.findByname(name));
                    }catch (NoSuchElementException e){
                        System.out.println("Error: "+e.getMessage());
                    }

                    Printer.line();
                    Printer.lineBreak();
                    break;

                case 8:

                    Printer.line();
                    System.out.print("Provide ID: ");

                    try {
                        UUID idDel = UUID.fromString(scanner.nextLine());

                        Product pDel = productService.deleteById(idDel);

                        System.out.println("Product Deleted");
                        Printer.printProduct(pDel);

                    }catch (IllegalArgumentException e){
                        System.err.println("Invalid UUID format: ");

                    }catch (NoSuchElementException e){
                        System.err.println("Product not found: "+e.getMessage());

                    }
                    Printer.lineBreak();
                    Printer.line();
                    break;

                case 9:

                    Printer.line();
                    System.out.print("Provide barcode: ");
                    String barcodeDel = scanner.nextLine();

                    try {

                        Printer.printProduct(productService.deleteByBarcode(barcodeDel));
                        System.out.println("Product Deleted");

                    }catch (NoSuchElementException e){
                        System.err.println("Product not found.");

                    }
                    Printer.lineBreak();
                    Printer.line();

                    break;

                case 10:

                    System.out.print("Enter ID product: ");

                   try {
                       UUID id = UUID.fromString(scanner.nextLine());
                       updateProduct(id);
                   }catch (IllegalArgumentException e){
                       System.out.println(e.getMessage());
                   }
                    break;

                case 0:
                    runningProduct = false;
                    break;

                default:
                    System.out.println("Invalid choice");
                    Printer.line();
                    break;

            }

        }
    }

    private void runUserMenu(){

        boolean runUserMenu = true;
        while (runUserMenu){

            Printer.printUserMenuAdm();
            byte choiceUser = MainFlow.readByte(scanner);

            switch (choiceUser){

                case 1:

                    registerUserAdm();
                    break;

                case 2:

                    registerCustomer();
                    break;

                case 3:

                    userService.findAllCustomers().forEach(Printer::printUser);
                    Printer.lineBreak();
                    break;

                case 4:

                    userService.findAllAdm().forEach(Printer::printUser);
                    Printer.lineBreak();
                    break;

                case 5:

                    userService.findAllUsers().forEach(Printer::printUser);
                    Printer.lineBreak();
                    break;

                case 6:

                    System.out.print("Provide ID: ");
                    try {
                        Printer.printUser(userService.findById(UUID.fromString(scanner.nextLine())));
                        Printer.lineBreak();
                        Printer.line();

                    }catch (IllegalArgumentException e ){
                        System.out.println("Invalid ID.");


                    }catch (NoSuchElementException e){
                        System.out.println("User not found: "+e.getMessage());

                    }

                    break;

                case 7:

                    System.out.print("Provide Email: ");

                    try {

                        Printer.printUser(userService.findByEmail(scanner.nextLine()));
                    }catch (NoSuchElementException e){
                        System.err.println("User not found. ");
                    }catch (SecurityException e){
                        System.err.println(e.getMessage());
                    }

                    Printer.lineBreak();
                    Printer.line();
                    break;

                case 8:

                    System.out.print("Provide ID: ");

                    try {

                        userService.deleteById(UUID.fromString(scanner.nextLine()));
                        System.out.println("User deleted!");

                    }catch (IllegalArgumentException e){
                        System.err.println("Invalid ID.");


                    }catch (NoSuchElementException e){
                        System.err.println("User not found.");

                    }

                    break;
                case 9:

                    System.out.print("Provide Email: ");

                    try {

                        Printer.printUser(userService.deleteByEmail(scanner.nextLine()));

                    }catch (NoSuchElementException e){
                        System.err.println("User not found.");

                    }catch (SecurityException e){
                        System.err.println(e.getMessage());

                    }

                    Printer.lineBreak();
                    Printer.line();
                    break;

                case 0:

                    runUserMenu = false;
                    break;

                default:
                    System.out.println("Invalid choice");
                    Printer.line();
                    break;

            }

        }
    }

    //---------------------------- Utility ----------------------------//

    private Product updateProduct(UUID id){

        Product p = productService.findProductById(id);

        System.out.println("Updating product: "+p.getName());
        System.out.println("Enter new values. [press ENTER to keep current value]");

        System.out.print("New name ["+p.getName()+"]: ");
        String newName = scanner.nextLine();

        newName = newName.isBlank() ? p.getName() : newName;

        System.out.println("New price ["+p.getPrice()+"]: ");
        String priceInput = scanner.nextLine();

        BigDecimal newPrice = priceInput.isBlank() ? p.getPrice() : new BigDecimal(priceInput);

        return productService.updateProduct(p, newName, newPrice);

    }



}
