import Flow.AdmFlow;
import model.User;
import model.UserType;
import repository.ProductRepository;
import repository.ProductRepositoryInMemory;
import repository.UserRepository;
import repository.UserRepositoryInMemory;
import security.UserSession;
import services.ProductService;
import services.UserService;

import java.util.Scanner;
import java.util.UUID;

public class Main {

    static void main(String[] args) {

        ProductRepository productRepository = new ProductRepositoryInMemory();
        ProductService productService = new ProductService(productRepository);

        UserRepository userRepository = new UserRepositoryInMemory();
        UserService userService = new UserService(userRepository);
        Scanner scanner = new Scanner(System.in);

        AdmFlow admFlow = new AdmFlow(userService, productService, scanner);

        User userAdm = new User(UUID.randomUUID(), "joao Alexandre", "joao@gmail.com", "123456", UserType.ADM);
        UserSession.login(userAdm);

        admFlow.start();

    }
}