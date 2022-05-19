package dariusG82.classes.data.interfaces;

import dariusG82.classes.custom_exeptions.ItemIsNotInWarehouseExeption;
import dariusG82.classes.custom_exeptions.PurchaseOrderDoesNotExistExeption;
import dariusG82.classes.custom_exeptions.WrongDataPathExeption;
import dariusG82.classes.warehouse.Item;

import java.io.IOException;
import java.sql.SQLException;

public interface WarehouseInterface {

    int getNewPurchaseOrderNumber() throws WrongDataPathExeption, IOException, SQLException;

    void receiveGoods(int purchaseOrder) throws PurchaseOrderDoesNotExistExeption, IOException, SQLException;

    Item getItemFromWarehouse(String itemName) throws ItemIsNotInWarehouseExeption, SQLException;

    void updateWarehouseStock(Item soldItem, int quantity) throws IOException, ItemIsNotInWarehouseExeption, SQLException;
}
