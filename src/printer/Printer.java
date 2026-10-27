package printer;

import model.Product.Product;
import model.User.User;
import model.User.Wallet;
import model.sale.Cart;
import model.sale.Order;
import model.sale.OrderIten;

import java.security.PublicKey;

public class Printer {

    public static void lineBreak(){
        System.out.println();
    }

    public static void line(){
        System.out.println("-----------------------");
    }

    public static void printProduct(Product p){

        System.out.println("======= Product =======");
        System.out.println("ID: "+p.getId());
        System.out.println("Name: "+p.getName());
        System.out.println("Brand: "+p.getBrand());
        System.out.println("Category: "+p.getCategory());
        System.out.println("Price: €"+p.getPrice());
        System.out.println("Barcode: "+p.getBarCode());
        System.out.println("Stock: "+p.getStock());
        lineBreak();
    }

    public static void printUser(User u){

        System.out.println("        User");
        System.out.println("ID: "+u.getId());
        System.out.println("Name: "+u.getName());
        System.out.println("Email: "+u.getEmail());
        System.out.println("User Type: "+u.getUserType());
        lineBreak();
        line();
    }

    public static void printMainAdmMenu(){

        System.out.println("==== Main Adm menu ====");
        System.out.println("1 - Products");
        System.out.println("2 - Users");
        System.out.println("3 - Orders");
        System.out.println("0 - exit");
        System.out.print("-> ");
    }

    public static void printMainCustomerMenu(){

        System.out.println("===== Main customer menu ===== ");
        System.out.println("1 - Account Settings ");
        System.out.println("2 - Products");
        System.out.println("3 - Orders");
        System.out.println("4 - Cart");
        System.out.println("0 - exit");
        System.out.print("-> ");
    }

    public static void printProductMenuAdm(){
        System.out.println("1 - Register product");
        System.out.println("2 - Find all products");
        System.out.println("3 - Find by category");
        System.out.println("4 - Find by ID");
        System.out.println("5 - Find by barcode");
        System.out.println("6 - Find by Brand");
        System.out.println("7 - Find by Name");
        System.out.println("8 - Delete by ID");
        System.out.println("9 - Dele by barcode");
        System.out.println("10 - Update product");
        System.out.println("0 - exit");
        System.out.print("-> ");
    }

    public static void printUserMenuAdm(){

        System.out.println("1 - Adm register");
        System.out.println("2 - Customer register");
        System.out.println("3 - Find all costumer");
        System.out.println("4 - Find all administrators");
        System.out.println("5 - Find all users");
        System.out.println("6 - find by ID");
        System.out.println("7 - find by Email");
        System.out.println("8 - Delete by ID");
        System.out.println("9 - Delete by Email");
        System.out.println("0 - exit");
        System.out.print("-> ");
    }

    public static void printProductMenuCustomer(){

        System.out.println("1 - find all products");
        System.out.println("2 - find by ID");
        System.out.println("3 - find by category");
        System.out.println("4 - find by brand");
        System.out.println("5 - find by name");
        System.out.println("6 - find by barcode");
        System.out.println("0 - back");
        System.out.print("-> ");

    }

    public static void printSettingsCustomerMenu(){

        System.out.println("==== Account Settings ====");
        System.out.println("1 - Wallet (Balance and Deposit)");
        System.out.println("2 - view data");
        System.out.println("3 - Change password");
        System.out.println("0 - Back");
        System.out.print("-> ");

    }

    public static void printWalletMenu(Wallet wallet, User user){

        System.out.println("==== Wallet ====");
        System.out.println("Wallet ID: "+wallet.getId());
        System.out.println("User: "+user.getEmail()+" | "+user.getName());
        System.out.println("Balance: €"+wallet.getBalance());
        System.out.println("-----------------");
        System.out.println("1 - Deposit");
        System.out.println("0 - back");
        System.out.print("-> ");

    }

    public static void printCart(Cart cart){

        System.out.println("=========  \uD83D\uDED2 Shopping cart =========");
        System.out.println("Customer: "+cart.getCustomer().getName());
        System.out.println("Value: €"+cart.getValue());
        System.out.println("  --------- Items ---------");
        cart.getItens().forEach(Printer::printOrderItem);
    }

    public static void printOrderItem(OrderIten orderIten){
        System.out.println("Product ID: "+orderIten.getIdProduct());
        System.out.println("Name: "+orderIten.getNameProduct());
        System.out.println("Brand: "+orderIten.getBrand());
        System.out.println("Barcode: "+orderIten.getBarcode());
        System.out.println("Quantity: "+orderIten.getQuantity());
        System.out.println("Unit Price: €"+orderIten.getUnitPrice()+" Total price: €"+orderIten.getSubtotal());

        lineBreak();
        line();
    }

    public static void printOrder(Order order){
        System.out.println("        \uD83D\uDCE6 Order");
        System.out.println("ID: "+order.getId());
        System.out.println("Customer: "+order.getCustumer().getName());
        System.out.println("Date: "+order.getDate());
        System.out.println("Value: €"+order.getValue());
        System.out.println("Status: "+order.getStatus());
        System.out.println("  --------- Items ---------");
        order.getItens().forEach(o -> System.out.println(o.getQuantity()+"X "+o.getNameProduct()+" - €"+o.getUnitPrice()));
    }



}
