package dariusG82.classes.accounting.orders;

import dariusG82.classes.warehouse.Item;

public class PurchaseOrderLine extends OrderLine {
    protected
    boolean isFinished;
    protected double salePrice;

    public PurchaseOrderLine(int orderNr, Item item){
        super(orderNr, item.getItemName(), item.getCurrentQuantity(), item.getPurchasePrice(), null);
        this.salePrice = item.getSalePrice();
        this.isFinished = false;
    }

    public double getSalePrice() {
        return salePrice;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void setFinished(boolean finished) {
        isFinished = finished;
    }
}
