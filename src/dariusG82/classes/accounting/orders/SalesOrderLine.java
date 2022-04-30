package dariusG82.classes.accounting.orders;

public class SalesOrderLine extends OrderLine {

    private boolean isPaymentReceived;

    public SalesOrderLine(int orderNr, String clientName, String itemName, int lineQuantity, double unitPrice, String salesmanID) {
        super(orderNr, clientName, itemName, lineQuantity, unitPrice, salesmanID);
        this.isPaymentReceived = false;
    }

    public void updateQuantity(int returnedQuantity) {
        this.lineQuantity -= returnedQuantity;
    }

    public boolean isPaymentReceived() {
        return isPaymentReceived;
    }

    public void setPaymentReceived(boolean paymentReceived) {
        isPaymentReceived = paymentReceived;
    }
}
