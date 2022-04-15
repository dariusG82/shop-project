package dariusG82.services.accounting;

import java.time.LocalDate;

public class SalesCashRecord extends CashRecord {
    private final String recordID;

    public SalesCashRecord(int recordNr, LocalDate date, double amount) {
        super(date, CashOperation.DAILY_INCOME, amount);
        this.recordID = "SF " + recordNr;
    }

    public SalesCashRecord(String recordID, LocalDate date, double amount) {
        super(date, CashOperation.DAILY_INCOME, amount);
        this.recordID = recordID;
    }


    public String getRecordID(){
        return recordID;
    }
}
