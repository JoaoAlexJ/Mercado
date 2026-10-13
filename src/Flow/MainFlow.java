package Flow;

import services.ProductService;
import services.UserService;

import java.util.Scanner;

public class MainFlow {

    private UserService userService;
    private Scanner scanner;

    public MainFlow(UserService userService, Scanner scanner) {
        this.userService = userService;
        this.scanner = scanner;
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

        System.out.println("Login or sing up your account!");
        System.out.println();
        System.out.println("1 - login");
        System.out.println("2 - Sing up");
        System.out.print("-> \n");
        byte mainChoice = scanner.nextByte();

        scanner.nextLine();

        switch (mainChoice){

            case 1:

                System.out.print("Email: \n");
                String email = scanner.nextLine();

                System.out.print("Password: \n");
                String password = scanner.nextLine();

                userService.login(email, password);



            case 2:






        }





    }

}
