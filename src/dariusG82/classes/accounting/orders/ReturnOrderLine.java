package dariusG82.classes.accounting.orders;

public class ReturnOrderLine extends OrderLine {

    public ReturnOrderLine(int orderNr, String itemName, int returnQuantity, double unitPrice, String salesmanID) {
        super(orderNr, itemName, returnQuantity, unitPrice, salesmanID);
    }
}
