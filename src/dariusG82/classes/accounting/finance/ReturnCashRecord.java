package dariusG82.classes.accounting.finance;

import java.time.LocalDate;
import java.util.Objects;

public class ReturnCashRecord extends CashRecord {

    private final String recordID;


    public ReturnCashRecord(int recordNr, LocalDate date, double amount, String sellerUsername) {
        super(date, CashOperation.DAILY_EXPENSE, amount, sellerUsername);
        this.recordID = "RE " + recordNr;
    }

    public ReturnCashRecord(String recordID, LocalDate date, double amount, String sellerUsername) {
        super(date, CashOperation.DAILY_EXPENSE, amount, sellerUsername);
        this.recordID = recordID;
    }

    public String getRecordID() {
        return recordID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReturnCashRecord that = (ReturnCashRecord) o;
        return Objects.equals(recordID, that.recordID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(recordID);
    }
}
