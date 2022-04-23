package dariusG82.services.accounting.orders;

import dariusG82.services.warehouse.items.Item;

public class PurchaseOrder {

    public int orderNr;
    public Item item;
    private boolean isFinished;

    public PurchaseOrder(int orderNr, Item item) {
        this.orderNr = orderNr;
        this.item = item;
        isFinished = false;
    }

    public int getOrderNr() {
        return orderNr;
    }

    public Item getItem() {
        return item;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void setFinished(boolean finished) {
        isFinished = finished;
    }
}
