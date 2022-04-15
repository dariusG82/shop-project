package dariusG82;

import dariusG82.services.accounting.*;

import java.io.IOException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static Balance balance = new Balance();
    public static void main(String[] args) {
//        MainService mainService = new MainService();

//        balance.addRecordToBalance(CashOperation.DAILY_INCOME, LocalDate.of(2022,3,9), 1500);
//        balance.addRecordToBalance(CashOperation.DAILY_EXPENSE, LocalDate.of(2022,3,9),520);

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
        System.out.println("Enter the day for balance - format yyyy-mm-dd");
        while (true){
            try {
                LocalDate date = getLocalDate(scanner);
                double cashBalance = balance.getDaysBalance(date);
                System.out.println("*******************");
                System.out.printf("Balance for %s is: %.2f\n", date, cashBalance);
                System.out.println("*******************");
                return;
            } catch (NumberFormatException e){
                System.out.println("Wrong format, try again");
            } catch (DateTimeException e){
                System.out.println("Wrong day/month/year entered, try again");
            }
        }

    }

    private static void getBalanceForAMonth(Scanner scanner){
        System.out.println("Enter the month for balance - format yyyy-mm");
        while (true){
            try {
                LocalDate date = getLocalDate(scanner);
                double cashBalance = balance.getMonthBalance(date);
                System.out.println("*******************");
                System.out.printf("Balance for %d %s is: %.2f\n",date.getYear(), date.getMonth(), cashBalance);
                System.out.println("*******************");
                return;
            } catch (NumberFormatException e){
                System.out.println("Wrong format, try again");
            } catch (DateTimeException e){
                System.out.println("Wrong day/month/year entered, try again");
            }
        }
    }

    private static void getSalesDocumentsByDay(Scanner scanner){

        System.out.println("Enter the day for sales documents balance - format yyyy-mm-dd");
        while (true){
            try {
                LocalDate date = getLocalDate(scanner);
                ArrayList<CashRecord> salesForDay = balance.getDailySaleDocuments(date, CashOperation.DAILY_INCOME);
                System.out.printf("Sales documents for %s day is:\n", date);
                for(CashRecord cashRecord : salesForDay){
                    SalesCashRecord salesCashRecord = (SalesCashRecord) cashRecord;
                    System.out.println("*******************");
                    System.out.printf("Sales document id: %s, amount = %.2f\n",
                            salesCashRecord.getRecordID(), salesCashRecord.getAmount());
                    System.out.println("*******************");
                }
                return;
            } catch (NumberFormatException e){
                System.out.println("Wrong format, try again");
            } catch (DateTimeException e){
                System.out.println("Wrong day/month/year entered, try again");
            }
        }
    }

    private static void getReturnsDocumentsByDay(Scanner scanner){
        System.out.println("Enter the day for returns documents balance - format yyyy-mm-dd");
        while (true){
            try {
                LocalDate date = getLocalDate(scanner);
                ArrayList<CashRecord> salesForDay = balance.getDailySaleDocuments(date, CashOperation.DAILY_EXPENSE);
                System.out.printf("Return documents for %s is:\n", date);
                for(CashRecord cashRecord : salesForDay){
                    ReturnCashRecord returnCashRecord = (ReturnCashRecord) cashRecord;
                    System.out.println("*******************");
                    System.out.printf("Return document id: %s, amount = %.2f\n",
                            returnCashRecord.getRecordID(), returnCashRecord.getAmount());
                    System.out.println("*******************");
                }
                return;
            } catch (NumberFormatException e){
                System.out.println("Wrong format, try again");
            } catch (DateTimeException e){
                System.out.println("Wrong day/month/year entered, try again");
            }
        }
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

    private static LocalDate getLocalDate(Scanner scanner) throws DateTimeException, NumberFormatException{
        String input;
        input = scanner.nextLine();

        int year = Integer.parseInt(input.substring(0,4));
        int month = Integer.parseInt(input.substring(5,7));
        int day = 1;
        if(input.length() == 10){
            day = Integer.parseInt(input.substring(8));
        }

        return LocalDate.of(year, month,day);
    }
}
