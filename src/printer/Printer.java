package printer;

import model.Product;

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

    public static void printMainProductMenu(){

        System.out.println("==== Main Adm menu ====");
        System.out.println("1 - Products");
        System.out.println("2 - Users");
        System.out.println("3 - exit");
        System.out.print("-> ");
    }

    public static void printProductMenuAdm(){
        System.out.println("1 - Register product");
        System.out.println("2 - List all products");
        System.out.println("3 - Find product by ID");
        System.out.println("4 - find product by barcode");
        System.out.println("5 - Delete by ID");
        System.out.println("6 - Dele by barcode");
        System.out.println("7 - exit");
        System.out.print("-> ");
    }
}
