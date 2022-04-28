package dariusG82.classes.accounting.finance;

import java.time.LocalDate;

public class SalesCashRecord extends CashRecord {

    public SalesCashRecord(int recordNr, LocalDate date, double amount, String sellerUsername) {
        super(recordNr, date, CashOperation.DAILY_INCOME, amount, sellerUsername);
    }
}
