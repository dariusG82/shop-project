package dariusG82.classes.data.interfaces;

import dariusG82.classes.custom_exeptions.UserNotFoundException;
import dariusG82.classes.users.User;
import dariusG82.classes.users.UserType;

import java.io.IOException;
import java.sql.SQLException;

public interface AdminInterface {

    boolean isUsernameUnique(String username) throws SQLException;
    void addNewUser(User user) throws IOException, SQLException;
    void removeUser(String username) throws UserNotFoundException, IOException, SQLException;
    User getUserByType(String username, String password, UserType type) throws UserNotFoundException, SQLException;
    User getUserByUsername(String username) throws SQLException;

}
