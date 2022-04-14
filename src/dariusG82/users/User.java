package dariusG82.users;

public class User {

    public String name;
    public String surname;
    public String username;
    public String password;
    public UserType userType;

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
