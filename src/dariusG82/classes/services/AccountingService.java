package dariusG82.classes.services;

import dariusG82.classes.accounting.DailyReport;
import dariusG82.classes.accounting.finance.CashOperation;
import dariusG82.classes.accounting.finance.CashRecord;
import dariusG82.classes.accounting.orders.*;
import dariusG82.classes.custom_exeptions.NegativeBalanceException;
import dariusG82.classes.custom_exeptions.WrongDataPathExeption;
import dariusG82.classes.warehouse.ReturnedItem;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

public class AccountingService extends Service {

    public void updateCashRecords(CashRecord cashRecord) throws IOException {
        ArrayList<CashRecord> allCashRecords = dataService.getAllCashRecords();

        if (allCashRecords != null) {
            allCashRecords.add(cashRecord);
            ArrayList<CashRecord> uniqueRecords = sumCashRecordsByID(allCashRecords);
            dataService.rewriteDailyBalance(uniqueRecords);
        }
    }

    public Order getDocumentByID(String id) {
        String documentBegins = id.substring(0, id.indexOf(" ") + 1);
        String index = id.substring(id.indexOf(" ") + 1);
        int orderNr = Integer.parseInt(index);

        switch (documentBegins) {
            case "SF " -> {
                return getOrder(SALES_ORDERS_PATH, orderNr);
            }
            case "RE " -> {
                return getOrder(RETURN_ORDERS_PATH, orderNr);
            }
        }
        return null;
    }

    private Order getOrder(String orderDataPath, int orderNr) {
        ArrayList<OrderLine> orderLines = dataService.getAllOrderLines(orderDataPath);
        Order order = null;

        if (orderDataPath.equals(SALES_ORDERS_PATH)) {
            order = new SalesOrder(orderNr);
        } else if (orderDataPath.equals(RETURN_ORDERS_PATH)) {
            order = new ReturnOrder(orderNr);
        }

        if (orderLines == null || order == null) {
            return null;
        }

        for (OrderLine orderLine : orderLines) {
            if (orderLine.orderNr == orderNr) {
                if (orderLine instanceof SalesOrderLine salesOrderLine && order instanceof SalesOrder) {
                    ((SalesOrder) order).getOrderItems().add(salesOrderLine);
                }
                if (orderLine instanceof ReturnOrderLine returnOrderLine && order instanceof ReturnOrder) {
                    ((ReturnOrder) order).getOrderItems().add(returnOrderLine);
                }
            }
        }
        return order;
    }

    public void updateSalesOrderLine(SalesOrder salesOrder) throws WrongDataPathExeption, IOException {
        ArrayList<OrderLine> allSalesOrderLines = dataService.getAllOrderLines(SALES_ORDERS_PATH);

        if (allSalesOrderLines == null) {
            throw new WrongDataPathExeption();
        }

        ArrayList<SalesOrderLine> orderLines = salesOrder.getOrderItems();

        allSalesOrderLines.addAll(orderLines);

        dataService.rewriteOrderLines(allSalesOrderLines, SALES_ORDERS_PATH);
    }

    public void updateReturnOrderLines(ReturnOrder returnOrder) throws WrongDataPathExeption, IOException {
        ArrayList<OrderLine> allReturnOrdersLines = dataService.getAllOrderLines(RETURN_ORDERS_PATH);

        if (allReturnOrdersLines == null) {
            throw new WrongDataPathExeption();
        }

        ArrayList<ReturnOrderLine> orderLines = returnOrder.getOrderItems();

        allReturnOrdersLines.addAll(orderLines);

        dataService.rewriteOrderLines(allReturnOrdersLines, RETURN_ORDERS_PATH);
    }

    public void refreshSalesOrdersQuantity(SalesOrder salesOrder, ReturnedItem item, int quantity) throws WrongDataPathExeption, IOException {
        ArrayList<OrderLine> allSalesOrderLines = dataService.getAllOrderLines(SALES_ORDERS_PATH);

        if (allSalesOrderLines == null) {
            throw new WrongDataPathExeption();
        }

        for (OrderLine salesOrderLine : allSalesOrderLines) {
            if (salesOrderLine.getOrderNr() == salesOrder.getOrderID() && salesOrderLine.getItemName().equals(item.getItemName())) {
                ((SalesOrderLine) salesOrderLine).updateQuantity(quantity);
            }
        }
        dataService.rewriteOrderLines(allSalesOrderLines, SALES_ORDERS_PATH);
    }

    public ReturnedItem getSoldItemByName(SalesOrder salesOrder, String itemName) {
        ArrayList<SalesOrderLine> salesOrderLines = salesOrder.getOrderItems();

        for (SalesOrderLine salesOrderLine : salesOrderLines) {
            if (salesOrderLine.getItemName().equals(itemName)) {
                return new ReturnedItem(itemName, salesOrderLine.getLineQuantity(), salesOrderLine.getUnitPrice(), salesOrderLine.getSalesmanID());
            }
        }
        return null;
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
        ArrayList<CashRecord> allCashRecords = dataService.getAllCashRecords();
        ArrayList<CashRecord> cashRecords = new ArrayList<>();
        if (allCashRecords == null) {
            return null;
        }
        for (CashRecord cashRecord : allCashRecords) {
            if (cashRecord.getOperation().equals(cashOperation) && cashRecord.getDate().equals(date)) {
                    cashRecords.add(cashRecord);
            }
        }

         return cashRecords;
    }

    public int getNewDocumentNumber(String orderType) throws IOException, WrongDataPathExeption {
        int documentNr = getInfoFromDataString(orderType);

        if (documentNr > 0) {
            return documentNr;
        } else {
            throw new WrongDataPathExeption();
        }
    }

    public ArrayList<CashRecord> getSalesReportBySalesperson(String username, int year, int month) {
        ArrayList<CashRecord> recordsBySeller = getSalesRecordsForSeller(username);

        return getSalesRecordsForMonth(recordsBySeller, year, month);
    }

    public double getTotalSalesByReport(ArrayList<CashRecord> records) {
        double totalSales = 0.0;
        for (CashRecord record : records) {
            totalSales += record.getAmount();
        }
        return totalSales;
    }

    public void updateBalance(OrderLine orderLine) throws WrongDataPathExeption, IOException, NegativeBalanceException {
        ArrayList<String> dataList = dataService.getDataStrings();

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
            dataService.updateDataStrings(dataList);
        } else {
            throw new NegativeBalanceException();
        }
    }

    public void countIncomeAndExpensesByDays() throws WrongDataPathExeption, IOException {
        ArrayList<CashRecord> cashRecords = dataService.getAllCashRecords();
        ArrayList<DailyReport> dailyReports = new ArrayList<>();

        if (cashRecords == null) {
            throw new WrongDataPathExeption();
        }

        CashRecord record = cashRecords.get(0);
        DailyReport report = new DailyReport(record.getDate());
        report.updateDailyReport(record.getOperation(), record.getAmount());
        dailyReports.add(report);
        int dailyReportIndex = 0;

        for (int index = 1; index < cashRecords.size(); index++) {
            CashRecord currentRecord = cashRecords.get(index);
            DailyReport dailyReport = dailyReports.get(dailyReportIndex);
            if (currentRecord.getDate().equals(dailyReport.getDate())) {
                dailyReport.updateDailyReport(currentRecord.getOperation(), currentRecord.getAmount());
            } else {
                dailyReportIndex++;
                dailyReport = new DailyReport(currentRecord.getDate());
                dailyReport.updateDailyReport(currentRecord.getOperation(), currentRecord.getAmount());
                dailyReports.add(dailyReport);
            }
        }

        dataService.rewriteDailyReports(dailyReports);
    }

    private ArrayList<CashRecord> sumCashRecordsByID(ArrayList<CashRecord> oldRecords) {
        ArrayList<CashRecord> cashRecords = new ArrayList<>();

        for (CashRecord record : oldRecords) {
            boolean recordUpdated = false;

            String id = record.getRecordID();

            for (CashRecord cashRecord : cashRecords) {
                if (cashRecord.getRecordID().equals(id) && cashRecord.getSellerUsername().equals(record.getSellerUsername())) {
                    cashRecord.updateAmount(record.getAmount());
                    recordUpdated = true;
                }
            }
            if (!recordUpdated) {
                cashRecords.add(record);
            }
        }

        return cashRecords;
    }

    private double getCashOperationsByTypeAndMonth(CashOperation operation, int year, int month) {
        ArrayList<CashRecord> allCashRecords = dataService.getAllCashRecords();
        double cashSum = 0.0;
        if (allCashRecords == null) {
            return cashSum;
        }
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
        ArrayList<CashRecord> allCashRecords = dataService.getAllCashRecords();
        double cashSum = 0.0;
        if (allCashRecords == null) {
            return cashSum;
        }
        for (CashRecord cashRecord : allCashRecords) {
            if (cashRecord.getOperation().equals(operation) && cashRecord.getDate().equals(date)) {
                cashSum += cashRecord.getAmount();
            }
        }
        return cashSum;
    }

    private ArrayList<CashRecord> getSalesRecordsForSeller(String sellerUsername) {
        ArrayList<CashRecord> allRecords = dataService.getAllCashRecords();
        ArrayList<CashRecord> salesCashRecords = new ArrayList<>();

        if (allRecords == null) {
            return new ArrayList<>();
        }

        for (CashRecord record : allRecords) {
            if (record.getRecordID().startsWith("SF ") && record.getSellerUsername().equals(sellerUsername)) {
                salesCashRecords.add(record);
            }
        }

        return salesCashRecords;
    }

    private ArrayList<CashRecord> getSalesRecordsForMonth(ArrayList<CashRecord> records, int year,
                                                               int month) {
        ArrayList<CashRecord> recordsForMonth = new ArrayList<>();

        for (CashRecord cashRecord : records) {
            if (cashRecord.getDate().getYear() == year && cashRecord.getDate().getMonthValue() == month) {
                recordsForMonth.add(cashRecord);
            }
        }
        return recordsForMonth;
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
