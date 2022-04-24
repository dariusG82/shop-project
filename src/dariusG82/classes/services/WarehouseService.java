package dariusG82.classes.services;

import dariusG82.classes.accounting.orders.PurchaseOrderLine;
import dariusG82.classes.custom_exeptions.ItemIsNotInWarehouseExeption;
import dariusG82.classes.custom_exeptions.PurchaseOrderDoesNotExistExeption;
import dariusG82.classes.custom_exeptions.WrongDataPathExeption;
import dariusG82.classes.warehouse.Item;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class WarehouseService extends Service {

    private static final String path = "src/dariusG82/services/data/warehouse.txt";
    private static final String orderPath = "src/dariusG82/services/data/orders/purchaseOrderList.txt";

    public int getNewPurchaseOrderNumber() throws WrongDataPathExeption, IOException {
        int documentNr = getInfoFromDataString(PURCHASE_ORDER_NR_INFO);

        if (documentNr > 0) {
            return documentNr;
        } else {
            throw new WrongDataPathExeption();
        }
    }

    public void addItemToPurchaseOrder(PurchaseOrderLine orderLine) throws IOException {
        PrintWriter printWriter = new PrintWriter(new FileWriter(orderPath, true));

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

    public void receiveGoods(int purchaseOrder) throws PurchaseOrderDoesNotExistExeption, IOException {
        ArrayList<PurchaseOrderLine> purchaseOrdersLines = getPurchaseOrderLines();
        ArrayList<Item> allItems = getAllWarehouseItems();

        if (purchaseOrdersLines == null) {
            throw new PurchaseOrderDoesNotExistExeption(purchaseOrder);
        } else if (allItems == null) {
            throw new FileNotFoundException();
        }

        ArrayList<PurchaseOrderLine> updatedOrders = new ArrayList<>();

        for (PurchaseOrderLine orderLine : purchaseOrdersLines) {
            if (orderLine.getOrderNr() == purchaseOrder) {
                Item item = new Item(orderLine.getItemName(), orderLine.getUnitPrice(), orderLine.getSalePrice(), orderLine.getLineQuantity());
                allItems.add(item);
                orderLine.setFinished(true);
            }
            updatedOrders.add(orderLine);
        }

        ArrayList<Item> uniqueItems = getUniqueItemsList(allItems);

        updatedOrderList(updatedOrders);
        saveWarehouseDatabase(uniqueItems);
    }

    private ArrayList<PurchaseOrderLine> getPurchaseOrderLines() {
        try {
            Scanner scanner = new Scanner(new File(orderPath));
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

    public ArrayList<Item> getAllWarehouseItems() {
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

    private void updatedOrderList(ArrayList<PurchaseOrderLine> orderLines) throws IOException {
        PrintWriter printWriter = new PrintWriter(new FileWriter(orderPath));
        printWriter.close();

        for (PurchaseOrderLine orderLine : orderLines) {
            addItemToPurchaseOrder(orderLine);
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
