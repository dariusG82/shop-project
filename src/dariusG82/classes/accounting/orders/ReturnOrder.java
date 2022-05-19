package dariusG82.classes.accounting.orders;

import dariusG82.classes.partners.Client;

import java.util.ArrayList;

public class ReturnOrder extends Order {
    private final ArrayList<ReturnOrderLine> returnItems;
    private double totalOrderAmount;

    private Client client;

    public ReturnOrder(int orderID) {
        super(orderID);
        this.returnItems = new ArrayList<>();
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    @Override
    public ArrayList<ReturnOrderLine> getOrderItems() {
        return returnItems;
    }

    public void addSalesOrderLineToOrder(ReturnOrderLine returnOrderLine) {
        this.returnItems.add(returnOrderLine);
    }

    @Override
    public double getTotalOrderAmount() {
        for (ReturnOrderLine returnOrderLine : returnItems) {
            totalOrderAmount += returnOrderLine.getLineQuantity() * returnOrderLine.getUnitPrice();
        }
        return totalOrderAmount;
    }

}
