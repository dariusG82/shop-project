package dariusG82;

import dariusG82.classes.accounting.DailyReport;
import dariusG82.classes.services.*;
import dariusG82.classes.accounting.finance.*;
import dariusG82.classes.accounting.orders.*;
import dariusG82.classes.users.User;
import dariusG82.classes.users.UserType;
import dariusG82.classes.custom_exeptions.*;
import dariusG82.classes.partners.Client;
import dariusG82.classes.warehouse.Item;
import dariusG82.classes.warehouse.ReturnedItem;

import java.io.IOException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

import static dariusG82.classes.services.Service.*;

public class Main {
    public static AccountingService accountingService = new AccountingService();
    public static AdminService adminService = new AdminService();
    public static BusinessService businessService = new BusinessService();
    public static WarehouseService warehouseService = new WarehouseService();

    public static void main(String[] args) {
        System.out.println("Welcome to OfficeGoodsShop");
        updateDailySalesJournal();

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
                case 0 -> {
                    return;
                }
                default -> System.out.println("Unavailable option");
            }
        }
    }

    private static void updateDailySalesJournal() {
        LocalDate localDate = LocalDate.now();
        try {
            LocalDate lastLoginDate = adminService.getLoginDate();
            if (!lastLoginDate.equals(localDate)) {
                adminService.updateCurrentDateInDataString(localDate);
                accountingService.countIncomeAndExpensesByDays();
            }
        } catch (WrongDataPathExeption | IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void printMenu() {
        System.out.println("Choose your function:");
        System.out.println("[1] - Accounting login");
        System.out.println("[2] - Sales login");
        System.out.println("[3] - IT login");
        System.out.println("[0] - Exit");
    }

    private static User confirmLoginAndGetUser(Scanner scanner, UserType type) throws UserNotFoundException {
        System.out.print("Enter your username: ");
        String username = scanner.nextLine();
        System.out.print("Enter your password: ");
        String password = scanner.nextLine();

        return adminService.getUserByType(username, password, type);
    }

    private static void loginAsAccountant(Scanner scanner, User currentUser) {
        System.out.printf("Welcome, %s %s!\n", currentUser.getName(), currentUser.getSurname());
        while (true) {
            printAccountantMenu();
            switch (getChoiceFromScanner(scanner)) {
                case 1 -> startFinancialOperations(scanner);
                case 2 -> startClientsOperations(scanner);
                case 0 -> {
                    return;
                }
                default -> System.out.println("Unavailable option");
            }
        }
    }

    private static void printAccountantMenu() {
        System.out.println("Accountant Operations:");
        System.out.println("**********************");
        System.out.println("[1] - Financial Operations");
        System.out.println("[2] - Clients Operations");
        System.out.println("[0] - Return to previous menu");
    }

    private static void startFinancialOperations(Scanner scanner) {
        while (true) {
            printFinancialMenu();
            switch (getChoiceFromScanner(scanner)) {
                case 1 -> getDailySalesReturnsBalance();
                case 2 -> getBalanceForADay(scanner);
                case 3 -> getBalanceForAMonth(scanner);
                case 4 -> getSalesDocumentsByDay(scanner);
                case 5 -> getReturnsDocumentsByDay(scanner);
                case 6 -> getSalesBySellerByMonth(scanner);
                case 0 -> {
                    return;
                }
                default -> System.out.println("Unavailable option");
            }
        }
    }

    private static void printFinancialMenu() {
        System.out.println("Balance Operations:");
        System.out.println("**********************");
        System.out.println("[1] - Get daily sales/returns balance reports");
        System.out.println("[2] - Balance for a day");
        System.out.println("[3] - Balance for a month");
        System.out.println("[4] - Sales documents by day");
        System.out.println("[5] - Returns documents by day");
        System.out.println("[6] - Month sales by seller");
        System.out.println("[0] - Return to previous menu");
    }

    private static void startClientsOperations(Scanner scanner) {
        while (true) {
            printClientsServiceMenu();
            switch (getChoiceFromScanner(scanner)) {
                case 1 -> addNewClient(scanner);
                case 2 -> getClientIDByClientName(scanner);
                case 3 -> deleteClient(scanner);
                case 0 -> {
                    return;
                }
                default -> System.out.println("Unavailable option");
            }
        }
    }

    private static void printClientsServiceMenu() {
        System.out.println("Clients Master Operations:");
        System.out.println("**********************");
        System.out.println("[1] - Add new client");
        System.out.println("[2] - Get clientID by client name");
        System.out.println("[3] - Delete client from database");
        System.out.println("[0] - Return to previous menu");
    }

    private static void getDailySalesReturnsBalance() {
        ArrayList<DailyReport> reports = accountingService.getDataService().getDailyReports();

        if (reports == null) {
            System.out.println("There is no daily reports");
            return;
        }
        double totalBalance = 0.0;
        System.out.println("**********************");
        for (DailyReport report : reports) {
            System.out.printf("Date: %s || Daily income = %.2f, Daily expenses = %.2f, || Daily balance = %.2f\n",
                    report.getDate(), report.getDailyIncome(), report.getDailyExpenses(), report.getDailyBalance());
            totalBalance += report.getDailyBalance();
        }
        System.out.println("**********************");
        System.out.printf("Total sales/returns balance = %.2f\n", totalBalance);
    }

    private static void getBalanceForADay(Scanner scanner) {
        System.out.print("Enter the day for balance - format yyyy-mm-dd: ");
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
        System.out.print("Enter the month for balance - format yyyy-mm: ");
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
        System.out.print("Enter the day for sales documents balance - format yyyy-mm-dd: ");
        while (true) {
            try {
                LocalDate date = getLocalDate(scanner);
                ArrayList<CashRecord> salesForDay = accountingService.getDailySaleDocuments(date, CashOperation.DAILY_INCOME);
                if (salesForDay.size() == 0) {
                    System.out.printf("There is no sales document for %s day\n", date);
                    return;
                }
                System.out.printf("Sales documents for %s day is:\n", date);
                System.out.println("*******************");
                for (CashRecord cashRecord : salesForDay) {
                    System.out.printf("Sales document id: %s, amount = %.2f\n",
                            cashRecord.getRecordID(), cashRecord.getAmount());
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
        System.out.print("Enter the day for returns documents balance - format yyyy-mm-dd: ");
        while (true) {
            try {
                LocalDate date = getLocalDate(scanner);
                ArrayList<CashRecord> salesForDay = accountingService.getDailySaleDocuments(date, CashOperation.DAILY_EXPENSE);
                System.out.printf("Return documents for %s is:\n", date);
                System.out.println("*******************");
                for (CashRecord cashRecord : salesForDay) {
                    System.out.printf("Return document id: %s, amount = %.2f\n",
                            cashRecord.getRecordID(), cashRecord.getAmount());
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
        System.out.print("Enter salesman username to get report: ");
        String sellerUsername = scanner.nextLine();
        User user = adminService.getUserByUsername(sellerUsername);
        if(user == null){
            System.out.printf("User with username %s does not exist\n", sellerUsername);
            return;
        }
        while (true) {
            try {
                System.out.print("Enter month for sales report - format yyyy-mm: ");
                LocalDate date = getLocalDate(scanner);
                ArrayList<CashRecord> userSales = accountingService.getSalesReportBySalesperson(sellerUsername, date.getYear(), date.getMonthValue());
                if (userSales.size() == 0) {
                    System.out.printf("No sales data found for %s user for %s %s\n",
                            sellerUsername, date.getYear(), date.getMonth());
                    return;
                }
                for (CashRecord record : userSales) {
                    System.out.printf("Date: %s, document id: %s, amount: %.2f\n",
                            record.getDate(), record.getRecordID(), record.getAmount());
                }
                System.out.println("*******************");
                System.out.printf("Total %s %s sales for %s %s is: %.2f\n",
                        user.getName(), user.getSurname(), date.getYear(), date.getMonth(), accountingService.getTotalSalesByReport(userSales));
                System.out.println("*******************");
                return;
            } catch (NumberFormatException e) {
                System.out.println("Wrong format, try again");
            } catch (DateTimeException e) {
                System.out.println("Wrong day/month/year entered, try again");
            }
        }
    }

    private static void addNewClient(Scanner scanner) {
        Client client = getNewClientInfo(scanner);

        try {
            businessService.addNewClientToDatabase(client);
            System.out.printf("New client %s added successfully!\n", client.getClientName());
        } catch (WrongDataPathExeption e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.printf("Client %s was not added\n", client.getClientName());
        }
    }

    private static Client getNewClientInfo(Scanner scanner) {
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

        return new Client(name, businessId, streetAddress, city, country);
    }

    private static void getClientIDByClientName(Scanner scanner) {
        System.out.print("Enter client name: ");
        String name = scanner.nextLine();
        try {
            Client partner = businessService.getClientName(name);
            System.out.printf("Client %s id is: %s\n", name, partner.getBusinessID());
        } catch (WrongDataPathExeption | ClientDoesNotExistExeption e) {
            System.out.println(e.getMessage());
        }
    }

    private static void deleteClient(Scanner scanner) {
        System.out.print("Enter name of client you want to delete: ");
        String name = scanner.nextLine();
        try {
            Client client = businessService.getClientName(name);
            businessService.deleteClientFromDatabase(client);
            System.out.printf("Client %s successfully deleted\n", client.getClientName());
        } catch (WrongDataPathExeption | ClientDoesNotExistExeption e) {
            System.out.println(e.getMessage());
        }
    }

    private static void loginAsSalesman(Scanner scanner, User currentUser) {
        System.out.printf("Welcome, %s %s!\n", currentUser.getName(), currentUser.getSurname());
        while (true) {
            printSalesmanMenu();
            switch (getChoiceFromScanner(scanner)) {
                case 1 -> createNewSalesOperation(scanner, currentUser);
                case 2 -> findSalesDocumentByID(scanner);
                case 3 -> createNewReturnOperation(scanner, currentUser);
                case 4 -> findReturnDocumentByID(scanner);
                case 5 -> createPurchaseOrderToWarehouse(scanner);
                case 6 -> receiveGoodsToWarehouse(scanner);
                case 7 -> showWarehouseStock();
                case 0 -> {
                    return;
                }
                default -> System.out.println("Unavailable option");
            }
        }
    }

    private static void printSalesmanMenu() {
        System.out.println("Sales Operations:");
        System.out.println("**********************");
        System.out.println("[1] - Sale operation");
        System.out.println("[2] - Find sales document by document Nr.");
        System.out.println("[3] - Return operation");
        System.out.println("[4] - Find return document by document Nr. ");
        System.out.println("[5] - Create goods order to shop");
        System.out.println("[6] - Receive goods to warehouse by order Nr.");
        System.out.println("[7] - Print warehouse stock");
        System.out.println("[0] - Return to previous menu");
    }

    private static void createNewSalesOperation(Scanner scanner, User currentUser) {
        System.out.println("Creating new Sales document");
        System.out.println("Enter client name: ");
        String clientName = scanner.nextLine();
        if (!businessService.isClientInDatabase(clientName)) {
            System.out.printf("Client %s is not in database, ask your accountant to add new client\n", clientName);
            return;
        }
        try {
            int salesOrderID = accountingService.getNewDocumentNumber(SALES_ORDER_NR_INFO);
            SalesOrder newOrder = new SalesOrder(salesOrderID);
            while (true) {
                System.out.println("[1] - Add item to sales order / [2] - Finish sales order");

                switch (getChoiceFromScanner(scanner)) {
                    case 1 -> finishSalesOrder(scanner, currentUser, salesOrderID, newOrder);
                    case 2 -> {
                        accountingService.updateSalesOrderLine(newOrder);
                        return;
                    }
                    default -> System.out.println("Wrong choice, choose again");
                }
            }
        } catch (WrongDataPathExeption | IOException e) {
            System.out.println("Cannot create new order / data file doesn't exist");
        }
    }

    private static void finishSalesOrder(Scanner scanner, User currentUser, int salesOrderID, SalesOrder newOrder) {
        Item item = getItemByName(scanner);
        if (item.getCurrentQuantity() == 0) {
            System.out.println("Item is out of stock");
            return;
        }
        int quantity = getItemQuantity(scanner, item);
        try {
            SalesOrderLine salesOrderLine = new SalesOrderLine(salesOrderID, item.getItemName(), quantity, item.getSalePrice(), currentUser.getName());
            updateSalesLine(currentUser, salesOrderID, item, quantity, salesOrderLine);
            newOrder.addSalesOrderLineToOrder(salesOrderLine);
        } catch (NegativeBalanceException | ItemIsNotInWarehouseExeption e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println("Sales line update failed, cannot update stock / balance");
        } catch (WrongDataPathExeption e) {
            System.out.println("Balance is not updated, cannot find data file");
        }
    }

    private static void findSalesDocumentByID(Scanner scanner) {
        Order order = getDocumentFromAccounting(scanner);
        if (order == null) {
            System.out.println("Sales order cannot be found");
            return;
        }
        if (order instanceof ReturnOrder) {
            System.out.println("Sales order must begin with SF ");
            return;
        }
        if (order instanceof SalesOrder salesOrder) {
            System.out.println("*****************************");
            System.out.printf("Sales order Nr: %s\n", salesOrder.getOrderID());
            for (SalesOrderLine salesOrderLine : salesOrder.getOrderItems()) {
                double lineAmount = salesOrderLine.getLineQuantity() * salesOrderLine.getUnitPrice();
                System.out.printf("Item: %s, sold quantity: %s, sold unitPrice: %.2f, total line amount %.2f\n",
                        salesOrderLine.getItemName(), salesOrderLine.getLineQuantity(), salesOrderLine.getUnitPrice(), lineAmount);
            }
            System.out.println("**************");
            System.out.printf("Total sales order amount: %.2f\n", salesOrder.getTotalOrderAmount());
            System.out.println("*****************************");
        }

    }

    private static void createNewReturnOperation(Scanner scanner, User currentUser) {
        System.out.println("Creating new Returns document");
        Order order = getDocumentFromAccounting(scanner);

        if (order == null) {
            System.out.println("Sales order cannot be found");
            return;
        }
        if (order instanceof ReturnOrder) {
            System.out.println("Sales order must begin with SF ");
            return;
        }
        if (order instanceof SalesOrder salesOrder) {
            try {
                int returnOrderID = accountingService.getNewDocumentNumber(RETURN_ORDER_NR_INFO);
                ReturnOrder returnOrder = new ReturnOrder(returnOrderID);
                while (true) {
                    System.out.println("[1] - Add item to return order / [2] - Finish return order");
                    switch (getChoiceFromScanner(scanner)) {
                        case 1 -> updateReturnOrder(scanner, currentUser, salesOrder, returnOrderID, returnOrder);
                        case 2 -> {
                            accountingService.updateReturnOrderLines(returnOrder);
                            return;
                        }
                        default -> System.out.println("Wrong choice, choose again");
                    }
                }
            } catch (WrongDataPathExeption | IOException e) {
                System.out.println("Cannot create new return order / data file doesn't exist");
            }
        }
    }

    private static void updateReturnOrder(Scanner scanner, User currentUser, SalesOrder salesOrder, int returnOrderID, ReturnOrder returnOrder) {
        System.out.print("Enter sold item name: ");
        String itemName = scanner.nextLine();
        ReturnedItem returnedItem = accountingService.getSoldItemByName(salesOrder, itemName);

        if (returnedItem == null) {
            System.out.printf("Cannot find sold item by name: %s\n", itemName);
            return;
        }
        int quantity = getItemQuantity(scanner, returnedItem);

        try {
            ReturnOrderLine returnOrderLine = new ReturnOrderLine(returnOrderID, itemName, quantity, returnedItem.getSalePrice(), currentUser.getUsername());
            makeGoodsReturn(salesOrder, returnOrderLine, returnedItem, currentUser);
            returnOrder.addSalesOrderLineToOrder(returnOrderLine);
        } catch (NegativeBalanceException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println("Return line update failed, cannot update stock / balance");
        } catch (WrongDataPathExeption e) {
            System.out.println("Balance is not updated, cannot find data file");
        } catch (ItemIsNotInWarehouseExeption e) {
            System.out.println("Can't return item, warehouse data file is empty");
        }
    }

    private static void findReturnDocumentByID(Scanner scanner) {
        Order order = getDocumentFromAccounting(scanner);
        if (order == null) {
            System.out.println("Return order cannot be found");
            return;
        }
        if (order instanceof SalesOrder) {
            System.out.println("Return order number must begin with RE ");
            return;
        }
        if (order instanceof ReturnOrder returnOrder) {
            System.out.println("*****************************");
            System.out.printf("Return order Nr: %s\n", returnOrder.getOrderID());
            for (ReturnOrderLine returnOrderLine : returnOrder.getOrderItems()) {
                double lineAmount = returnOrderLine.getLineQuantity() * returnOrderLine.getUnitPrice();
                System.out.printf("Item: %s, returned quantity: %s, returned unitPrice: %.2f, total line amount %.2f\n",
                        returnOrderLine.getItemName(), returnOrderLine.getLineQuantity(), returnOrderLine.getUnitPrice(), lineAmount);
            }
            System.out.println("**************");
            System.out.printf("Total return order amount: %.2f\n", returnOrder.getTotalOrderAmount());
            System.out.println("*****************************");
        }
    }

    private static void createPurchaseOrderToWarehouse(Scanner scanner) {
        try {
            int purchaseNr = warehouseService.getNewPurchaseOrderNumber();
            System.out.printf("Creating purchase order Nr.: %d\n", purchaseNr);
            System.out.println("***********************");
            PurchaseOrder purchaseOrder = new PurchaseOrder(purchaseNr);
            while (true) {
                System.out.println("[1] - Add item to purchase order / [2] - Finish order");
                switch (getChoiceFromScanner(scanner)) {
                    case 1 -> {
                        System.out.print("Enter item name: ");
                        String itemName = scanner.nextLine();
                        double purchasePrice = getPurchasePriceFromScanner(scanner);
                        int purchaseQuantity = getQuantityFromScanner(scanner);
                        Item item = new Item(itemName, purchasePrice, purchaseQuantity);
                        PurchaseOrderLine purchaseOrderLine = new PurchaseOrderLine(purchaseNr, item);
                        try {
                            updatePurchaseOrder(purchaseOrder, purchaseOrderLine);
                        } catch (NegativeBalanceException e) {
                            System.out.println(e.getMessage());
                        } catch (IOException e) {
                            System.out.printf("Item %s was not added, wrong data file location\n", itemName);
                        }
                    }
                    case 2 -> {
                        System.out.printf("Total purchase order cash amount = %.2f\n",
                                purchaseOrder.getTotalOrderAmount());
                        return;
                    }
                    default -> System.out.println("Wrong choice, choose again");
                }
            }
        } catch (IOException | WrongDataPathExeption e) {
            System.out.println(e.getMessage());
        }

    }

    private static void updatePurchaseOrder(PurchaseOrder purchaseOrder, PurchaseOrderLine purchaseOrderLine) throws WrongDataPathExeption, IOException, NegativeBalanceException {
        accountingService.updateBalance(purchaseOrderLine);
        warehouseService.getDataService().addItemToPurchaseOrder(purchaseOrderLine);
        purchaseOrder.addPurchaseOrderLinesToOrder(purchaseOrderLine);
        System.out.printf("Item %s was successfully added to order %d\n", purchaseOrderLine.getItemName(), purchaseOrder.getOrderID());
    }

    private static void receiveGoodsToWarehouse(Scanner scanner) {
        int purchaseNr = 0;
        do {
            System.out.print("Enter goods purchase order nr: ");
            String input = scanner.nextLine();
            try {
                purchaseNr = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Wrong input, try again");
            }
            if (purchaseNr <= 0) {
                System.out.println("Order number cannot be 0");
            } else {
                break;
            }
        } while (true);
        try {
            warehouseService.receiveGoods(purchaseNr);
            System.out.println("Goods successfully added to warehouse stock");
            System.out.println("**********************");
        } catch (PurchaseOrderDoesNotExistExeption | IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void showWarehouseStock() {
        ArrayList<Item> stock = warehouseService.getDataService().getAllWarehouseItems();

        if (stock == null) {
            System.out.println("Warehouse is empty");
            return;
        }

        for (Item item : stock) {
            System.out.println("**********");
            System.out.printf("Item: %s, total stock: %d\n", item.getItemName(), item.getCurrentQuantity());
        }
        System.out.println("**********");
    }

    private static Order getDocumentFromAccounting(Scanner scanner) {
        System.out.println("Enter sales/return document number: ");
        String requestedID = scanner.nextLine();

        if (requestedID.startsWith("SF ") || requestedID.startsWith("RE ")) {
            return accountingService.getDocumentByID(requestedID);
        } else {
            return null;
        }
    }

    private static void updateSalesLine(User currentUser, int salesOrderID, Item item, int quantity, SalesOrderLine salesOrderLine) throws WrongDataPathExeption, IOException, NegativeBalanceException, ItemIsNotInWarehouseExeption {
        accountingService.updateBalance(salesOrderLine);
        warehouseService.updateWarehouseStock(item, -quantity);
        CashRecord cashRecord = new SalesCashRecord(salesOrderID, LocalDate.now(), item.getSalePrice() * quantity, currentUser.getUsername());
        accountingService.updateCashRecords(cashRecord);
    }

    private static void makeGoodsReturn(SalesOrder salesOrder, ReturnOrderLine returnOrderLine, ReturnedItem returnedItem, User currentUser) throws NegativeBalanceException, IOException, WrongDataPathExeption, ItemIsNotInWarehouseExeption {
        accountingService.updateBalance(returnOrderLine);
        warehouseService.updateWarehouseStock(returnedItem, returnOrderLine.lineQuantity);
        accountingService.refreshSalesOrdersQuantity(salesOrder, returnedItem, returnOrderLine.lineQuantity);
        CashRecord cashRecord = new ReturnCashRecord(returnOrderLine.getOrderNr(), LocalDate.now(), returnedItem.getSalePrice() * returnOrderLine.getLineQuantity(), currentUser.getUsername());
        accountingService.updateCashRecords(cashRecord);
    }

    private static double getPurchasePriceFromScanner(Scanner scanner) {
        do {
            try {
                System.out.print("Enter item purchase price: ");
                String purchasePriceString = scanner.nextLine();
                double purchasePrice = Double.parseDouble(purchasePriceString);
                if (purchasePrice > 0) {
                    return purchasePrice;
                }
            } catch (NumberFormatException e) {
                System.out.println("Wrong input, try again");
            }
        } while (true);
    }

    private static int getQuantityFromScanner(Scanner scanner) {
        do {
            try {
                System.out.print("Enter item purchase quantity: ");
                String purchaseQuantityString = scanner.nextLine();
                int quantity = Integer.parseInt(purchaseQuantityString);
                if (quantity > 0) {
                    return quantity;
                }
            } catch (NumberFormatException e) {
                System.out.println("Wrong input, try again");
            }
        } while (true);
    }

    private static Item getItemByName(Scanner scanner) {
        while (true) {
            System.out.print("Enter sold item name: ");
            String itemName = scanner.nextLine();

            try {
                return warehouseService.getItemFromWarehouse(itemName);
            } catch (ItemIsNotInWarehouseExeption e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private static void loginAsITSupport(Scanner scanner, User currentUser) {
        System.out.printf("Welcome, %s %s!\n", currentUser.getName(), currentUser.getSurname());
        while (true) {
            printITSupportMenu();
            switch (getChoiceFromScanner(scanner)) {
                case 1 -> printListOfUsers();
                case 2 -> registerNewUser(scanner);
                case 3 -> removeUserByUsername(scanner, currentUser);
                case 0 -> {
                    return;
                }
                default -> System.out.println("Unavailable option");
            }
        }
    }

    private static void printITSupportMenu() {
        System.out.println("IT Support Operations:");
        System.out.println("**********************");
        System.out.println("[1] - Print list of users");
        System.out.println("[2] - Register new user");
        System.out.println("[3] - Remove user");
        System.out.println("[0] - Return to previous menu");
    }

    private static void printListOfUsers() {
        ArrayList<User> users = adminService.getDataService().getAllUsers();
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

    private static int getItemQuantity(Scanner scanner, Item item) {
        while (true) {
            System.out.print("Enter quantity: ");
            int quantity = getChoiceFromScanner(scanner);
            if (quantity == 0) {
                System.out.println("Quantity cannot be 0");
            } else if (quantity > item.getCurrentQuantity()) {
                System.out.println("Not enough items");
            } else {
                return quantity;
            }
        }
    }

    private static int getChoiceFromScanner(Scanner scanner) {
        String input;
        int option = 0;
        try {
            input = scanner.nextLine();
            option = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("Wrong input format, try again");
        }
        return option;
    }
}
