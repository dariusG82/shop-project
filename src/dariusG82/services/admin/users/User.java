package dariusG82.services.admin.users;

public class User {

    private final String name;
    private final String surname;
    private final String username;
    private final String password;
    private final UserType userType;

    public User(String name, String surname, String username, String password, UserType userType) {
        this.name = name;
        this.surname = surname;
        this.username = username;
        this.password = password;
        this.userType = userType;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public UserType getUserType() {
        return userType;
    }
}
