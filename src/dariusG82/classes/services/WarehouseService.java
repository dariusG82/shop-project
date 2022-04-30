package dariusG82.classes.services;

import dariusG82.classes.accounting.orders.PurchaseOrderLine;
import dariusG82.classes.custom_exeptions.ItemIsNotInWarehouseExeption;
import dariusG82.classes.custom_exeptions.PurchaseOrderDoesNotExistExeption;
import dariusG82.classes.custom_exeptions.WrongDataPathExeption;
import dariusG82.classes.warehouse.Item;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class WarehouseService extends Service {

    public int getNewPurchaseOrderNumber() throws WrongDataPathExeption, IOException {
        int documentNr = getInfoFromDataString(PURCHASE_ORDER_NR_INFO);

        if (documentNr > 0) {
            return documentNr;
        } else {
            throw new WrongDataPathExeption();
        }
    }

    public void receiveGoods(int purchaseOrder) throws PurchaseOrderDoesNotExistExeption, IOException {
        ArrayList<PurchaseOrderLine> purchaseOrdersLines = dataService.getPurchaseOrderLines();
        ArrayList<Item> allItems = dataService.getAllWarehouseItems();

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

        dataService.updatePurchaseOrderLines(updatedOrders);
        dataService.saveWarehouseStock(uniqueItems);
    }

    public Item getItemFromWarehouse(String itemName) throws ItemIsNotInWarehouseExeption {
        ArrayList<Item> allItems = dataService.getAllWarehouseItems();

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

    public void updateWarehouseStock(Item soldItem, int quantity) throws IOException, ItemIsNotInWarehouseExeption {
        ArrayList<Item> allItems = dataService.getAllWarehouseItems();

        if (allItems == null) {
            throw new ItemIsNotInWarehouseExeption();
        }
        boolean itemQuantityUpdated = false;
        for (Item item : allItems) {
            if (item.getItemName().equals(soldItem.getItemName()) && item.getSalePrice() == soldItem.getSalePrice()) {
                item.updateQuantity(quantity);
                itemQuantityUpdated = true;
            }
        }
        if(!itemQuantityUpdated){
            allItems.add(soldItem);
        }
        dataService.saveWarehouseStock(allItems);
    }

    private ArrayList<Item> getUniqueItemsList(ArrayList<Item> items) {
        ArrayList<Item> uniqueItems = new ArrayList<>();

        for (Item item : items) {
            if(item.getCurrentQuantity() > 0){
                int index = uniqueItems.indexOf(item);
                if (index != -1) {
                    uniqueItems.get(index).updateQuantity(item.getCurrentQuantity());
                } else {
                    uniqueItems.add(item);
                }
            }
        }
        return uniqueItems;
    }
}
