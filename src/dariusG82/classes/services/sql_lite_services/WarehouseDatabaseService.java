package dariusG82.classes.services.sql_lite_services;

import dariusG82.classes.data.interfaces.WarehouseInterface;
import dariusG82.classes.warehouse.Item;

import java.sql.*;

import static dariusG82.classes.services.sql_lite_services.SQL_Query.GET_LAST_PURCHASE_ORDER_NR;

public class WarehouseDatabaseService extends SQLService implements WarehouseInterface {
    @Override
    public int getNewPurchaseOrderNumber() throws SQLException {
        String query = GET_LAST_PURCHASE_ORDER_NR.getQuery();

        Connection connection = this.connectToDB();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);

        return resultSet.getInt(1) + 1;
    }

    @Override
    public void receiveGoods(int purchaseOrder) throws SQLException {
        ResultSet resultSet = getPurchaseOrderLines(purchaseOrder);
        int itemId = getNewItemId();

        if(resultSet == null || itemId == 0){
            throw new SQLException();
        }

        while (resultSet.next()){
            String itemName = resultSet.getString("itemName");
            double newPurchasePrice = resultSet.getDouble("itemPrice");
            int newQuantity = resultSet.getInt("itemQuantity");

            Item item = getItemFromWarehouse(resultSet.getString("itemName"));
            itemId = getNewItemId();

            if(item == null || item.getPurchasePrice() != newPurchasePrice){
                item = new Item(itemName, newPurchasePrice, newQuantity);
                addNewItemToWarehouse(itemId, item);
            } else {
                updateWarehouseStock(item, newQuantity);
            }
        }
    }

    @Override
    public Item getItemFromWarehouse(String itemName) {
        String query = "SELECT * FROM warehouse WHERE itemName =?";

        try {
            Connection connection = this.connectToDB();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, itemName);
            ResultSet resultSet = statement.executeQuery();

            double purchasePrice = resultSet.getDouble("itemPurchasePrice");
            double salePrice = resultSet.getDouble("itemSalePrice");
            int quantity = resultSet.getInt("itemQuantity");

            return new Item(itemName, purchasePrice, salePrice, quantity);
        } catch (SQLException e){
            return null;
        }
    }

    @Override
    public void updateWarehouseStock(Item item, int quantity) throws SQLException {
//        if(!(item instanceof ReturnedItem)){
//            quantity = -quantity;
//        }

        Integer currentItemQuantity = getCurrentItemQuantity(item.getItemName());

        if(currentItemQuantity == null){
            throw new SQLException();
        }

        int newQuantity = currentItemQuantity + quantity;

        String query = "UPDATE warehouse SET itemQuantity = ?, itemSalePrice = ? WHERE itemName = ?";

        Connection connection = this.connectToDB();
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, newQuantity);
        statement.setDouble(2, item.getSalePrice());
        statement.setString(3, item.getItemName());
        statement.executeUpdate();
    }

    private Integer getCurrentItemQuantity(String itemName){
        String query = "SELECT itemQuantity FROM warehouse WHERE itemName = ?";

        Connection connection = this.connectToDB();
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, itemName);
            ResultSet resultSet = statement.executeQuery();

            return resultSet.getInt("itemQuantity");
        } catch (SQLException e) {
            return null;
        }
    }

    private ResultSet getPurchaseOrderLines(int orderNr) {
        String query = "SELECT * FROM order_lines WHERE orderId = ? AND orderType = 'PURCHASE'";

        Connection connection = this.connectToDB();
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, orderNr);
            return statement.executeQuery();
        } catch (SQLException e) {
            return null;
        }
    }

    private int getNewItemId(){
        String query = "SELECT MAX(itemId) FROM warehouse";

        Connection connection = this.connectToDB();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            return resultSet.getInt("itemId") + 1;
        } catch (SQLException e) {
            return 0;
        }
    }

    private void addNewItemToWarehouse(int itemId, Item item) throws SQLException {
        String query = "INSERT INTO warehouse VALUES(?,?,?,?,?)";

        Connection connection = this.connectToDB();
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, itemId);
        statement.setString(2, item.getItemName());
        statement.setInt(3, item.getCurrentQuantity());
        statement.setDouble(4, item.getPurchasePrice());
        statement.setDouble(5, item.getSalePrice());

        statement.executeUpdate();
    }
}
