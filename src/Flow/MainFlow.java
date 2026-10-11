package Flow;

import services.ProductService;
import services.UserService;

public class MainFlow {

    private UserService userService;
    private ProductService productService;

    public MainFlow(UserService userService, ProductService productService) {
        this.userService = userService;
        this.productService = productService;
    }

    //---------------//

    public UserService getUserService() {
        return userService;
    }

    public ProductService getProductService() {
        return productService;
    }

    //---------------//

    public void start(){

        System.out.println("Login or sing up your account!");
        System.out.println();
        System.out.println("Email: ");




    }

}
