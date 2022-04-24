package dariusG82.classes.custom_exeptions;

public class NegativeBalanceException extends Exception{
    public NegativeBalanceException(){
        super("You don't have enough money, to make this transaction");
    }
}
