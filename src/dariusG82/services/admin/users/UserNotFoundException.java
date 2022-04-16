package dariusG82.services.admin.users;

public class UserNotFoundException  extends Exception{

    public UserNotFoundException(){
        super("User cannot be found");
    }
}
