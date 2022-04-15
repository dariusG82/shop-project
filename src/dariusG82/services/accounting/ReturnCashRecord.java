package dariusG82.services.accounting;

import java.time.LocalDate;

public class ReturnCashRecord extends CashRecord {

    private final String recordID;


    public ReturnCashRecord(int recordNr, LocalDate date, double amount) {
        super(date, CashOperation.DAILY_EXPENSE, amount);
        this.recordID = "RE " + recordNr;
    }

    public ReturnCashRecord(String recordID, LocalDate date, double amount) {
        super(date, CashOperation.DAILY_EXPENSE, amount);
        this.recordID = recordID;
    }

    public String getRecordID() {
        return recordID;
    }
}
