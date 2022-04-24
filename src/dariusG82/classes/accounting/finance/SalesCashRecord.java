package dariusG82.classes.accounting.finance;

import java.time.LocalDate;
import java.util.Objects;

public class SalesCashRecord extends CashRecord {
    private final String recordID;

    public SalesCashRecord(int recordNr, LocalDate date, double amount, String sellerUsername) {
        super(date, CashOperation.DAILY_INCOME, amount, sellerUsername);
        this.recordID = "SF " + recordNr;
    }

    public SalesCashRecord(String recordID, LocalDate date, double amount, String sellerUsername) {
        super(date, CashOperation.DAILY_INCOME, amount, sellerUsername);
        this.recordID = recordID;
    }


    public String getRecordID(){
        return recordID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SalesCashRecord that = (SalesCashRecord) o;
        return Objects.equals(recordID, that.recordID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(recordID);
    }
}
