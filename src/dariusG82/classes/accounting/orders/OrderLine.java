package dariusG82.classes.accounting.orders;

public class OrderLine {

    public int orderNr;
    public String itemName;
    public int lineQuantity;
    public double unitPrice;
    public String salesmanID;

    public OrderLine(int orderNr, String itemName, int lineQuantity, double unitPrice, String salesmanID) {
        this.orderNr = orderNr;
        this.itemName = itemName;
        this.lineQuantity = lineQuantity;
        this.unitPrice = unitPrice;
        this.salesmanID = salesmanID;
    }

    public double getLineAmount(){
        return getLineQuantity() * getUnitPrice();
    }
    public int getOrderNr() {
        return orderNr;
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

