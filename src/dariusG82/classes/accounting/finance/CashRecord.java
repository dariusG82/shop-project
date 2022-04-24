package dariusG82.classes.accounting.finance;

import java.time.LocalDate;

public class CashRecord {

    private final LocalDate date;
    private final CashOperation cashOperation;
    private double amount;
    private final String sellerUsername;

    public CashRecord(LocalDate date, CashOperation cashOperation, double amount, String sellerUsername) {
        this.date = date;
        this.cashOperation = cashOperation;
        this.amount = amount;
        this.sellerUsername = sellerUsername;

    }

    public void updateAmount(double newAmount){
        amount += newAmount;
    }

    public LocalDate getDate() {
        return date;
    }
    public CashOperation getOperation() {
        return cashOperation;
    }
    public double getAmount() {
        return amount;
    }

    public String getSellerUsername() {
        return sellerUsername;
    }
}
