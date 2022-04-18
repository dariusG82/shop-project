package dariusG82;

import dariusG82.services.accounting.AccountingService;
import dariusG82.services.accounting.finance.*;
import dariusG82.services.partners.*;
import dariusG82.services.admin.AdminService;
import dariusG82.services.admin.users.User;
import dariusG82.services.admin.users.UserNotFoundException;
import dariusG82.services.admin.users.UserType;
import dariusG82.services.partners.helpers.PartnerDoesNotExistExeption;

import java.io.IOException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static AccountingService accountingService = new AccountingService();
    public static AdminService adminService = new AdminService();
    public static BusinessService businessService = new BusinessService();

    public static void main(String[] args) {
//        MainService mainService = new MainService();

//        balance.addRecordToBalance(CashOperation.DAILY_INCOME, LocalDate.of(2022,3,9), 1500);
//        balance.addRecordToBalance(CashOperation.DAILY_EXPENSE, LocalDate.of(2022,3,9),520);

        Scanner scanner = new Scanner(System.in);
        String input;
        int option = 0;

        while (true) {
            printMenu();
            try {
                input = scanner.nextLine();
                option = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Wrong input format, try again");
            }
            switch (option) {
                case 1 -> {
                    try {
                        UserType type = UserType.ACCOUNTING;
                        User accountingUser = confirmLoginAndGetUser(scanner, type);
                        loginAsAccountant(scanner, accountingUser);
                    } catch (UserNotFoundException e) {
                        System.out.println(e.getMessage());
                    }
                }
                case 2 -> {
                    try {
                        UserType type = UserType.SALESPERSON;
                        User salesUser = confirmLoginAndGetUser(scanner, type);
                        loginAsSalesman(scanner, salesUser);
                    } catch (UserNotFoundException e) {
                        System.out.println(e.getMessage());
                    }
                }
                case 3 -> {
                    try {
                        UserType type = UserType.IT_SUPPORT;
                        User supportUser = confirmLoginAndGetUser(scanner, type);
                        loginAsITSupport(scanner, supportUser);
                    } catch (UserNotFoundException e) {
                        System.out.println(e.getMessage());
                    }
                }
                case 9 -> {
                    return;
                }
                default -> System.out.println("Unavailable option");
            }
        }

    }

    public static void printMenu() {
        System.out.println("Choose your function:");
        System.out.println("[1] - Accounting login");
        System.out.println("[2] - Sales login");
        System.out.println("[3] - IT login");
        System.out.println("[9] - Exit");
    }

    public static User confirmLoginAndGetUser(Scanner scanner, UserType type) throws UserNotFoundException {
        System.out.print("Enter your username: ");
        String username = scanner.nextLine();
        System.out.print("Enter your password: ");
        String password = scanner.nextLine();

        return adminService.getUserByType(username, password, type);
    }

    public static void loginAsAccountant(Scanner scanner, User currentUser) {
        System.out.printf("Welcome, %s %s!\n", currentUser.getName(), currentUser.getSurname());
        String input;
        int option = 0;
        while (true) {
            printAccountantMenu();
            try {
                input = scanner.nextLine();
                option = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Wrong input format, try again");
            }
            switch (option) {
                case 1 -> getBalanceForADay(scanner);
                case 2 -> getBalanceForAMonth(scanner);
                case 3 -> getSalesDocumentsByDay(scanner);
                case 4 -> getReturnsDocumentsByDay(scanner);
                case 5 -> getSalesBySellerByMonth(scanner);
                case 6 -> addBusinessPartner(scanner);
                case 7 -> getClientIDByClientName(scanner);
                case 8 -> deleteClient(scanner);
                case 9 -> {
                    return;
                }
                default -> System.out.println("Unavailable option");
            }
        }
    }

    public static void printAccountantMenu() {
        System.out.println("Accountant Operations:");
        System.out.println("**********************");
        System.out.println("[1] - Balance for a day");
        System.out.println("[2] - Balance for a month");
        System.out.println("[3] - Sales documents by day");
        System.out.println("[4] - Returns documents by day");
        System.out.println("[5] - Month sales by seller");
        System.out.println("[6] - Add new client");
        System.out.println("[7] - Get clientID by client name");
        System.out.println("[8] - Delete client from database");
        System.out.println("[9] - Return to previous menu");
    }

    private static void getBalanceForADay(Scanner scanner) {
        System.out.println("Enter the day for balance - format yyyy-mm-dd");
        while (true) {
            try {
                LocalDate date = getLocalDate(scanner);
                double cashBalance = accountingService.getDaysBalance(date);
                System.out.println("*******************");
                System.out.printf("Balance for %s is: %.2f\n", date, cashBalance);
                System.out.println("*******************");
                return;
            } catch (NumberFormatException e) {
                System.out.println("Wrong format, try again");
            } catch (DateTimeException e) {
                System.out.println("Wrong day/month/year entered, try again");
            }
        }

    }

    private static void getBalanceForAMonth(Scanner scanner) {
        System.out.println("Enter the month for balance - format yyyy-mm");
        while (true) {
            try {
                LocalDate date = getLocalDate(scanner);
                double cashBalance = accountingService.getMonthBalance(date);
                System.out.println("*******************");
                System.out.printf("Balance for %d %s is: %.2f\n", date.getYear(), date.getMonth(), cashBalance);
                System.out.println("*******************");
                return;
            } catch (NumberFormatException e) {
                System.out.println("Wrong format, try again");
            } catch (DateTimeException e) {
                System.out.println("Wrong day/month/year entered, try again");
            }
        }
    }

    private static void getSalesDocumentsByDay(Scanner scanner) {

        System.out.println("Enter the day for sales documents balance - format yyyy-mm-dd");
        while (true) {
            try {
                LocalDate date = getLocalDate(scanner);
                ArrayList<CashRecord> salesForDay = accountingService.getDailySaleDocuments(date, CashOperation.DAILY_INCOME);
                System.out.printf("Sales documents for %s day is:\n", date);
                for (CashRecord cashRecord : salesForDay) {
                    SalesCashRecord salesCashRecord = (SalesCashRecord) cashRecord;
                    System.out.println("*******************");
                    System.out.printf("Sales document id: %s, amount = %.2f\n",
                            salesCashRecord.getRecordID(), salesCashRecord.getAmount());
                    System.out.println("*******************");
                }
                return;
            } catch (NumberFormatException e) {
                System.out.println("Wrong format, try again");
            } catch (DateTimeException e) {
                System.out.println("Wrong day/month/year entered, try again");
            }
        }
    }

    private static void getReturnsDocumentsByDay(Scanner scanner) {
        System.out.println("Enter the day for returns documents balance - format yyyy-mm-dd");
        while (true) {
            try {
                LocalDate date = getLocalDate(scanner);
                ArrayList<CashRecord> salesForDay = accountingService.getDailySaleDocuments(date, CashOperation.DAILY_EXPENSE);
                System.out.printf("Return documents for %s is:\n", date);
                for (CashRecord cashRecord : salesForDay) {
                    ReturnCashRecord returnCashRecord = (ReturnCashRecord) cashRecord;
                    System.out.println("*******************");
                    System.out.printf("Return document id: %s, amount = %.2f\n",
                            returnCashRecord.getRecordID(), returnCashRecord.getAmount());
                    System.out.println("*******************");
                }
                return;
            } catch (NumberFormatException e) {
                System.out.println("Wrong format, try again");
            } catch (DateTimeException e) {
                System.out.println("Wrong day/month/year entered, try again");
            }
        }
    }

    private static void getSalesBySellerByMonth(Scanner scanner) {
        System.out.print("Enter salesman username to get report:");
        String sellerUsername = scanner.nextLine();
        while (true) {
            try {
                System.out.print("Enter month for sales report - format yyyy-mm");
                LocalDate date = getLocalDate(scanner);
                ArrayList<SalesCashRecord> userSales = accountingService.getSalesReportBySalesperson(sellerUsername, date.getYear(), date.getMonthValue());
                if (userSales.size() == 0) {
                    System.out.printf("No sales data found for %s user for %s %s\n",
                            sellerUsername, date.getYear(), date.getMonth());
                    return;
                }
                for (SalesCashRecord record : userSales) {
                    System.out.printf("Date: %s, document id: %s, amount: %.2f\n",
                            record.getDate(), record.getRecordID(), record.getAmount());
                }
                System.out.println("*******************");
                System.out.printf("Total %s sales for %s %s is: %.2f\n",
                        sellerUsername, date.getYear(), date.getMonth(), accountingService.getTotalSalesByReport(userSales));
                System.out.println("*******************");
                return;
            } catch (NumberFormatException e) {
                System.out.println("Wrong format, try again");
            } catch (DateTimeException e) {
                System.out.println("Wrong day/month/year entered, try again");
            }
        }
    }

    private static void addBusinessPartner(Scanner scanner) {
        System.out.print("Enter client name: ");
        String name = scanner.nextLine();
        System.out.print("Enter client businessID: ");
        String businessId = scanner.nextLine();
        System.out.print("Enter client street address: ");
        String streetAddress = scanner.nextLine();
        System.out.print("Enter client city: ");
        String city = scanner.nextLine();
        System.out.print("Enter client country: ");
        String country = scanner.nextLine();

        BusinessPartner businessPartner = new BusinessPartner(name, businessId, streetAddress, city, country);
        try {
            businessService.addNewBusinessPartner(businessPartner);
            System.out.printf("New partner %s added successfully!\n", businessPartner.getPartnerName());
        } catch (IOException e) {
            System.out.printf("Partner %s was not added\n", name);
        }
    }

    private static void getClientIDByClientName(Scanner scanner) {
        System.out.print("Enter client name: ");
        String name = scanner.nextLine();
        try {
            BusinessPartner partner = businessService.getPartnerByName(name);
            System.out.printf("Client %s id is: %s\n", name, partner.getBusinessID());
        } catch (PartnerDoesNotExistExeption e) {
            System.out.println(e.getMessage());
        }

    }

    private static void deleteClient(Scanner scanner) {
        System.out.print("Enter name of client you want to delete: ");
        String name = scanner.nextLine();
        try {
            BusinessPartner partner = businessService.getPartnerByName(name);
            businessService.deletePartner(partner);
            System.out.printf("Partner %s successfully deleted\n", partner.getPartnerName());
        } catch (PartnerDoesNotExistExeption e) {
            System.out.println(e.getMessage());
        }
    }

    public static void loginAsSalesman(Scanner scanner, User currentUser) {
        System.out.printf("Welcome, %s %s!\n", currentUser.getName(), currentUser.getSurname());
        printSalesmanMenu();
        //TODO implement method
    }

    public static void printSalesmanMenu() {
        System.out.println("Sales Operations:");
        System.out.println("**********************");
        System.out.println("[1] - Sale operation");
        System.out.println("[2] - Find sales document by document Nr.");
        System.out.println("[3] - Return operation");
        System.out.println("[4] - Find return document by document Nr. ");
        System.out.println("[9] - Return to previous menu");
    }

    private static void createNewSalesOperation(Scanner scanner) {
        //TODO implement method
    }

    private static void findSalesDocumentByID(Scanner scanner) {
        //TODO implement method
    }

    private static void createNewReturnOperation(Scanner scanner) {
        //TODO implement method
    }

    private static void findReturnDocumentByID(Scanner scanner) {
        //TODO implement method
    }

    public static void loginAsITSupport(Scanner scanner, User currentUser) {
        System.out.printf("Welcome, %s %s!\n", currentUser.getName(), currentUser.getSurname());
        String input;
        int option = 0;
        while (true) {
            printITSupportMenu();
            try {
                input = scanner.nextLine();
                option = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Wrong input format, try again");
            }
            switch (option) {
                case 1 -> printListOfUsers();
                case 2 -> registerNewUser(scanner);
                case 3 -> removeUserByUsername(scanner, currentUser);
                case 9 -> {
                    return;
                }
                default -> System.out.println("Unavailable option");
            }
        }
    }

    public static void printITSupportMenu() {
        System.out.println("IT Support Operations:");
        System.out.println("**********************");
        System.out.println("[1] - Print list of users");
        System.out.println("[2] - Register new user");
        System.out.println("[3] - Remove user");
        System.out.println("[9] - Return to previous menu");
    }

    private static LocalDate getLocalDate(Scanner scanner) throws DateTimeException, NumberFormatException {
        String input;
        input = scanner.nextLine();

        int year = Integer.parseInt(input.substring(0, 4));
        int month = Integer.parseInt(input.substring(5, 7));
        int day = 1;
        if (input.length() == 10) {
            day = Integer.parseInt(input.substring(8));
        }

        return LocalDate.of(year, month, day);
    }

    private static void printListOfUsers() {
        ArrayList<User> users = adminService.getAllUsers();
        System.out.println("********************");
        for (User user : users) {
            System.out.printf("Username: %s, User: %s %s, User Role: %s\n",
                    user.getUsername(), user.getName(), user.getSurname(), user.getUserType());
            System.out.println("********************");
        }
    }

    private static void registerNewUser(Scanner scanner) {
        System.out.println("Enter new user data:");
        String username = validateUsername(scanner);
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        System.out.print("Enter name of user: ");
        String name = scanner.nextLine();
        System.out.print("Enter surname of user: ");
        String surname = scanner.nextLine();
        UserType type = getUserType(scanner);
        try {
            adminService.addNewUser(new User(name, surname, username, password, type));
            System.out.printf("User %s successfully added\n", username);
        } catch (IOException e) {
            System.out.printf("User %s cannot be added\n", username);
        }
    }

    private static String validateUsername(Scanner scanner) {
        while (true) {
            System.out.print("Enter username: ");
            String username = scanner.nextLine();
            if (adminService.isUsernameUnique(username)) {
                return username;
            } else {
                System.out.println("Username is already in database, try again");
            }
        }
    }

    private static UserType getUserType(Scanner scanner) {
        String input;
        int option = 0;
        while (true) {
            System.out.println("Choose new user role: ");
            System.out.println("[1] - ACCOUNTING");
            System.out.println("[2] - SALESPERSON");
            System.out.println("[3] - IT SUPPORT");
            try {
                input = scanner.nextLine();
                option = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Wrong input format, try again");
            }
            switch (option) {
                case 1 -> {
                    return UserType.ACCOUNTING;
                }
                case 2 -> {
                    return UserType.SALESPERSON;
                }
                case 3 -> {
                    return UserType.IT_SUPPORT;
                }
                default -> System.out.println("Unavailable option");
            }
        }
    }

    private static void removeUserByUsername(Scanner scanner, User currentUser) {
        System.out.println("Enter username of user to delete: ");
        String username = scanner.nextLine();
        if (currentUser.getUsername().equals(username)) {
            System.out.println("ATTENTION!!! You cannot delete yourself!!!");
            return;
        }

        try {
            adminService.removeUser(username);
            System.out.printf("User %s has been deleted\n", username);
        } catch (UserNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println("Database file was not found");
        }
    }
}
