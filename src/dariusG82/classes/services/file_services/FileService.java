package dariusG82.classes.services.file_services;

import dariusG82.classes.custom_exeptions.ClientDoesNotExistExeption;
import dariusG82.classes.custom_exeptions.WrongDataPathExeption;
import dariusG82.classes.data.interfaces.DataManagement;
import dariusG82.classes.data.interfaces.FileReaderInterface;
import dariusG82.classes.data.interfaces.ServiceInterface;
import dariusG82.classes.partners.Client;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

public class FileService implements ServiceInterface, FileReaderInterface {

    private final DataManagement dataService = new DataFromFileService();
    private final AccountingFileService accountingService;
    //    private final AdminService adminService;
    private final BusinessFileService businessService;
    private final WarehouseFileService warehouseService;

    public FileService() {
        this.accountingService = new AccountingFileService(this.dataService);
//        this.adminService = new AdminService(this.dataService);
        this.businessService = new BusinessFileService(this.dataService);
        this.warehouseService = new WarehouseFileService(this.dataService);

    }

    public static final String CURRENT_DATE = "0|";
    public static final String PURCHASE_ORDER_NR_INFO = "1|";
    public static final String SALES_ORDER_NR_INFO = "2|";
    public static final String RETURN_ORDER_NR_INFO = "3|";
    public static final String CASH_REGISTER = "4|";
    public static final String BANK_ACCOUNT = "5|";

    protected int getInfoFromDataString(String infoSection) throws IOException, WrongDataPathExeption {
        ArrayList<String> dataList = reader.getDataStrings();

        if (dataList == null) {
            throw new WrongDataPathExeption();
        }

        for (String data : dataList) {
            int orderNr = getOrderNr(dataList, infoSection, data);
            if (orderNr > 0) {
                return orderNr;
            }
        }
        return 0;
    }

    protected double getBalanceFromDataString(String infoSection) throws WrongDataPathExeption {
        ArrayList<String> dataList = reader.getDataStrings();

        if (dataList == null) {
            throw new WrongDataPathExeption();
        }

        double balance = 0.0;

        for (String data : dataList) {
            balance = getBalance(data, infoSection);
            if (balance > 0.0) {
                break;
            }
        }
        return balance;
    }

    public LocalDate getLoginDate() throws WrongDataPathExeption {
        ArrayList<String> datalist = reader.getDataStrings();

        if (datalist == null) {
            throw new WrongDataPathExeption();
        }

        for (String data : datalist) {
            if (data.startsWith(CURRENT_DATE)) {
                String dateTime = data.substring(data.indexOf("-") + 1);
                return LocalDate.parse(dateTime);
            }
        }
        return null;
    }

    private int getOrderNr(ArrayList<String> dataList, String infoSection, String data) throws IOException {
        if (data.startsWith(infoSection)) {
            String purchaseOrderNumberString = data.substring(data.indexOf("-") + 1);
            int newOrderNumber = Integer.parseInt(purchaseOrderNumberString);
            int index = dataList.indexOf(data);
            String newString = data.substring(0, data.indexOf("-") + 1) + ++newOrderNumber;
            dataList.set(index, newString);
            reader.updateDataStrings(dataList);
            return newOrderNumber;
        }
        return 0;
    }

    private double getBalance(String data, String infoString) {
        if (data.startsWith(infoString)) {
            String purchaseOrderNumberString = data.substring(data.indexOf("=") + 1);
            return Double.parseDouble(purchaseOrderNumberString);
        }
        return 0.0;
    }

    public Client getClientByName(String name) throws ClientDoesNotExistExeption, WrongDataPathExeption {
        ArrayList<Client> clients = dataService.getAllClients();

        if (clients != null) {
            for (Client client : clients) {
                if (client.clientName().equals(name)) {
                    return client;
                }
            }
        } else {
            throw new WrongDataPathExeption();
        }
        throw new ClientDoesNotExistExeption(name);
    }

    @Override
    public DataManagement getDataService() {
        return dataService;
    }

    @Override
    public WarehouseFileService getWarehouseService() {
        return warehouseService;
    }

    @Override
    public BusinessFileService getBusinessService() {
        return businessService;
    }

    @Override
    public AccountingFileService getAccountingService() {
        return accountingService;
    }

    public void countIncomeAndExpensesByDays() throws WrongDataPathExeption, IOException {

    }
}
