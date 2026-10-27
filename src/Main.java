import Flow.AdmFlow;
import Flow.CustomerFlow;
import Flow.MainFlow;
import model.Product.Category;
import model.Product.Product;
import model.User.User;
import model.User.UserType;
import model.dto.ProductRequestDTO;
import model.dto.UserRequestDTO;
import model.sale.Cart;
import model.sale.Order;
import printer.Printer;
import repository.*;
import repository.Interfaces.OrderRepository;
import repository.Interfaces.ProductRepository;
import repository.Interfaces.UserRepository;
import repository.Interfaces.WalletRepository;
import security.PasswordHasher;
import security.UserSession;
import services.OrderService;
import services.ProductService;
import services.UserService;
import services.WalletService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class Main {

    static void main(String[] args) {

        ProductRepository productRepository = new ProductRepositoryInMemory();
        UserRepository userRepository = new UserRepositoryInMemory();
        WalletRepository walletRepository = new WalletRepositoryInMemory();
        OrderRepository orderRepository = new OrderRepositoryInMemory();

        WalletService walletService = new WalletService(walletRepository, userRepository );
        UserService userService = new UserService(userRepository, walletService);
        ProductService productService = new ProductService(productRepository);
        OrderService orderService = new OrderService(orderRepository, walletService, productService);

        Scanner scanner = new Scanner(System.in);

        AdmFlow admFlow = new AdmFlow(userService, productService, scanner, orderService);
        CustomerFlow customerFlow = new CustomerFlow(userService, productService, scanner, walletService, new Cart(), orderService);
        MainFlow mainFlow = new MainFlow(userService, scanner, customerFlow, admFlow);


        createdMasterAdmin(userRepository);
        seedProducts(productService);
        seedUsersAndOrders(userService, walletService, productService, orderService);
        //---------------------//

        mainFlow.start();


    }

    public static void createdMasterAdmin(UserRepository userRepository){

        String passwordHash = PasswordHasher.hash("Joao72943816");

        User userAdm = new User(UUID.randomUUID(), "joao Alexandre", "joao@gmail.com", passwordHash, UserType.ADM);
        userRepository.save(userAdm);

    }

    public static void seedUsersAndOrders(UserService userService, WalletService walletService,
                                          ProductService productService, OrderService orderService) {

        userService.registerCustomer(new UserRequestDTO("Maria Silva", "maria@gmail.com", "Maria@123"));
        User maria = userService.findByEmail("maria@gmail.com");

        walletService.deposit(maria.getId(), new BigDecimal("500.00"));

        Product teclado = productService.findProductByBarcode("5099206010130"); // Teclado
        Product mouse = productService.findProductByBarcode("8886419010142");   // Mouse

        userService.login("maria@gmail.com", "Maria@123");

        Cart cart1 = new Cart();
        cart1.setCustomer(maria);
        cart1.addProduct(teclado, 1);
        orderService.completeOrder("Maria@123", cart1);

        Cart cart2 = new Cart();
        cart2.setCustomer(maria);
        cart2.addProduct(mouse, 2);
        Order orderCancelada = orderService.completeOrder("Maria@123", cart2);

        orderService.cancelOrder(orderCancelada.getId());

        UserSession.logout();

        System.out.println("Seed de usuários e pedidos carregado com sucesso!\n");
    }


    public static void seedProducts(ProductService productService){

        List<ProductRequestDTO> productRequestDTOS = new ArrayList<>(List.of(
                // FOOD
                new ProductRequestDTO("Arroz Agulha 1kg", Category.FOOD, "Cigala", "5601009010011", new BigDecimal("1.35"), 50),
                new ProductRequestDTO("Arroz Carolino 1kg", Category.FOOD, "Cigala", "5601170010025", new BigDecimal("1.49"), 40),
                new ProductRequestDTO("Cafe Torrado Lote Chavena 250g", Category.FOOD, "Delta", "5601083010037", new BigDecimal("3.20"), 45),
                new ProductRequestDTO("Capsulas Cafe Q Intensidade 10un", Category.FOOD, "Delta", "5601266010045", new BigDecimal("3.89"), 60),

                // CLEANING
                new ProductRequestDTO("Detergente Loica Limao 500ml", Category.CLEANING, "Fairy", "5410076010059", new BigDecimal("2.19"), 35),
                new ProductRequestDTO("Capsulas Maquina Loica Platinum 30un", Category.CLEANING, "Fairy", "5601199010068", new BigDecimal("9.99"), 20),
                new ProductRequestDTO("Lava Tudo Pinho 1.5L", Category.CLEANING, "Sonasol", "5601324010079", new BigDecimal("2.89"), 25),
                new ProductRequestDTO("Lixivia Gel Multiusos 2L", Category.CLEANING, "Sonasol", "8710447010082", new BigDecimal("1.99"), 30),

                // HYGIENE
                new ProductRequestDTO("Gel de Banho Hidratante 500ml", Category.HYGIENE, "Dove", "8717163010099", new BigDecimal("3.99"), 35),
                new ProductRequestDTO("Desodorizante Roll-On Original 50ml", Category.HYGIENE, "Dove", "4005808010127", new BigDecimal("2.59"), 40),
                new ProductRequestDTO("Pasta Dentifrica Total 75ml", Category.HYGIENE, "Colgate", "8718951010109", new BigDecimal("2.49"), 50),
                new ProductRequestDTO("Elixir Bucal Plax Mentol 500ml", Category.HYGIENE, "Colgate", "8001090010117", new BigDecimal("4.29"), 25),

                // ELECTRONICS
                new ProductRequestDTO("Teclado Mecanico G413 SE", Category.ELECTRONICS, "Logitech", "5099206010130", new BigDecimal("79.90"), 10),
                new ProductRequestDTO("Rato Gaming G502 Hero", Category.ELECTRONICS, "Logitech", "8886419010142", new BigDecimal("49.99"), 15),
                new ProductRequestDTO("Headset Kraken X Lite", Category.ELECTRONICS, "Razer", "8806091010155", new BigDecimal("44.90"), 12),
                new ProductRequestDTO("Tapete Gaming Gigantus V2", Category.ELECTRONICS, "Razer", "4548736010161", new BigDecimal("19.99"), 25)
        ));

        productRequestDTOS.forEach(productService::registerProduct);
    }
}