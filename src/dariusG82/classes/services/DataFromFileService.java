package dariusG82.classes.services;

import dariusG82.classes.accounting.DailyReport;
import dariusG82.classes.accounting.finance.CashOperation;
import dariusG82.classes.accounting.finance.CashRecord;
import dariusG82.classes.accounting.orders.OrderLine;
import dariusG82.classes.accounting.orders.PurchaseOrderLine;
import dariusG82.classes.accounting.orders.ReturnOrderLine;
import dariusG82.classes.accounting.orders.SalesOrderLine;
import dariusG82.classes.custom_exeptions.WrongDataPathExeption;
import dariusG82.classes.data.DataManagement;
import dariusG82.classes.partners.Client;
import dariusG82.classes.users.User;
import dariusG82.classes.users.UserType;
import dariusG82.classes.warehouse.Item;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

public class DataFromFileService implements DataManagement {

    public final String PURCHASE_ORDERS_PATH = "src/dariusG82/classes/data/orders/purchaseOrderList.txt";
    public final String RETURN_ORDERS_PATH = "src/dariusG82/classes/data/orders/returnOrderList";
    public final String SALES_ORDERS_PATH = "src/dariusG82/classes/data/orders/salesOrderList.txt";

    public final String CLIENT_PATH = "src/dariusG82/classes/data/clients.txt";
    public final String DAILY_CASH_JOURNALS_PATH = "src/dariusG82/classes/data/dailyCashJournals.txt";
    public final String ALL_CASH_RECORDS_PATH = "src/dariusG82/classes/data/allCashRecords.txt";
    private final String SYSTEM_DATA_PATH = "src/dariusG82/classes/data/systemData.txt";
    public final String USERS_DATA_PATH = "src/dariusG82/classes/data/users.txt";
    public final String WAREHOUSE_DATA_PATH = "src/dariusG82/classes/data/warehouse.txt";

    public ArrayList<String> getDataStrings() {
        try {
            Scanner scanner = new Scanner(new File(SYSTEM_DATA_PATH));
            ArrayList<String> dataList = new ArrayList<>();

            while (scanner.hasNext()) {
                String data = scanner.nextLine();
                scanner.nextLine();
                dataList.add(data);
            }

            return dataList;
        } catch (FileNotFoundException e) {
            return null;
        }
    }

    public void updateDataStrings(ArrayList<String> updatedDataStrings) throws IOException {
        PrintWriter printWriter = new PrintWriter(new FileWriter(SYSTEM_DATA_PATH));

        for (String dataString : updatedDataStrings) {
            printWriter.println(dataString);
            printWriter.println();
        }

        printWriter.close();
    }

    public void rewriteDailyReports(ArrayList<DailyReport> dailyReports) throws IOException {
        PrintWriter printWriter = new PrintWriter(new FileWriter(DAILY_CASH_JOURNALS_PATH));

        for (DailyReport report : dailyReports) {
            printWriter.println(report.getDate());
            printWriter.println(report.getDailyIncome());
            printWriter.println(report.getDailyExpenses());
            printWriter.println(report.getDailyBalance());
            printWriter.println();
        }

        printWriter.close();
    }

    public ArrayList<OrderLine> getAllOrderLines(String path) {
        ArrayList<OrderLine> orderLines = new ArrayList<>();

        try {
            Scanner scanner = new Scanner(new File(path));
            while (scanner.hasNext()) {
                String orderIdString = scanner.nextLine();
                String itemName = scanner.nextLine();
                String quantityString = scanner.nextLine();
                String unitPriceString = scanner.nextLine();
                String salesmanID = scanner.nextLine();
                scanner.nextLine();

                int id = Integer.parseInt(orderIdString.substring(orderIdString.indexOf(" ") + 1));
                int quantity = Integer.parseInt(quantityString);
                double unitPrice = Double.parseDouble(unitPriceString);

                OrderLine orderLine;
                switch (path) {
                    case SALES_ORDERS_PATH ->
                            orderLine = new SalesOrderLine(id, itemName, quantity, unitPrice, salesmanID);
                    case RETURN_ORDERS_PATH ->
                            orderLine = new ReturnOrderLine(id, itemName, quantity, unitPrice, salesmanID);
                    default -> orderLine = new OrderLine(id, itemName, quantity, unitPrice, salesmanID);
                }

                orderLines.add(orderLine);
            }
            return orderLines;
        } catch (FileNotFoundException e) {
            return null;
        }
    }

    public void rewriteOrderLines(ArrayList<OrderLine> orderLines, String path) throws IOException, WrongDataPathExeption {
        PrintWriter printWriter = new PrintWriter(new FileWriter(path));

        String prefix;

        switch (path) {
            case SALES_ORDERS_PATH -> prefix = "SF ";
            case RETURN_ORDERS_PATH -> prefix = "RE ";
            default -> throw new WrongDataPathExeption();
        }

        for (OrderLine orderLine : orderLines) {
            printWriter.println(prefix + orderLine.getOrderNr());
            printWriter.println(orderLine.getItemName());
            printWriter.println(orderLine.getLineQuantity());
            printWriter.println(orderLine.getUnitPrice());
            printWriter.println(orderLine.getSalesmanID());
            printWriter.println();
        }

        printWriter.close();
    }

    public ArrayList<CashRecord> getAllCashRecords() {
        try {
            Scanner scanner = new Scanner(new File(ALL_CASH_RECORDS_PATH));
            ArrayList<CashRecord> cashRecords = new ArrayList<>();

            while (scanner.hasNext()) {
                String id = scanner.nextLine();
                LocalDate operationDate = getOperationDate(scanner.nextLine());
                CashOperation cashOperation = getCashOperation(scanner.nextLine());
                double amount = getAmount(scanner.nextLine());
                String sellerUsername = scanner.nextLine();
                scanner.nextLine();

                if (operationDate != null && cashOperation != null && amount != 0.0) {
                    cashRecords.add(new CashRecord(id, operationDate, cashOperation, amount, sellerUsername));
                }
            }
            return cashRecords;
        } catch (FileNotFoundException e) {
            return null;
        }
    }

    public ArrayList<DailyReport> getDailyReports() {
        try {
            Scanner scanner = new Scanner(new File(DAILY_CASH_JOURNALS_PATH));
            ArrayList<DailyReport> dailyReports = new ArrayList<>();

            while (scanner.hasNext()) {
                LocalDate localDate = LocalDate.parse(scanner.nextLine());
                double incomes = Double.parseDouble(scanner.nextLine());
                double expenses = Double.parseDouble(scanner.nextLine());
                double balance = Double.parseDouble(scanner.nextLine());
                scanner.nextLine();

                DailyReport report = new DailyReport(localDate, incomes, expenses, balance);
                dailyReports.add(report);

            }
            return dailyReports;
        } catch (FileNotFoundException e) {
            return null;
        }
    }

    private double getAmount(String input) {
        try {
            return Double.parseDouble(input);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    private CashOperation getCashOperation(String input) {
        try {
            return CashOperation.valueOf(input);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private LocalDate getOperationDate(String input) {
        try {
            int year = Integer.parseInt(input.substring(0, 4));
            int month = Integer.parseInt(input.substring(5, 7));
            int day = Integer.parseInt(input.substring(8, 10));

            return LocalDate.of(year, month, day);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public void rewriteDailyBalance(ArrayList<CashRecord> cashRecords) throws IOException {
        PrintWriter printWriter = new PrintWriter(new FileWriter(ALL_CASH_RECORDS_PATH));

        for (CashRecord cashRecord : cashRecords) {
            printWriter.println(cashRecord.getRecordID());
            printWriter.println(cashRecord.getDate());
            printWriter.println(cashRecord.getOperation());
            printWriter.printf("%.2f\n", cashRecord.getAmount());
            printWriter.println(cashRecord.getSellerUsername());
            printWriter.println();
        }

        printWriter.close();
    }

    public ArrayList<User> getAllUsers() {
        try {
            Scanner scanner = new Scanner(new File(USERS_DATA_PATH));
            ArrayList<User> users = new ArrayList<>();

            while (scanner.hasNext()) {
                String name = scanner.nextLine();
                String surname = scanner.nextLine();
                String username = scanner.nextLine();
                String password = scanner.nextLine();
                UserType type = getUserType(scanner);
                if (type != null) {
                    User user = new User(name, surname, username, password, type);
                    users.add(user);
                }
                scanner.nextLine();
            }

            return users;
        } catch (FileNotFoundException e) {
            return null;
        }
    }

    private UserType getUserType(Scanner scanner) {
        String type = scanner.nextLine();
        try {
            return UserType.valueOf(type);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public void updateAllUsers(ArrayList<User> users) throws IOException {
        PrintWriter printWriter = new PrintWriter(new FileWriter(USERS_DATA_PATH));

        for (User user : users) {
            printWriter.println(user.getName());
            printWriter.println(user.getSurname());
            printWriter.println(user.getUsername());
            printWriter.println(user.getPassword());
            printWriter.println(user.getUserType());
            printWriter.println();
        }

        printWriter.close();
    }

    public ArrayList<Client> getAllClients() {
        try {
            Scanner scanner = new Scanner(new File(CLIENT_PATH));
            ArrayList<Client> partners = new ArrayList<>();

            while (scanner.hasNext()) {
                String partnerName = scanner.nextLine();
                String businessID = scanner.nextLine();
                String streetAddress = scanner.nextLine();
                String city = scanner.nextLine();
                String country = scanner.nextLine();
                scanner.nextLine();

                partners.add(new Client(partnerName, businessID, streetAddress, city, country));
            }

            return partners;
        } catch (FileNotFoundException e) {
            return null;
        }
    }

    public void updateClientsDatabase(ArrayList<Client> clients) {
        try {
            PrintWriter printWriter = new PrintWriter(new FileWriter(CLIENT_PATH));

            for (Client partner : clients) {
                printWriter.println(partner.getClientName());
                printWriter.println(partner.getBusinessID());
                printWriter.println(partner.getStreetAddress());
                printWriter.println(partner.getCity());
                printWriter.println(partner.getCountry());
                printWriter.println();
            }

            printWriter.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public ArrayList<Item> getAllWarehouseItems() {
        try {
            Scanner scanner = new Scanner(new File(WAREHOUSE_DATA_PATH));
            ArrayList<Item> items = new ArrayList<>();

            while (scanner.hasNext()) {
                String itemName = scanner.nextLine();
                String purchasePriceString = scanner.nextLine();
                String salesPriceString = scanner.nextLine();
                String currentQuantityString = scanner.nextLine();
                scanner.nextLine();

                double purchasePrice = Double.parseDouble(purchasePriceString);
                double salePrice = Double.parseDouble(salesPriceString);
                int currentQuantity = Integer.parseInt(currentQuantityString);

                items.add(new Item(itemName, purchasePrice, salePrice, currentQuantity));
            }

            return items;
        } catch (NumberFormatException | FileNotFoundException e) {
            return null;
        }
    }

    public ArrayList<PurchaseOrderLine> getPurchaseOrderLines() {
        try {
            Scanner scanner = new Scanner(new File(PURCHASE_ORDERS_PATH));
            ArrayList<PurchaseOrderLine> purchaseOrderLines = new ArrayList<>();

            while (scanner.hasNext()) {
                String orderNrString = scanner.nextLine();
                String itemName = scanner.nextLine();
                String purchasePriceString = scanner.nextLine();
                String salesPriceString = scanner.nextLine();
                String currentQuantityString = scanner.nextLine();
                String isOrderFinished = scanner.nextLine();
                scanner.nextLine();

                int orderNr = Integer.parseInt(orderNrString);
                double purchasePrice = Double.parseDouble(purchasePriceString);
                double salePrice = Double.parseDouble(salesPriceString);
                int currentQuantity = Integer.parseInt(currentQuantityString);

                Item item = new Item(itemName, purchasePrice, salePrice, currentQuantity);
                PurchaseOrderLine newLine = new PurchaseOrderLine(orderNr, item);
                if (isOrderFinished.equals("true")) {
                    newLine.setFinished(true);
                }
                purchaseOrderLines.add(newLine);
            }
            return purchaseOrderLines;

        } catch (FileNotFoundException e) {
            return null;
        }
    }

    public void updatedOrderList(ArrayList<PurchaseOrderLine> orderLines) throws IOException {
        PrintWriter printWriter = new PrintWriter(new FileWriter(PURCHASE_ORDERS_PATH));
        printWriter.close();

        for (PurchaseOrderLine orderLine : orderLines) {
            addItemToPurchaseOrder(orderLine);
        }
    }

    public void addItemToPurchaseOrder(PurchaseOrderLine orderLine) throws IOException {
        PrintWriter printWriter = new PrintWriter(new FileWriter(PURCHASE_ORDERS_PATH, true));

        printWriter.println(orderLine.getOrderNr());
        printWriter.println(orderLine.getItemName());
        printWriter.println(orderLine.getUnitPrice());
        String value = orderLine.getSalePrice() + "0";   // adding 0 to protect against integer input
        String purchaseValue = value.substring(0, value.indexOf(".") + 3);
        printWriter.println(purchaseValue);
        printWriter.println(orderLine.getLineQuantity());
        printWriter.println(orderLine.isFinished());
        printWriter.println();

        printWriter.close();
    }


}
