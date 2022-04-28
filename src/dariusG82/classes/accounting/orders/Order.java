package dariusG82.classes.accounting.orders;

import java.util.ArrayList;

public abstract class Order {

    private final int orderID;

    public Order(int orderID) {
        this.orderID = orderID;
    }

    public int getOrderID() {
        return orderID;
    }

    public abstract double getTotalOrderAmount();

    public abstract ArrayList<? extends OrderLine> getOrderItems();
}
