package dariusG82.classes.accounting.finance;

import java.time.LocalDate;
import java.util.Objects;

public class CashRecord {

    private final String recordID;
    private final LocalDate date;
    private final CashOperation cashOperation;
    private double amount;
    private final String sellerUsername;

    public CashRecord(int recordNr, LocalDate date, CashOperation cashOperation, double amount, String sellerUsername) {
        this.recordID = setRecordID(recordNr, cashOperation);
        this.date = date;
        this.cashOperation = cashOperation;
        this.amount = amount;
        this.sellerUsername = sellerUsername;
    }

    public CashRecord(String recordID, LocalDate date, CashOperation cashOperation, double amount, String sellerUsername) {
        this.recordID = recordID;
        this.date = date;
        this.cashOperation = cashOperation;
        this.amount = amount;
        this.sellerUsername = sellerUsername;
    }

    private String setRecordID(int recordNr, CashOperation cashOperation) {
        if (cashOperation.equals(CashOperation.DAILY_INCOME)) {
            return "SF " + recordNr;
        } else {
            return "RE " + recordNr;
        }
    }

    public String getRecordID() {
        return recordID;
    }

    public void updateAmount(double newAmount) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CashRecord that = (CashRecord) o;
        return Objects.equals(getRecordID(), that.getRecordID());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRecordID());
    }
}
