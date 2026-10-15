import Flow.AdmFlow;
import model.User;
import model.UserType;
import repository.*;
import security.UserSession;
import services.ProductService;
import services.UserService;
import services.WalletService;

import java.util.Scanner;
import java.util.UUID;

public class Main {

    static void main(String[] args) {

        ProductRepository productRepository = new ProductRepositoryInMemory();
        UserRepository userRepository = new UserRepositoryInMemory();
        WalletRepository walletRepository = new WalletRepositoryInMemory();

        WalletService walletService = new WalletService(walletRepository, userRepository );
        UserService userService = new UserService(userRepository, walletService);
        ProductService productService = new ProductService(productRepository);

        Scanner scanner = new Scanner(System.in);

        AdmFlow admFlow = new AdmFlow(userService, productService, scanner);

        User userAdm = new User(UUID.randomUUID(), "joao Alexandre", "joao@gmail.com", "123456", UserType.ADM);
        UserSession.login(userAdm);

        admFlow.start();

    }
}