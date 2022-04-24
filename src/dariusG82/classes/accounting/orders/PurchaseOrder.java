package dariusG82.classes.accounting.orders;

import java.util.ArrayList;

public class PurchaseOrder extends Order {
    protected double totalOrderAmount;
    public ArrayList<PurchaseOrderLine> orderedItems = new ArrayList<>();

    public PurchaseOrder(int orderNr) {
        super(orderNr);
    }

    public void addPurchaseOrderLinesToOrder(PurchaseOrderLine purchaseOrderLine) {
        this.orderedItems.add(purchaseOrderLine);
    }

    @Override
    public double getTotalOrderAmount() {
        for (PurchaseOrderLine purchaseOrderLine : orderedItems) {
            totalOrderAmount += purchaseOrderLine.getLineQuantity() * purchaseOrderLine.getUnitPrice();
        }
        return totalOrderAmount;
    }
}
