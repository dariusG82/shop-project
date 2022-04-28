package dariusG82.classes.services;

import dariusG82.classes.custom_exeptions.WrongDataPathExeption;
import dariusG82.classes.data.DataManagement;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

public class Service {

    protected DataManagement dataService = new DataFromFileService();
    public final String PURCHASE_ORDERS_PATH = "src/dariusG82/classes/data/orders/purchaseOrderList.txt";
    public final String RETURN_ORDERS_PATH = "src/dariusG82/classes/data/orders/returnOrderList";
    public final String SALES_ORDERS_PATH = "src/dariusG82/classes/data/orders/salesOrderList.txt";

    public final String CLIENT_PATH = "src/dariusG82/classes/data/clients.txt";
    public final String DAILY_CASH_JOURNALS_PATH = "src/dariusG82/classes/data/dailyCashJournals.txt";
    public final String USERS_DATA_PATH = "src/dariusG82/classes/data/users.txt";
    public final String WAREHOUSE_DATA_PATH = "src/dariusG82/classes/data/warehouse.txt";

    public static final String CURRENT_DATE = "0|";
    public static final String PURCHASE_ORDER_NR_INFO = "1|";
    public static final String SALES_ORDER_NR_INFO = "2|";
    public static final String RETURN_ORDER_NR_INFO = "3|";
    public static final String CURRENT_BALANCE = "4|";

    protected int getInfoFromDataString(String infoSection) throws IOException, WrongDataPathExeption {
        ArrayList<String> dataList = dataService.getDataStrings();

        if (dataList == null) {
            throw new WrongDataPathExeption();
        }

        for (String data : dataList) {
            switch (infoSection) {
                case PURCHASE_ORDER_NR_INFO -> {
                    Integer newOrderNumber = getOrderNr(dataList, infoSection, data);
                    if (newOrderNumber != null) {
                        return newOrderNumber;
                    }
                }
                case SALES_ORDER_NR_INFO -> {
                    Integer newSalesOrderNumber = getOrderNr(dataList, infoSection, data);
                    if (newSalesOrderNumber != null) {
                        return newSalesOrderNumber;
                    }
                }
                case RETURN_ORDER_NR_INFO -> {
                    Integer newReturnOrderNumber = getOrderNr(dataList, infoSection, data);
                    if (newReturnOrderNumber != null) {
                        return newReturnOrderNumber;
                    }
                }
            }
        }
        return 0;
    }

    protected double getBalanceFromDataString() throws WrongDataPathExeption {
        ArrayList<String> dataList = dataService.getDataStrings();

        if (dataList == null) {
            throw new WrongDataPathExeption();
        }

        for (String data : dataList) {
            if (data.startsWith(CURRENT_BALANCE)) {
                Double balance = getBalance(data);
                if (balance != null) {
                    return balance;
                }
            }
        }
        return 0.0;
    }

    public LocalDate getLoginDate() throws WrongDataPathExeption {
        ArrayList<String> datalist = dataService.getDataStrings();

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

    private Integer getOrderNr(ArrayList<String> dataList, String infoSection, String data) throws IOException {
        if (data.startsWith(infoSection)) {
            String purchaseOrderNumberString = data.substring(data.indexOf("-") + 1);
            int newOrderNumber = Integer.parseInt(purchaseOrderNumberString);
            int index = dataList.indexOf(data);
            String newString = data.substring(0, data.indexOf("-") + 1) + ++newOrderNumber;
            dataList.set(index, newString);
            dataService.updateDataStrings(dataList);
            return newOrderNumber;
        }
        return null;
    }

    private Double getBalance(String data) {
        if (data.startsWith(Service.CURRENT_BALANCE)) {
            String purchaseOrderNumberString = data.substring(data.indexOf("-") + 1);
            return Double.parseDouble(purchaseOrderNumberString);
        }
        return null;
    }

    public DataManagement getDataService() {
        return dataService;
    }
}
