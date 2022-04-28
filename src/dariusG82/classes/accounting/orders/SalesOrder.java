package dariusG82.classes.accounting.orders;

import java.util.ArrayList;

public class SalesOrder extends Order {

    public ArrayList<SalesOrderLine> soldItems;
    protected double totalOrderAmount;

    public SalesOrder(int orderID) {
        super(orderID);
        this.soldItems = new ArrayList<>();
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
