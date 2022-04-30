package dariusG82.classes.accounting.orders;

import dariusG82.classes.partners.Client;

import java.util.ArrayList;

public class SalesOrder extends Order {

    private final ArrayList<SalesOrderLine> soldItems;
    private double totalOrderAmount;
    private Client client;

    public SalesOrder(int orderID) {
        super(orderID);
        this.soldItems = new ArrayList<>();
    }

    public Client getClient(){
        return client;
    }

    public void setClient(Client client){
        this.client = client;
    }
    @Override
    public ArrayList<SalesOrderLine> getOrderItems() {
        return soldItems;
    }

    public void addSalesOrderLineToOrder(SalesOrderLine salesOrderLine) {
        this.soldItems.add(salesOrderLine);
    }

    @Override
    public double getTotalOrderAmount() {
        for (SalesOrderLine salesOrderLine : soldItems) {
            totalOrderAmount += salesOrderLine.getLineQuantity() * salesOrderLine.getUnitPrice();
        }
        return totalOrderAmount;
    }
}
