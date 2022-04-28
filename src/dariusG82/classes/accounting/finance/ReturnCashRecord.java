package dariusG82.classes.accounting.finance;

import java.time.LocalDate;

public class ReturnCashRecord extends CashRecord {

    public ReturnCashRecord(int recordNr, LocalDate date, double amount, String sellerUsername) {
        super(recordNr, date, CashOperation.DAILY_EXPENSE, amount, sellerUsername);
    }
}
