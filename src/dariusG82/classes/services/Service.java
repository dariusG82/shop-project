package dariusG82.classes.services;

import dariusG82.classes.custom_exeptions.WrongDataPathExeption;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Service {

    private static final String DATA_PATH = "src/dariusG82/services/data/systemData.txt";
    protected static final String PURCHASE_ORDER_NR_INFO = "1|";
    protected static final String SALES_ORDER_NR_INFO = "2|";
    protected static final String RETURN_ORDER_NR_INFO = "3|";
    protected static final String CURRENT_BALANCE = "4|";

    protected ArrayList<String> getDataStrings() {
        try {
            Scanner scanner = new Scanner(new File(DATA_PATH));
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

    protected void updateDataStrings(ArrayList<String> updatedDataStrings) throws IOException {
        PrintWriter printWriter = new PrintWriter(new FileWriter(DATA_PATH));

        for (String dataString : updatedDataStrings) {
            printWriter.println(dataString);
            printWriter.println();
        }

        printWriter.close();
    }

    protected int getInfoFromDataString(String infoSection) throws IOException, WrongDataPathExeption {
        ArrayList<String> dataList = getDataStrings();

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
        ArrayList<String> dataList = getDataStrings();

        if (dataList == null) {
            throw new WrongDataPathExeption();
        }

        for (String data : dataList) {
            if(data.startsWith(CURRENT_BALANCE)){
                Double balance = getBalance(data);
                if(balance != null) {
                    return balance;
                }
            }
        }
        return 0.0;
    }



    private Integer getOrderNr(ArrayList<String> dataList, String infoSection, String data) throws IOException {
        if (data.startsWith(infoSection)) {
            String purchaseOrderNumberString = data.substring(data.indexOf("-") + 1);
            int newOrderNumber = Integer.parseInt(purchaseOrderNumberString);
            int index = dataList.indexOf(data);
            String newString = data.substring(0, data.indexOf("-") + 1) + ++newOrderNumber;
            dataList.set(index, newString);
            updateDataStrings(dataList);
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
}
