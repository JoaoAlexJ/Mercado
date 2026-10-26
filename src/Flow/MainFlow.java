package Flow;

import model.Product.Product;
import model.dto.ProductRequestDTO;
import model.dto.UserRequestDTO;
import printer.Printer;
import security.UserSession;
import services.ProductService;
import services.SortOrder;
import services.UserService;

import javax.print.attribute.standard.JobOriginatingUserName;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainFlow {

    private UserService userService;
    private Scanner scanner;

    private CustomerFlow customerFlow;
    private AdmFlow admFlow;

    public MainFlow(UserService userService, Scanner scanner, CustomerFlow customerFlow, AdmFlow admFlow) {
        this.userService = userService;
        this.scanner = scanner;

        this.customerFlow = customerFlow;
        this.admFlow = admFlow;
    }

    //---------------//


    public Scanner getScanner() {
        return scanner;
    }

    public UserService getUserService() {
        return userService;
    }

    //---------------//

    public void start(){

        boolean runMain = true;
        while (runMain){

            System.out.println("Login or sing up your account!");
            System.out.println("1 - login");
            System.out.println("2 - Sing up");
            System.out.println("0 - exit");
            System.out.print("-> ");
            byte mainChoice = readByte(scanner);
            Printer.line();

            switch (mainChoice){

                case 1:

                    System.out.print("Email: ");
                    String email = scanner.nextLine();

                    System.out.print("Password: ");
                    String password = scanner.nextLine();


                    //-----------------//

                    try {

                        performLogin(email, password);

                    }catch (IllegalArgumentException e){
                        System.out.println("Error: "+e.getMessage());
                        Printer.line();

                    }
                    break;

                case 2:

                    System.out.print("Enter your name: ");
                    String name = scanner.nextLine();

                    System.out.print("Enter your email: ");
                    String emailSigUp = scanner.nextLine();
                    Printer.lineBreak();

                    System.out.println("========= Requirements for a valid password ========= ");
                    System.out.println("1 - Uppercase letter");
                    System.out.println("2 - Lowercase letter");
                    System.out.println("3 - Numbers");
                    System.out.println("4 - Length of 8 to 12 letter.");
                    System.out.println("------------------------------------------------------");

                    System.out.print("Enter a password: ");
                    String passwordSingUp = scanner.nextLine();
                    Printer.lineBreak();

                    UserRequestDTO dto = new UserRequestDTO(name, emailSigUp, passwordSingUp);

                    try {

                        userService.registerCustomer(dto);
                        System.out.println("Registered user successfully!");

                        //--------------------//

                        performLogin(emailSigUp, passwordSingUp);


                    }catch (IllegalArgumentException | NoSuchElementException e){
                        System.out.println("Error: "+e.getMessage());
                        Printer.line();
                        continue;

                    }

                    Printer.lineBreak();
                    Printer.line();
                    break;

                case 0:

                    runMain = false;
                    break;

                default:

                    System.out.println("Invalid choice");
                    break;
            }

        }


    }

    private void performLogin(String email, String password){

        userService.login(email, password);
        System.out.println("Login successful.");
        customerFlow.getCart().setCustomer(UserSession.getCurrentUser());

        if (UserSession.isAdm()) {

            admFlow.start();
        } else {

            customerFlow.start();
        }
    }

    public static void menuSortPrice(List<Product> products, ProductService productService, Scanner scanner){

        boolean runSort = true;
        while (runSort) {
            System.out.println("1 - Price: High to low");
            System.out.println("2 - Price: Low to High");
            System.out.println("3 - Random");
            System.out.println("0 - back");
            System.out.print("-> ");
            byte choiceSort = MainFlow.readByte(scanner);

            switch (choiceSort) {

                case 1 -> {
                    productService.sortedByPrice(products, SortOrder.DESC).forEach(Printer::printProduct);
                    Printer.line();
                }
                case 2 -> {
                    productService.sortedByPrice(products, SortOrder.ASC).forEach(Printer::printProduct);
                    Printer.line();
                }
                case 3 -> {
                    products.forEach(Printer::printProduct);
                    Printer.line();
                }
                case 0 ->{
                    runSort = false;
                    Printer.line();
                }
                default -> System.out.println("Invalid choice");

            }

        }
    }

    public static byte readByte(Scanner scanner){

        try{
            return Byte.parseByte(scanner.nextLine());
        }catch (NumberFormatException e) {
            return -1;
        }
    }

    public static int readInt(Scanner scanner){

        try{
            return Integer.parseInt(scanner.nextLine());
        }catch (NumberFormatException e) {
            return -1;
        }
    }

}
