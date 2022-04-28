package dariusG82.classes.accounting.orders;

import java.util.ArrayList;

public class ReturnOrder extends Order {
    public ArrayList<ReturnOrderLine> returnItems;
    protected double totalOrderAmount;

    public ReturnOrder(int orderID) {
        super(orderID);
        this.returnItems = new ArrayList<>();
    }

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
