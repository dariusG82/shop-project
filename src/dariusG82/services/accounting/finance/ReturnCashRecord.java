package dariusG82.services.accounting.finance;

import java.time.LocalDate;

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
}
