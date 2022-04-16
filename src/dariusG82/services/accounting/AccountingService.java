package dariusG82.services.accounting;

import dariusG82.services.accounting.finance.CashOperation;
import dariusG82.services.accounting.finance.CashRecord;
import dariusG82.services.accounting.finance.ReturnCashRecord;
import dariusG82.services.accounting.finance.SalesCashRecord;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

public class AccountingService {
    private int returnDocumentsCount = 0;
    private int salesDocumentCount = 0;
    private final String path = "src/dariusG82/services/data/salesJournal.txt";
//    private ArrayList<CashRecord> fullBalance = new ArrayList<>();

    public void addRecordToBalance(CashOperation cashOperation, LocalDate date, double cashAmount, String sellerUsername) throws IOException {
        ArrayList<CashRecord> allCashRecords = getAllRecords();
        switch (cashOperation){
            case DAILY_INCOME -> {
                int recordNr = getNewSalesDocumentNumber();
                SalesCashRecord salesRecord = new SalesCashRecord(recordNr, date, cashAmount, sellerUsername);
                allCashRecords.add(salesRecord);
            }
            case DAILY_EXPENSE -> {
                int recordNr = getNewReturnDocumentNumber();
                ReturnCashRecord returnRecord = new ReturnCashRecord(recordNr, date, cashAmount, sellerUsername);
                allCashRecords.add(returnRecord);
            }
        }

        rewriteDailyBalance(allCashRecords);
    }

    public ArrayList<CashRecord> getAllRecords() {
        int salesCounter = 0;
        int returnCounter = 0;
        try{
            Scanner scanner = new Scanner(new File(path));
            ArrayList<CashRecord> cashRecords = new ArrayList<>();

            while (scanner.hasNext()){
                String id = scanner.nextLine();
                LocalDate operationDate = getOperationDate(scanner.nextLine());
                CashOperation cashOperation = getCashOperation(scanner.nextLine());
                double amount = getAmount(scanner.nextLine());
                String sellerUsername = scanner.nextLine();
                scanner.nextLine();

                if(operationDate != null && cashOperation != null && amount != 0.0){
                    if(id.startsWith("SF")){
                        cashRecords.add(new SalesCashRecord(id, operationDate, amount, sellerUsername));
                        salesCounter++;
                    }
                    if(id.startsWith("RE")){
                        cashRecords.add(new ReturnCashRecord(id, operationDate, amount, sellerUsername));
                        returnCounter++;
                    }

                }

            }
            setSalesDocumentCount(salesCounter);
            setReturnDocumentsCount(returnCounter);
            return cashRecords;
        } catch (FileNotFoundException e){
            return null;
        }
    }

    private double getAmount(String input) {
        try {
            return Double.parseDouble(input);
        } catch (NumberFormatException e){
            return 0.0;
        }
    }

    private CashOperation getCashOperation(String input) {
        try {
            return CashOperation.valueOf(input);
        } catch (IllegalArgumentException e){
            return null;
        }
    }

    private LocalDate getOperationDate(String input) {
        try {
            int year = Integer.parseInt(input.substring(0,4));
            int month = Integer.parseInt(input.substring(5,7));
            int day = Integer.parseInt(input.substring(8,10));

            return LocalDate.of(year, month, day);
        } catch (NumberFormatException e){
            return null;
        }
    }

    public void rewriteDailyBalance(ArrayList<CashRecord> cashRecords) throws IOException {
        PrintWriter printWriter = new PrintWriter(new FileWriter(path));

        for(CashRecord cashRecord : cashRecords){
            if(cashRecord instanceof SalesCashRecord){
                printWriter.println(((SalesCashRecord) cashRecord).getRecordID());
            } else if (cashRecord instanceof ReturnCashRecord) {
                printWriter.println(((ReturnCashRecord) cashRecord).getRecordID());
            }
            printWriter.println(cashRecord.getDate());
            printWriter.println(cashRecord.getOperation());
            printWriter.println(cashRecord.getAmount());
            printWriter.println();
        }

        printWriter.close();
    }

    public double getDaysBalance(LocalDate date){
        double income = getCashOperationsByTypeAndDay(CashOperation.DAILY_INCOME, date);
        double expense = getCashOperationsByTypeAndDay(CashOperation.DAILY_EXPENSE, date);
        return income - expense;
    }

    public double getMonthBalance(LocalDate date){
        int year = date.getYear();
        int month = date.getMonthValue();
        double income = getCashOperationsByTypeAndMonth(CashOperation.DAILY_INCOME, year, month);
        double expense = getCashOperationsByTypeAndMonth(CashOperation.DAILY_EXPENSE, year, month);
        return income - expense;
    }

    public ArrayList<CashRecord> getDailySaleDocuments(LocalDate date, CashOperation cashOperation){
        ArrayList<CashRecord> allCashRecords = getAllRecords();
        ArrayList<CashRecord> dailyRecords = new ArrayList<>();
        for(CashRecord cashRecord : allCashRecords){
            if(cashRecord.getOperation().equals(cashOperation) && cashRecord.getDate().equals(date)){
                dailyRecords.add(cashRecord);
            }
        }
        return dailyRecords;
    }

    private double getCashOperationsByType(CashOperation operation){
        ArrayList<CashRecord> allCashRecords = getAllRecords();
        double cashSum = 0.0;
        for(CashRecord cashRecord : allCashRecords){
            if(cashRecord.getOperation().equals(operation)){
                cashSum += cashRecord.getAmount();
            }
        }
        return cashSum;
    }

    private double getCashOperationsByTypeAndMonth(CashOperation operation, int year, int month){
        ArrayList<CashRecord> allCashRecords = getAllRecords();
        double cashSum = 0.0;
        for(CashRecord cashRecord : allCashRecords){
            if(cashRecord.getOperation().equals(operation) &&
                    cashRecord.getDate().getYear() == year &&
                    cashRecord.getDate().getMonthValue() == month){
                cashSum += cashRecord.getAmount();
            }
        }
        return cashSum;
    }

    private double getCashOperationsByTypeAndDay(CashOperation operation, LocalDate date){
        ArrayList<CashRecord> allCashRecords = getAllRecords();
        double cashSum = 0.0;
        for(CashRecord cashRecord : allCashRecords){
            if(cashRecord.getOperation().equals(operation) && cashRecord.getDate().equals(date)){
                cashSum += cashRecord.getAmount();
            }
        }
        return cashSum;
    }

    private int getNewSalesDocumentNumber(){
        return ++salesDocumentCount;
    }

    private int getNewReturnDocumentNumber(){
        return ++returnDocumentsCount;
    }

    public double getIncomes() {
        return getCashOperationsByType(CashOperation.DAILY_INCOME);
    }

    public double getExpenses() {
        return getCashOperationsByType(CashOperation.DAILY_EXPENSE);
    }

    public double getBalance() {
        return getIncomes() - getExpenses();
    }

    public int getReturnDocumentsCount() {
        return returnDocumentsCount;
    }

    public int getSalesDocumentCount() {
        return salesDocumentCount;
    }

    private void setReturnDocumentsCount(int returnDocumentsCount) {
        this.returnDocumentsCount = returnDocumentsCount;
    }

    private void setSalesDocumentCount(int salesDocumentCount) {
        this.salesDocumentCount = salesDocumentCount;
    }

    public ArrayList<SalesCashRecord> getSalesReportBySalesperson(String username, int year, int month) {
        ArrayList<SalesCashRecord> recordsBySeller = getSalesRecordsForSeller(username);

        return getSalesRecordsForMonth(recordsBySeller, year, month);
    }

    public double getTotalSalesByReport(ArrayList<SalesCashRecord> records){
        double totalSales = 0.0;
        for (SalesCashRecord record : records){
            totalSales += record.getAmount();
        }
        return totalSales;
    }

    private ArrayList<SalesCashRecord> getSalesRecordsForSeller(String sellerUsername){
        ArrayList<CashRecord> allRecords = getAllRecords();
        ArrayList<SalesCashRecord> salesCashRecords = new ArrayList<>();

        for (CashRecord record : allRecords){
            if(record instanceof SalesCashRecord salesRecord && record.getSellerUsername().equals(sellerUsername)){
                salesCashRecords.add(salesRecord);
            }
        }

        return salesCashRecords;
    }

    private ArrayList<SalesCashRecord> getSalesRecordsForMonth(ArrayList<SalesCashRecord> records, int year, int month){
        ArrayList<SalesCashRecord> recordsForMonth = new ArrayList<>();

        for (SalesCashRecord salesCashRecord : records){
            if(salesCashRecord.getDate().getYear() == year && salesCashRecord.getDate().getMonthValue() == month){
                recordsForMonth.add(salesCashRecord);
            }
        }

        return recordsForMonth;
    }
}
