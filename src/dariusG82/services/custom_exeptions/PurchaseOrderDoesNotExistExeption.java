package dariusG82.services.custom_exeptions;

public class PurchaseOrderDoesNotExistExeption extends Exception{

    public PurchaseOrderDoesNotExistExeption(int number){
        super("Purchase order " + number + " doesn't exist");
    }
}
