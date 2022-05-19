package dariusG82.classes.services.sql_lite_services;

import dariusG82.classes.custom_exeptions.UserNotFoundException;
import dariusG82.classes.data.interfaces.AdminInterface;
import dariusG82.classes.users.User;
import dariusG82.classes.users.UserType;

import java.sql.*;

import static dariusG82.classes.services.sql_lite_services.SQL_Query.SELECT_USER_BY_USERNAME;

public class AdminDatabaseService extends SQLService implements AdminInterface {

    @Override
    public boolean isUsernameUnique(String username) throws SQLException {
        String query = SELECT_USER_BY_USERNAME.getQuery() + "'" + username + "'";

        return !getResultSet(query).getString("username").equals(username);
    }

    @Override
    public void addNewUser(User user) throws SQLException {
        String query = "INSERT INTO users VALUES(?,?,?,?,?)";

        Connection conn = this.connectToDB();
        PreparedStatement statement = conn.prepareStatement(query);
        statement.setString(1, user.username());
        statement.setString(2, user.password());
        statement.setString(3, user.name());
        statement.setString(4, user.surname());
        statement.setString(5, user.userType().toString());

        statement.executeUpdate();
    }

    @Override
    public void removeUser(String username) throws UserNotFoundException, SQLException {
        String query = "DELETE FROM users WHERE username = ?";

        if (getUserByUsername(username) == null) {
            throw new UserNotFoundException();
        }

        Connection conn = this.connectToDB();
        PreparedStatement statement = conn.prepareStatement(query);
        statement.setString(1, username);
        statement.executeUpdate();
    }

    @Override
    public User getUserByType(String username, String password, UserType type) throws UserNotFoundException, SQLException {
        String query = "SELECT * FROM users WHERE username = '" + username +
                "' AND password = '" + password +
                "' AND userType = '" + type.toString() + "'";

        ResultSet resultSet = getResultSet(query);
        if (resultSet == null) {
            throw new UserNotFoundException();
        }
        String name = resultSet.getString("name");
        String surname = resultSet.getString("surname");

        return new User(name, surname, username, password, type);
    }

    @Override
    public User getUserByUsername(String username) throws SQLException {
        String query = SELECT_USER_BY_USERNAME.getQuery() + "'" + username + "'";

        ResultSet resultSet = getResultSet(query);
        if (resultSet == null) {
            return null;
        }
        String name = resultSet.getString("name");
        String surname = resultSet.getString("surname");
        String password = resultSet.getString("password");
        UserType type = UserType.valueOf(resultSet.getString("userType"));

        return new User(name, surname, username, password, type);
    }

    private ResultSet getResultSet(String query) throws SQLException {
        Connection connection = this.connectToDB();
        Statement statement = connection.createStatement();
        return statement.executeQuery(query);
    }
}
