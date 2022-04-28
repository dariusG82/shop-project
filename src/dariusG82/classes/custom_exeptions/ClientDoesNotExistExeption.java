package dariusG82.classes.custom_exeptions;

public class ClientDoesNotExistExeption extends Exception {

    public ClientDoesNotExistExeption() {
        super("Business Partner you looking for do not exists in database");
    }
}
