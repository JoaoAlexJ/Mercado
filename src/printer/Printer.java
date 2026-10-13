package printer;

import model.Product;
import model.User;

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
        System.out.println("Price: "+p.getPrice());
        System.out.println("Barcode: "+p.getBarCode());
        System.out.println("Stock: "+p.getStock());
    }

    public static void printUser(User u){

        System.out.println("        User");
        System.out.println("ID: "+u.getId());
        System.out.println("Name: "+u.getName());
        System.out.println("Email: "+u.getEmail());
        System.out.println("User Type: "+u.getUserType());
    }

    public static void printMainAdmMenu(){

        System.out.println("==== Main Adm menu ====");
        System.out.println("1 - Products");
        System.out.println("2 - Users");
        System.out.println("3 - exit");
        System.out.print("-> ");
    }

    public static void printProductMenuAdm(){
        System.out.println("1 - Register product");
        System.out.println("2 - Find all products");
        System.out.println("3 - Find by category");
        System.out.println("4 - Find product by ID");
        System.out.println("5 - find product by barcode");
        System.out.println("6 - Delete by ID");
        System.out.println("7 - Dele by barcode");
        System.out.println("0 - exit");
        System.out.print("-> ");
    }

    public static void printUserMeenuAdm(){

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
}
