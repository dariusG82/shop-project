package dariusG82;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input;
        int option = 0;

        while (true){
            printMenu();
            try {
                input = scanner.nextLine();
                option = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Wrong input format, try again");
            }
            switch (option){
                case 1 -> loginAsAccountant(scanner);
                case 2 -> loginAsSalesman(scanner);
                case 3 -> loginAsITSupport(scanner);
                case 9 ->{
                    return;
                }
                default -> System.out.println("Unavailable option");
            }
        }

    }

    public static void printMenu(){
        System.out.println("Choose your function:");
        System.out.println("[1] - Accounting login");
        System.out.println("[2] - Sales login");
        System.out.println("[3] - IT login");
        System.out.println("[9] - Exit");
    }

    public static void loginAsAccountant(Scanner scanner){
        String input;
        int option = 0;
        while (true){
            printAccountantMenu();
            try {
                input = scanner.nextLine();
                option = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Wrong input format, try again");
            }
            switch (option){
                case 1 -> getBalanceForADay(scanner);
                case 2 -> getBalanceForAMonth(scanner);
                case 3 -> getSalesDocumentsByDay(scanner);
                case 4 -> getReturnsDocumentsByDay(scanner);
                case 5 -> getSalesBySellerByMonth(scanner);
                case 6 -> getClientIDByClientName(scanner);
                case 9 -> {
                    return;
                }
                default -> System.out.println("Unavailable option");
            }
        }
    }

    public static void printAccountantMenu(){
        System.out.println("Accountant Operations:");
        System.out.println("**********************");
        System.out.println("[1] - Balance for a day");
        System.out.println("[2] - Balance for a month");
        System.out.println("[3] - Sales documents by day");
        System.out.println("[4] - Returns documents by day");
        System.out.println("[5] - Month sales by seller");
        System.out.println("[6] - Get clientID by client name");
        System.out.println("[9] - Return to previous menu");
    }

    private static void getBalanceForADay(Scanner scanner){
        //TODO implement method
    }

    private static void getBalanceForAMonth(Scanner scanner){
        //TODO implement method
    }

    private static void getSalesDocumentsByDay(Scanner scanner){
        //TODO implement method
    }

    private static void getReturnsDocumentsByDay(Scanner scanner){
        //TODO implement method
    }

    private static void getSalesBySellerByMonth(Scanner scanner){
        //TODO implement method
    }

    private static void getClientIDByClientName(Scanner scanner){
        //TODO implement method
    }

    public static void printClientsOperationsMenu(){
        System.out.println("Clients Operations");
        System.out.println("******************");
        System.out.println("[1] - New client registration");
        System.out.println("[2] - Find client by name");
        System.out.println("[3] - Delete client");
        System.out.println("[9] - Return to previous menu");
    }

    public static void loginAsSalesman(Scanner scanner){
        printSalesmanMenu();
    }

    public static void printSalesmanMenu(){
        System.out.println("Sales Operations:");
        System.out.println("**********************");
        System.out.println("[1] - Sale operation");
        System.out.println("[2] - Find sales document by document Nr.");
        System.out.println("[3] - Return operation");
        System.out.println("[4] - Find return document by document Nr. ");
        System.out.println("[9] - Return to previous menu");
    }

    public static void loginAsITSupport(Scanner scanner){
        printITSupportMenu();
    }

    public static void printITSupportMenu(){
        System.out.println("IT Support Operations:");
        System.out.println("**********************");
        System.out.println("[1] - Print list of users");
        System.out.println("[2] - Register new user");
        System.out.println("[3] - Remove user");
        System.out.println("[9] - Return to previous menu");
    }
}
