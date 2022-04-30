package dariusG82.classes.accounting.orders;

import java.util.ArrayList;

public class PurchaseOrder extends Order {
    private double totalOrderAmount;
    private final ArrayList<PurchaseOrderLine> orderedItems;

    public PurchaseOrder(int orderNr) {
        super(orderNr);
        this.orderedItems = new ArrayList<>();
    }

    @Override
    public ArrayList<PurchaseOrderLine> getOrderItems() {
        return null;
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
