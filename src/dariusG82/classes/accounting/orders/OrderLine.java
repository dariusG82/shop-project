package dariusG82.classes.accounting.orders;

public class OrderLine {

    private final int orderNr;
    private final String clientName;
    private final String itemName;
    protected int lineQuantity;
    private final double unitPrice;
    private final String salesmanID;

    public OrderLine(int orderNr, String clientName, String itemName, int lineQuantity, double unitPrice, String salesmanID) {
        this.orderNr = orderNr;
        this.clientName = clientName;
        this.itemName = itemName;
        this.lineQuantity = lineQuantity;
        this.unitPrice = unitPrice;
        this.salesmanID = salesmanID;
    }

    public OrderLine(int orderNr, String itemName, int lineQuantity, double unitPrice, String salesmanID) {
        this.orderNr = orderNr;
        this.clientName = null;
        this.itemName = itemName;
        this.lineQuantity = lineQuantity;
        this.unitPrice = unitPrice;
        this.salesmanID = salesmanID;
    }

    public double getLineAmount() {
        return getLineQuantity() * getUnitPrice();
    }

    public int getOrderNr() {
        return orderNr;
    }

    public String getClientName() {
        return clientName;
    }

    public String getItemName() {
        return itemName;
    }

    public int getLineQuantity() {
        return lineQuantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public String getSalesmanID() {
        return salesmanID;
    }
}

