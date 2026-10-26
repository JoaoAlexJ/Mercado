package model.Ultility;

import Flow.MainFlow;
import model.sale.OrderStatus;
import printer.Printer;
import services.OrderService;

import java.util.Scanner;
import java.util.UUID;

public class FindOrdersByStatus {


    public static void inMemory(Scanner scanner, OrderService orderService){

        boolean runOrders = true;
        while (runOrders){

            System.out.println("--------- Orders ---------");
            System.out.println("1 - List cancelled orders");
            System.out.println("2 - List placed orders");
            System.out.println("3 - List all orders");
            System.out.println("0 - back");
            System.out.print("-> ");
            byte choiceOrder = MainFlow.readByte(scanner);

            switch (choiceOrder){

                case 1 -> orderService.findAll().stream()
                        .filter(o -> o.getStatus() == OrderStatus.CANCELED)
                        .toList().forEach(Printer::printOrder);

                case 2 ->  orderService.findAll()
                        .stream().filter(o -> o.getStatus() == OrderStatus.PAID)
                        .toList().forEach(Printer::printOrder);

                case 3 -> orderService.findAll()
                        .forEach(Printer::printOrder);

                case 0 -> runOrders = false;
                default -> System.out.println("Invalid choice");
            }

        }
    }


    public static void byIdCustomer(UUID idCustomer, Scanner scanner, OrderService orderService){

        boolean runOrders = true;
        while (runOrders){

            System.out.println("--------- Orders ---------");
            System.out.println("1 - List cancelled orders");
            System.out.println("2 - List placed orders");
            System.out.println("3 - List all orders");
            System.out.println("0 - back");
            System.out.print("-> ");
            byte choiceOrder = MainFlow.readByte(scanner);

            switch (choiceOrder){

                case 1 -> orderService.findByCustomer(idCustomer).stream()
                        .filter(o -> o.getStatus() == OrderStatus.CANCELED)
                        .toList().forEach(Printer::printOrder);

                case 2 ->  orderService.findByCustomer(idCustomer)
                        .stream().filter(o -> o.getStatus() == OrderStatus.PAID)
                        .toList().forEach(Printer::printOrder);

                case 3 -> orderService.findByCustomer(idCustomer)
                        .forEach(Printer::printOrder);

                case 0 -> runOrders = false;
                default -> System.out.println("Invalid choice");
            }

        }
    }
}
