package dariusG82.services.custom_exeptions;

public class UserNotFoundException  extends Exception{

    public UserNotFoundException(){
        super("User cannot be found");
    }
}
