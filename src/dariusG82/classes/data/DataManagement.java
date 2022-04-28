package dariusG82.classes.data;

import dariusG82.classes.accounting.DailyReport;
import dariusG82.classes.accounting.finance.CashRecord;
import dariusG82.classes.accounting.orders.OrderLine;
import dariusG82.classes.accounting.orders.PurchaseOrderLine;
import dariusG82.classes.custom_exeptions.WrongDataPathExeption;
import dariusG82.classes.partners.Client;
import dariusG82.classes.users.User;
import dariusG82.classes.warehouse.Item;

import java.io.IOException;
import java.util.ArrayList;

public interface DataManagement {

    ArrayList<String> getDataStrings();

    void updateDataStrings(ArrayList<String> updatedDataStrings) throws IOException;

    void rewriteDailyReports(ArrayList<DailyReport> dailyReports) throws IOException;

    ArrayList<OrderLine> getAllOrderLines(String orderDataPath);

    ArrayList<CashRecord> getAllCashRecords();

    ArrayList<DailyReport> getDailyReports();

    void rewriteDailyBalance(ArrayList<CashRecord> cashRecords) throws IOException;

    void rewriteOrderLines(ArrayList<OrderLine> orderLines, String path) throws IOException, WrongDataPathExeption;

    ArrayList<User> getAllUsers();

    void updateAllUsers(ArrayList<User> users) throws IOException;

    ArrayList<Client> getAllClients();

    void updateClientsDatabase(ArrayList<Client> clients);

    ArrayList<Item> getAllWarehouseItems();

    void saveWarehouseStock(ArrayList<Item> items) throws IOException;

    ArrayList<PurchaseOrderLine> getPurchaseOrderLines();

    void updatedOrderList(ArrayList<PurchaseOrderLine> orderLines) throws IOException;

    void addItemToPurchaseOrder(PurchaseOrderLine orderLine) throws IOException;
}

