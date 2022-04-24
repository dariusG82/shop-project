package dariusG82.classes.accounting.orders;

public class SalesOrderLine extends OrderLine {

    public SalesOrderLine(int orderNr, String itemName, int lineQuantity, double unitPrice, String salesmanID) {
        super(orderNr, itemName, lineQuantity, unitPrice, salesmanID);
    }

    public void updateQuantity(int returnedQuantity){
        this.lineQuantity -= returnedQuantity;
    }
}
