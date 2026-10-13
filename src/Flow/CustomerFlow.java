package Flow;

import services.ProductService;
import services.UserService;

import java.util.Scanner;

public class CustomerFlow {

    private final UserService userService;
    private final ProductService productService;
    private final Scanner scanner;

    public CustomerFlow(UserService userService, ProductService productService, Scanner scanner) {
        this.userService = userService;
        this.productService = productService;
        this.scanner = scanner;
    }

    public void start(){




    }
}
