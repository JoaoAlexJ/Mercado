package Flow;

import model.dto.ProductRequestDTO;
import model.dto.UserRequestDTO;
import printer.Printer;
import security.UserSession;
import services.ProductService;
import services.UserService;

import javax.print.attribute.standard.JobOriginatingUserName;
import java.util.NoSuchElementException;
import java.util.Scanner;

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

            System.out.println();
            System.out.println("Login or sing up your account!");
            System.out.println("1 - login");
            System.out.println("2 - Sing up");
            System.out.println("0 - exit");
            System.out.print("-> ");
            byte mainChoice = readByte(scanner);

            switch (mainChoice){

                case 1:

                    System.out.print("Email: ");
                    String email = scanner.nextLine();

                    System.out.print("Password: ");
                    String password = scanner.nextLine();


                    //-----------------//

                    try {

                        userService.login(email, password);

                        Printer.lineBreak();
                        System.out.println("Login successful!");

                        if (UserSession.isAdm()) {

                            admFlow.start();
                        } else {

                            customerFlow.start();
                        }

                    }catch (IllegalArgumentException e){
                        System.out.println("Error: "+e.getMessage());


                    }
                    break;

                case 2:

                    System.out.print("Enter your name: ");
                    String name = scanner.nextLine();

                    System.out.print("Enter your email: ");
                    String emailSigUp = scanner.nextLine();

                    System.out.print("Enter a password: ");
                    String passwordSingUp = scanner.nextLine();

                    UserRequestDTO dto = new UserRequestDTO(name, emailSigUp, passwordSingUp);

                    Printer.lineBreak();

                    try {

                        userService.registerCustomer(dto);
                        System.out.println("Registered user successfully!");

                        //--------------------//

                        userService.login(emailSigUp, passwordSingUp);


                        if (UserSession.isAdm()) {

                            admFlow.start();
                        } else {

                            customerFlow.start();
                        }


                    }catch (IllegalArgumentException | NoSuchElementException e){
                        System.out.println("Error: "+e.getMessage());
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

    public static byte readByte(Scanner scanner){

        try{
            return Byte.parseByte(scanner.nextLine());
        }catch (NumberFormatException e) {
            return -1;
        }
    }

}
