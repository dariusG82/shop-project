package dariusG82.services.accounting;

import java.time.LocalDate;

public class CashRecord {

    public LocalDate date;
    public CashOperation cashOperation;
    public double amount;

    public CashRecord(LocalDate date, CashOperation cashOperation, double amount) {
        this.date = date;
        this.cashOperation = cashOperation;
        this.amount = amount;

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


}
