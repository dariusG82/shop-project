package dariusG82.classes.custom_exeptions;

public class PartnerDoesNotExistExeption extends Exception{

    public PartnerDoesNotExistExeption(){
        super("Business Partner you looking for do not exists in database");
    }
}
