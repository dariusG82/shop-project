package dariusG82.classes.services;

import dariusG82.classes.accounting.finance.CashOperation;
import dariusG82.classes.accounting.finance.CashRecord;
import dariusG82.classes.accounting.finance.ReturnCashRecord;
import dariusG82.classes.accounting.finance.SalesCashRecord;
import dariusG82.classes.accounting.orders.*;
import dariusG82.classes.custom_exeptions.NegativeBalanceException;
import dariusG82.classes.custom_exeptions.WrongDataPathExeption;
import dariusG82.classes.warehouse.Item;
import dariusG82.classes.warehouse.SoldItem;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

public class AccountingService extends Service {
    private final String path = "src/dariusG82/services/data/salesJournal.txt";
    private final String saleOrdersPath = "src/dariusG82/services/data/orders/salesOrderList.txt";
    private final String returnOrderPath = "src/dariusG82/services/data/orders/returnOrderList";

    private final String SALES_ORDER_BEGINS = "SF ";
    private final String RETURN_ORDER_BEGINS = "RE ";

    public void updateSalesRecords(int recordNr, Item item, int quantity, String username) throws IOException, WrongDataPathExeption {
        ArrayList<CashRecord> allCashRecords = getAllRecords();

        SalesCashRecord salesRecord = new SalesCashRecord(recordNr, LocalDate.now(), item.getSalePrice() * quantity, username);
        allCashRecords.add(salesRecord);

        ArrayList<CashRecord> uniqueRecords = sumCashRecordsByID(allCashRecords);

        rewriteDailyBalance(uniqueRecords);
    }

    public void updateReturnRecords(int recordNr, SoldItem item, int quantity, String username) throws IOException, WrongDataPathExeption {
        ArrayList<CashRecord> allCashRecords = getAllRecords();

        ReturnCashRecord returnCashRecord = new ReturnCashRecord(recordNr, LocalDate.now(), item.getSalePrice() * quantity, username);
        allCashRecords.add(returnCashRecord);

        ArrayList<CashRecord> uniqueRecords = sumCashRecordsByID(allCashRecords);

        rewriteDailyBalance(uniqueRecords);
    }

    public Order getDocumentByID(String id) {
        String documentBegins = id.substring(0, id.indexOf(" ") + 1);
        String index = id.substring(id.indexOf(" ") + 1);
        int orderNr = Integer.parseInt(index);

        switch (documentBegins) {
            case SALES_ORDER_BEGINS -> {
                ArrayList<SalesOrderLine> salesOrderLines = getAllSalesOrdersLines();
                SalesOrder salesOrder = new SalesOrder(orderNr);

                for (SalesOrderLine salesOrderLine : salesOrderLines) {
                    if (salesOrderLine.orderNr == orderNr) {
                        salesOrder.getSoldItems().add(salesOrderLine);
                    }
                }

                return salesOrder;
            }
            case RETURN_ORDER_BEGINS -> {
                ArrayList<ReturnOrderLine> salesOrderLines = getAllReturnOrdersLines();
                ReturnOrder returnOrder = new ReturnOrder(orderNr);

                for (ReturnOrderLine returnOrderLine : salesOrderLines) {
                    if (returnOrderLine.orderNr == orderNr) {
                        returnOrder.getReturnItems().add(returnOrderLine);
                    }
                }

                return returnOrder;
            }
        }
        return null;
    }

    public void updateSalesOrderLine(SalesOrder salesOrder) throws WrongDataPathExeption, IOException {
        ArrayList<SalesOrderLine> allSalesOrderLines = getAllSalesOrdersLines();

        if (allSalesOrderLines == null) {
            throw new WrongDataPathExeption();
        }

        ArrayList<SalesOrderLine> orderLines = salesOrder.getSoldItems();

        allSalesOrderLines.addAll(orderLines);

        rewriteSalesOrderLines(allSalesOrderLines);
    }

    public void updateReturnOrderLine(ReturnOrder returnOrder) throws WrongDataPathExeption, IOException {
        ArrayList<ReturnOrderLine> allReturnOrdersLines = getAllReturnOrdersLines();

        if (allReturnOrdersLines == null) {
            throw new WrongDataPathExeption();
        }

        ArrayList<ReturnOrderLine> orderLines = returnOrder.getReturnItems();

        allReturnOrdersLines.addAll(orderLines);

        rewriteReturnOrderLines(allReturnOrdersLines);
    }

    public void updateSalesOrders(SalesOrder salesOrder, SoldItem item, int quantity) throws WrongDataPathExeption, IOException {
        ArrayList<SalesOrderLine> allSalesOrderLines = getAllSalesOrdersLines();

        if (allSalesOrderLines == null) {
            throw new WrongDataPathExeption();
        }

        for (SalesOrderLine salesOrderLine : allSalesOrderLines) {
            if (salesOrderLine.getOrderNr() == salesOrder.getOrderID() && salesOrderLine.getItemName().equals(item.getItemName())) {
                salesOrderLine.updateQuantity(quantity);
            }
        }

        rewriteSalesOrderLines(allSalesOrderLines);
    }

    public SoldItem getSoldItemByName(SalesOrder salesOrder, String itemName) {
        ArrayList<SalesOrderLine> salesOrderLines = salesOrder.getSoldItems();

        for (SalesOrderLine salesOrderLine : salesOrderLines) {
            if (salesOrderLine.getItemName().equals(itemName)) {
                return new SoldItem(itemName, salesOrderLine.getLineQuantity(), salesOrderLine.getUnitPrice(), salesOrderLine.getSalesmanID());
            }
        }
        return null;
    }

    public ArrayList<SalesOrderLine> getAllSalesOrdersLines() {
        try {
            Scanner scanner = new Scanner(new File(saleOrdersPath));
            ArrayList<SalesOrderLine> orderLines = new ArrayList<>();

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

                SalesOrderLine orderLine = new SalesOrderLine(id, itemName, quantity, unitPrice, salesmanID);
                orderLines.add(orderLine);
            }
            return orderLines;
        } catch (FileNotFoundException e) {
            return null;
        }
    }

    public ArrayList<ReturnOrderLine> getAllReturnOrdersLines() {
        try {
            Scanner scanner = new Scanner(new File(returnOrderPath));
            ArrayList<ReturnOrderLine> orderLines = new ArrayList<>();

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

                ReturnOrderLine orderLine = new ReturnOrderLine(id, itemName, quantity, unitPrice, salesmanID);
                orderLines.add(orderLine);
            }
            return orderLines;
        } catch (FileNotFoundException e) {
            return null;
        }
    }

    public ArrayList<CashRecord> getAllRecords() {
        try {
            Scanner scanner = new Scanner(new File(path));
            ArrayList<CashRecord> cashRecords = new ArrayList<>();

            while (scanner.hasNext()) {
                String id = scanner.nextLine();
                LocalDate operationDate = getOperationDate(scanner.nextLine());
                CashOperation cashOperation = getCashOperation(scanner.nextLine());
                double amount = getAmount(scanner.nextLine());
                String sellerUsername = scanner.nextLine();
                scanner.nextLine();

                if (operationDate != null && cashOperation != null && amount != 0.0) {
                    if (id.startsWith(SALES_ORDER_BEGINS)) {
                        cashRecords.add(new SalesCashRecord(id, operationDate, amount, sellerUsername));
                    }
                    if (id.startsWith(RETURN_ORDER_BEGINS)) {
                        cashRecords.add(new ReturnCashRecord(id, operationDate, amount, sellerUsername));
                    }
                }
            }
            return cashRecords;
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

    private void rewriteSalesOrderLines(ArrayList<SalesOrderLine> salesOrderLines) throws IOException {
        PrintWriter printWriter = new PrintWriter(new FileWriter(saleOrdersPath));

        for (SalesOrderLine salesOrderLine : salesOrderLines) {

            printWriter.println(SALES_ORDER_BEGINS + salesOrderLine.getOrderNr());
            printWriter.println(salesOrderLine.getItemName());
            printWriter.println(salesOrderLine.getLineQuantity());
            printWriter.println(salesOrderLine.getUnitPrice());
            printWriter.println(salesOrderLine.getSalesmanID());
            printWriter.println();
        }

        printWriter.close();
    }

    private void rewriteReturnOrderLines(ArrayList<ReturnOrderLine> returnOrderLines) throws IOException {
        PrintWriter printWriter = new PrintWriter(new FileWriter(returnOrderPath));

        for (ReturnOrderLine returnOrderLine : returnOrderLines) {

            printWriter.println(RETURN_ORDER_BEGINS + returnOrderLine.getOrderNr());
            printWriter.println(returnOrderLine.getItemName());
            printWriter.println(returnOrderLine.getLineQuantity());
            printWriter.println(returnOrderLine.getUnitPrice());
            printWriter.println(returnOrderLine.getSalesmanID());
            printWriter.println();
        }

        printWriter.close();
    }

    private ArrayList<CashRecord> sumCashRecordsByID(ArrayList<CashRecord> oldRecords) {

        ArrayList<SalesCashRecord> uniqueSalesRecords = new ArrayList<>();
        ArrayList<ReturnCashRecord> uniqueReturnRecords = new ArrayList<>();

        for (CashRecord record : oldRecords) {
            boolean recordUpdated = false;
            if (record instanceof SalesCashRecord salesCashRecord) {
                String id = salesCashRecord.getRecordID();
                String sellerId = salesCashRecord.getSellerUsername();
                for (SalesCashRecord cashRecord : uniqueSalesRecords) {
                    if (cashRecord.getRecordID().equals(id) && cashRecord.getSellerUsername().equals(sellerId)) {
                        cashRecord.updateAmount(salesCashRecord.getAmount());
                        recordUpdated = true;
                    }
                }
                if (!recordUpdated) {
                    uniqueSalesRecords.add(salesCashRecord);
                }
            } else if (record instanceof ReturnCashRecord returnCashRecord) {
                String id = returnCashRecord.getRecordID();
                String sellerId = returnCashRecord.getSellerUsername();
                for (ReturnCashRecord cashRecord : uniqueReturnRecords) {
                    if (cashRecord.getRecordID().equals(id) && cashRecord.getSellerUsername().equals(sellerId)) {
                        cashRecord.updateAmount(record.getAmount());
                        recordUpdated = true;
                    }
                }
                if (!recordUpdated) {
                    uniqueReturnRecords.add(returnCashRecord);
                }
            }
        }

        ArrayList<CashRecord> cashRecords = new ArrayList<>();
        cashRecords.addAll(uniqueSalesRecords);
        cashRecords.addAll(uniqueReturnRecords);

        return cashRecords;
    }

    public void rewriteDailyBalance(ArrayList<CashRecord> cashRecords) throws IOException {
        PrintWriter printWriter = new PrintWriter(new FileWriter(path));

        for (CashRecord cashRecord : cashRecords) {
            String orderID = "";
            if (cashRecord instanceof SalesCashRecord) {
                orderID = ((SalesCashRecord) cashRecord).getRecordID();
            } else if (cashRecord instanceof ReturnCashRecord) {
                orderID = ((ReturnCashRecord) cashRecord).getRecordID();
            }
            printWriter.println(orderID);
            printWriter.println(cashRecord.getDate());
            printWriter.println(cashRecord.getOperation());
            printWriter.printf("%.2f\n", cashRecord.getAmount());
            printWriter.println(cashRecord.getSellerUsername());
            printWriter.println();
        }

        printWriter.close();
    }

    public double getDaysBalance(LocalDate date) {
        double income = getCashOperationsByTypeAndDay(CashOperation.DAILY_INCOME, date);
        double expense = getCashOperationsByTypeAndDay(CashOperation.DAILY_EXPENSE, date);
        return income - expense;
    }

    public double getMonthBalance(LocalDate date) {
        int year = date.getYear();
        int month = date.getMonthValue();
        double income = getCashOperationsByTypeAndMonth(CashOperation.DAILY_INCOME, year, month);
        double expense = getCashOperationsByTypeAndMonth(CashOperation.DAILY_EXPENSE, year, month);
        return income - expense;
    }

    public ArrayList<CashRecord> getDailySaleDocuments(LocalDate date, CashOperation cashOperation) {
        ArrayList<CashRecord> allCashRecords = getAllRecords();
        ArrayList<CashRecord> dailyRecords = new ArrayList<>();
        for (CashRecord cashRecord : allCashRecords) {
            if (cashRecord.getOperation().equals(cashOperation) && cashRecord.getDate().equals(date)) {
                dailyRecords.add(cashRecord);
            }
        }
        return dailyRecords;
    }

    private double getCashOperationsByTypeAndMonth(CashOperation operation, int year, int month) {
        ArrayList<CashRecord> allCashRecords = getAllRecords();
        double cashSum = 0.0;
        for (CashRecord cashRecord : allCashRecords) {
            if (cashRecord.getOperation().equals(operation) &&
                    cashRecord.getDate().getYear() == year &&
                    cashRecord.getDate().getMonthValue() == month) {
                cashSum += cashRecord.getAmount();
            }
        }
        return cashSum;
    }

    private double getCashOperationsByTypeAndDay(CashOperation operation, LocalDate date) {
        ArrayList<CashRecord> allCashRecords = getAllRecords();
        double cashSum = 0.0;
        for (CashRecord cashRecord : allCashRecords) {
            if (cashRecord.getOperation().equals(operation) && cashRecord.getDate().equals(date)) {
                cashSum += cashRecord.getAmount();
            }
        }
        return cashSum;
    }

    public int getNewSalesDocumentNumber() throws WrongDataPathExeption, IOException {
        int documentNr = getInfoFromDataString(SALES_ORDER_NR_INFO);

        if (documentNr > 0) {
            return documentNr;
        } else {
            throw new WrongDataPathExeption();
        }
    }

    public int getNewReturnDocumentNumber() throws WrongDataPathExeption, IOException {
        int documentNr = getInfoFromDataString(RETURN_ORDER_NR_INFO);
        if (documentNr > 0) {
            return documentNr;
        } else {
            throw new WrongDataPathExeption();
        }
    }

    public ArrayList<SalesCashRecord> getSalesReportBySalesperson(String username, int year, int month) {
        ArrayList<SalesCashRecord> recordsBySeller = getSalesRecordsForSeller(username);

        return getSalesRecordsForMonth(recordsBySeller, year, month);
    }

    public double getTotalSalesByReport(ArrayList<SalesCashRecord> records) {
        double totalSales = 0.0;
        for (SalesCashRecord record : records) {
            totalSales += record.getAmount();
        }
        return totalSales;
    }

    private ArrayList<SalesCashRecord> getSalesRecordsForSeller(String sellerUsername) {
        ArrayList<CashRecord> allRecords = getAllRecords();
        ArrayList<SalesCashRecord> salesCashRecords = new ArrayList<>();

        for (CashRecord record : allRecords) {
            if (record instanceof SalesCashRecord salesRecord && record.getSellerUsername().equals(sellerUsername)) {
                salesCashRecords.add(salesRecord);
            }
        }

        return salesCashRecords;
    }

    private ArrayList<SalesCashRecord> getSalesRecordsForMonth(ArrayList<SalesCashRecord> records, int year,
                                                               int month) {
        ArrayList<SalesCashRecord> recordsForMonth = new ArrayList<>();

        for (SalesCashRecord salesCashRecord : records) {
            if (salesCashRecord.getDate().getYear() == year && salesCashRecord.getDate().getMonthValue() == month) {
                recordsForMonth.add(salesCashRecord);
            }
        }

        return recordsForMonth;
    }

    public void updateBalance(OrderLine orderLine) throws
            WrongDataPathExeption, IOException, NegativeBalanceException {
        ArrayList<String> dataList = getDataStrings();

        if (dataList == null) {
            throw new WrongDataPathExeption();
        }

        double newBalance = 0.0;

        if (orderLine instanceof SalesOrderLine salesOrderLine) {
            newBalance = getBalanceFromDataString() + salesOrderLine.getLineAmount();
        } else if (orderLine instanceof ReturnOrderLine returnOrderLine) {
            newBalance = getBalanceFromDataString() - returnOrderLine.getLineAmount();
        } else if (orderLine instanceof PurchaseOrderLine purchaseOrderLine) {
            newBalance = getBalanceFromDataString() - purchaseOrderLine.getLineAmount();
        }

        if (newBalance > 0) {
            updateBalanceStringData(dataList, newBalance);
            updateDataStrings(dataList);
        } else {
            throw new NegativeBalanceException();
        }

    }

    private void updateBalanceStringData(ArrayList<String> dataList, double newBalance) {
        for (String data : dataList) {
            if (data.startsWith(CURRENT_BALANCE)) {
                int index = dataList.indexOf(data);
                String balance = newBalance + "0";
                String balanceString = data.substring(0, data.indexOf("-") + 1) + balance.substring(0, balance.indexOf(".") + 2);
                dataList.set(index, balanceString);
            }
        }
    }
}
