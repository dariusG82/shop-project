package dariusG82;

public class Main {

    public static void main(String[] args) {
        printMenu();
    }

    public static void printMenu(){
        System.out.println("Choose your function:");
        System.out.println("[1] - Accounting login");
        System.out.println("[2] - Sales login");
        System.out.println("[3] - IT login");
        System.out.println("[9] - Exit");
    }

    public static void loginAsAccountant(){

    }

    public static void printAccountantMenu(){
        System.out.println("Accountant Operations:");
        System.out.println("**********************");
        System.out.println("[1] - Cash operations");
        System.out.println("[2] - Clients info/registration");
        System.out.println("[9] - Return to previous menu");
    }

    public static void printCashOperationsMenu(){
        System.out.println("Cash Operations");
        System.out.println("***************");
        System.out.println("[1] - Balance for a day");
        System.out.println("[2] - Balance for a month");
        System.out.println("[3] - Sales documents by day");
        System.out.println("[4] - Returns documents by day");
        System.out.println("[5] - Month sales by seller");
        System.out.println("[9] - Return to previous menu");
    }

    public static void printClientsOperationsMenu(){
        System.out.println("Clients Operations");
        System.out.println("******************");
        System.out.println("[1] - New client registration");
        System.out.println("[2] - Find client by name");
        System.out.println("[3] - Delete client");
        System.out.println("[9] - Return to previous menu");
    }

    public static void loginAsSalesman(){

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

    public static void loginAsITSupport(){

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
