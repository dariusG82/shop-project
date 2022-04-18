package dariusG82.services.partners.helpers;

public class PartnerDoesNotExistExeption extends Exception{

    public PartnerDoesNotExistExeption(){
        super("Business Partner you looking for do not exists in database");
    }
}
