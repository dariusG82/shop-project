package dariusG82.services.warehouse;

import dariusG82.services.accounting.orders.PurchaseOrder;
import dariusG82.services.custom_exeptions.ItemIsNotInWarehouseExeption;
import dariusG82.services.custom_exeptions.PurchaseOrderDoesNotExistExeption;
import dariusG82.services.custom_exeptions.WrongDataPathExeption;
import dariusG82.services.warehouse.items.Item;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class WarehouseService {

    private static final String path = "src/dariusG82/services/data/warehouse.txt";
    private static final String orderPath = "src/dariusG82/services/data/orderList.txt";
    private static final String dataPath = "src/dariusG82/services/data/systemData.txt";
    private static int purchaseOrderNumber;

    public int getNewPurchaseOrderNumber() throws WrongDataPathExeption, IOException {
        ArrayList<String> dataList = getDataStrings();
        int newOrderNumber = 0;

        if (dataList == null) {
            throw new WrongDataPathExeption();
        }

        for (String data : dataList) {
            if (data.startsWith("1|")) {
                String purchaseOrderNumberString = data.substring(data.indexOf("-") + 1);
                newOrderNumber = Integer.parseInt(purchaseOrderNumberString);
                int index = dataList.indexOf(data);
                String newString = data.substring(0, data.indexOf("-")+1) + ++newOrderNumber;
                dataList.set(index, newString);
            }
        }

        updateDataStrings(dataList);

        return newOrderNumber;
    }

    private ArrayList<String> getDataStrings() {
        try {
            Scanner scanner = new Scanner(new File(dataPath));
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

    private void updateDataStrings(ArrayList<String> updatedDataStrings) throws IOException {
        PrintWriter printWriter = new PrintWriter(new FileWriter(dataPath));

        for(String dataString : updatedDataStrings){
            printWriter.println(dataString);
            printWriter.println();
        }

        printWriter.close();
    }

    public void addItemToPurchaseOrder(PurchaseOrder order) throws IOException {
        PrintWriter printWriter = new PrintWriter(new FileWriter(orderPath, true));

        printWriter.println(order.getOrderNr());
        printWriter.println(order.getItem().getItemName());
        printWriter.println(order.getItem().getPurchasePrice());
        String value = order.getItem().getSalePrice() + "0";   // adding 0 to protect against integer input
        String purchaseValue = value.substring(0, value.indexOf(".") + 3);
        printWriter.println(purchaseValue);
        printWriter.println(order.getItem().getCurrentQuantity());
        printWriter.println(order.isFinished());
        printWriter.println();

        printWriter.close();
    }

    public void receiveGoods(int purchaseOrder) throws PurchaseOrderDoesNotExistExeption, IOException {
        ArrayList<PurchaseOrder> purchaseOrders = getPurchaseOrder();
        ArrayList<Item> allItems = getAllWarehouseItems();

        if (purchaseOrders == null) {
            throw new PurchaseOrderDoesNotExistExeption(purchaseOrder);
        } else if (allItems == null) {
            throw new FileNotFoundException();
        }

        ArrayList<PurchaseOrder> updatedOrders = new ArrayList<>();

        for (PurchaseOrder order : purchaseOrders) {
            if (order.orderNr == purchaseOrder) {
                allItems.add(order.getItem());
                order.setFinished(true);
            }
            updatedOrders.add(order);
        }

        ArrayList<Item> uniqueItems = getUniqueItemsList(allItems);

        updatedOrderList(updatedOrders);
        saveWarehouseDatabase(uniqueItems);
    }

    private ArrayList<PurchaseOrder> getPurchaseOrder() {
        try {
            Scanner scanner = new Scanner(new File(orderPath));
            ArrayList<PurchaseOrder> purchaseOrders = new ArrayList<>();

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

                PurchaseOrder purchaseOrder = new PurchaseOrder(orderNr, new Item(itemName, purchasePrice, salePrice, currentQuantity));
                if (isOrderFinished.equals("true")) {
                    purchaseOrder.setFinished(true);
                }

                purchaseOrders.add(purchaseOrder);
            }
            return purchaseOrders;

        } catch (FileNotFoundException e) {
            return null;
        }
    }

    public void addItemToWarehouse(Item item) throws WrongDataPathExeption, IOException {
        ArrayList<Item> alItems = getAllWarehouseItems();

        if (alItems != null) {
            alItems.add(item);
            saveWarehouseDatabase(alItems);
        } else {
            throw new WrongDataPathExeption();
        }
    }

    public Item getItemFromWarehouse(String itemName) throws ItemIsNotInWarehouseExeption {
        ArrayList<Item> allItems = getAllWarehouseItems();

        if (allItems == null) {
            throw new ItemIsNotInWarehouseExeption();
        }

        for (Item item : allItems) {
            if (item.getItemName().equals(itemName)) {
                return item;
            }
        }

        throw new ItemIsNotInWarehouseExeption();
    }

    private ArrayList<Item> getAllWarehouseItems() {
        try {
            Scanner scanner = new Scanner(new File(path));
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

    private ArrayList<Item> getUniqueItemsList(ArrayList<Item> items) {
        ArrayList<Item> uniqueItems = new ArrayList<>();

        for (Item item : items) {
            int index = uniqueItems.indexOf(item);
            if (index != -1) {
                uniqueItems.get(index).updateQuantity(item.getCurrentQuantity());
            } else {
                uniqueItems.add(item);
            }
        }
        return uniqueItems;
    }

    private void updatedOrderList(ArrayList<PurchaseOrder> purchaseOrders) throws IOException {
        PrintWriter printWriter = new PrintWriter(new FileWriter(orderPath));
        printWriter.close();

        for (PurchaseOrder order : purchaseOrders) {
            addItemToPurchaseOrder(order);
        }
    }

    public void updateWarehouseStock(Item soldItem, int quantity) throws IOException, ItemIsNotInWarehouseExeption {
        ArrayList<Item> allItems = getAllWarehouseItems();

        if(allItems == null){
            throw new ItemIsNotInWarehouseExeption();
        }

        for (Item item : allItems){
            if(item.getItemName().equals(soldItem.getItemName())){
                item.updateQuantity(quantity);
            }
        }

        saveWarehouseDatabase(allItems);
    }

    private void saveWarehouseDatabase(ArrayList<Item> items) throws IOException {
        PrintWriter printWriter = new PrintWriter(new FileWriter(path));

        for (Item item : items) {
            printWriter.println(item.getItemName());
            printWriter.println(item.getPurchasePrice());
            printWriter.println(item.getSalePrice());
            printWriter.println(item.getCurrentQuantity());
            printWriter.println();
        }

        printWriter.close();
    }


}
