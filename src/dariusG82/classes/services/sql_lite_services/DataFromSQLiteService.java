package dariusG82.classes.services.sql_lite_services;

import dariusG82.classes.accounting.DailyReport;
import dariusG82.classes.accounting.finance.CashRecord;
import dariusG82.classes.accounting.orders.OrderLine;
import dariusG82.classes.accounting.orders.PurchaseOrderLine;
import dariusG82.classes.data.interfaces.DataManagement;
import dariusG82.classes.partners.Client;
import dariusG82.classes.users.User;
import dariusG82.classes.users.UserType;
import dariusG82.classes.warehouse.Item;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;

import static dariusG82.classes.services.sql_lite_services.SQL_Query.*;

public class DataFromSQLiteService extends SQLService implements DataManagement {

    private final AdminDatabaseService adminDatabaseService = new AdminDatabaseService();
    private final BusinessDatabaseService businessDatabaseService = new BusinessDatabaseService();
    private final WarehouseDatabaseService warehouseDatabaseService = new WarehouseDatabaseService();
    private final AccountingDatabaseService accountingDatabaseService = new AccountingDatabaseService();

    @Override
    public void rewriteDailyReports(ArrayList<DailyReport> dailyReports) {
        // TODO implement method
    }

    @Override
    public ArrayList<OrderLine> getAllOrderLines(String orderDataPath) {
        // TODO implement method
        return null;
    }

    @Override
    public ArrayList<CashRecord> getAllCashRecords() {
        // TODO implement method
        return null;
    }

    @Override
    public ArrayList<DailyReport> getDailyReports() {
        ArrayList<DailyReport> reports = new ArrayList<>();
        String query = SELECT_CASH_JOURNAL.getQuery();

        try {
            Connection connection = this.connectToDB();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                String reportID = resultSet.getString("reportID");
                LocalDate reportDate = LocalDate.parse(resultSet.getString("reportDate"));
                double dailyIncome = resultSet.getDouble("dailyIncome");
                double dailyExpenses = resultSet.getDouble("dailyExpenses");
                double dailyBalance = resultSet.getDouble("dailyBalance");

                reports.add(new DailyReport(reportID, reportDate, dailyIncome, dailyExpenses, dailyBalance));
            }
            return reports;
        } catch (SQLException e) {
            return null;
        }
    }

    @Override
    public void rewriteDailyBalance(ArrayList<CashRecord> cashRecords) {
        // TODO implement method
    }

    @Override
    public void rewriteOrderLines(ArrayList<OrderLine> orderLines, String path) {
        // TODO implement method
    }

    @Override
    public ArrayList<User> getAllUsers() {
        ArrayList<User> users = new ArrayList<>();
        String query = SELECT_ALL_USERS.getQuery();

        try {
            Connection connection = this.connectToDB();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                String name = resultSet.getString("name");
                String surname = resultSet.getString("surname");
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                UserType type = UserType.valueOf(resultSet.getString("userType"));

                users.add(new User(name, surname, username, password, type));
            }
            return users;
        } catch (SQLException e) {
            return null;
        }
    }

    @Override
    public void updateAllUsers(ArrayList<User> users) {
        // not needed in database
    }

    @Override
    public ArrayList<Client> getAllClients() {
        ArrayList<Client> clients = new ArrayList<>();
        String query = SELECT_ALL_CLIENTS.getQuery();

        try {
            Connection connection = this.connectToDB();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                String clientID = resultSet.getString("clientID");
                String clientName = resultSet.getString("clientName");
                String clientStreetAddress = resultSet.getString("clientStreetAddress");
                String clientCityAddress = resultSet.getString("clientCityAddress");
                String clientCountryAddress = resultSet.getString("clientCountryAddress");

                clients.add(new Client(clientName, clientID, clientStreetAddress, clientCityAddress, clientCountryAddress));
            }
            return clients;
        } catch (SQLException e) {
            return null;
        }
    }

    @Override
    public void updateClientsDatabase(ArrayList<Client> clients) {
        // not needed in database
    }

    @Override
    public ArrayList<Item> getAllWarehouseItems() {
        ArrayList<Item> items = new ArrayList<>();
        String query = SELECT_ALL_WAREHOUSE.getQuery();

        try {
            Connection connection = this.connectToDB();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                String itemName = resultSet.getString("itemName");
                int itemQuantity = resultSet.getInt("itemQuantity");
                double purchasePrice = resultSet.getDouble("itemPurchasePrice");
                double itemSalePrice = resultSet.getDouble("itemSalePrice");

                items.add(new Item(itemName, purchasePrice, itemSalePrice, itemQuantity));
            }
            return items;
        } catch (SQLException e) {
            return null;
        }
    }

    @Override
    public void saveWarehouseStock(ArrayList<Item> items) {
        // not needed in database
    }

    @Override
    public ArrayList<PurchaseOrderLine> getPurchaseOrderLines() {
        // TODO implement method
        return null;
    }

    @Override
    public void updatePurchaseOrderLines(ArrayList<PurchaseOrderLine> orderLines) {
        // not needed in database
    }

    @Override
    public void addItemToPurchaseOrder(PurchaseOrderLine orderLine) {
        // TODO implement method
    }

    @Override
    public AdminDatabaseService getAdmin() {
        return adminDatabaseService;
    }

    @Override
    public AccountingDatabaseService getAccounting() {
        return accountingDatabaseService;
    }

    @Override
    public BusinessDatabaseService getBusiness() {
        return businessDatabaseService;
    }

    @Override
    public WarehouseDatabaseService getWarehouse() {
        return warehouseDatabaseService;
    }
}
