package dariusG82.services.accounting.finance;

import java.time.LocalDate;

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
}
