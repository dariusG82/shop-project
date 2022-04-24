package dariusG82.classes.services;

import dariusG82.classes.admin.users.User;
import dariusG82.classes.custom_exeptions.UserNotFoundException;
import dariusG82.classes.admin.users.UserType;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class AdminService {

    private final String usersPath = "src/dariusG82/services/data/users.txt";

    public ArrayList<User> getAllUsers() {
        try {
            Scanner scanner = new Scanner(new File(usersPath));
            ArrayList<User> users = new ArrayList<>();

            while (scanner.hasNext()) {
                String name = scanner.nextLine();
                String surname = scanner.nextLine();
                String username = scanner.nextLine();
                String password = scanner.nextLine();
                UserType type = getUserType(scanner);
                if (type != null) {
                    User user = new User(name, surname, username, password, type);
                    users.add(user);
                }
                scanner.nextLine();
            }

            return users;
        } catch (FileNotFoundException e) {
            return null;
        }
    }

    public boolean isUsernameUnique(String username) {
        ArrayList<User> users = getAllUsers();
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return false;
            }
        }
        return true;
    }

    public void addNewUser(User user) throws IOException {
        ArrayList<User> users = getAllUsers();

        users.add(user);

        writeUsersToFile(users);
    }

    public void removeUser(String username) throws UserNotFoundException, IOException {
        ArrayList<User> users = getAllUsers();

        if(isUserExist(username)){
            users.removeIf(user -> user.getUsername().equals(username));
            writeUsersToFile(users);
            return;
        }

        throw new UserNotFoundException();
    }

    private void writeUsersToFile(ArrayList<User> users) throws IOException {
        PrintWriter printWriter = new PrintWriter(new FileWriter(usersPath));

        for (User user : users) {
            printWriter.println(user.getName());
            printWriter.println(user.getSurname());
            printWriter.println(user.getUsername());
            printWriter.println(user.getPassword());
            printWriter.println(user.getUserType());
            printWriter.println();
        }

        printWriter.close();
    }

    private User getUser(String username) throws UserNotFoundException {
        ArrayList<User> users = getAllUsers();

        for(User user : users){
            if(user.getUsername().equals(username)){
                return user;
            }
        }
        throw new UserNotFoundException();
    }

    private UserType getUserType(Scanner scanner) {
        String type = scanner.nextLine();
        try {
            return UserType.valueOf(type);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private boolean isUserExist(String username) {
        ArrayList<User> users = getAllUsers();

        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

    public User getUserByType(String username, String password, UserType type) throws UserNotFoundException {
        ArrayList<User> users = getAllUsers();

        for(User user : users){
            if(user.getUserType().equals(type) &&
                    user.getUsername().equals(username) &&
                    user.getPassword().equals(password)){
                return user;
            }
        }
        throw new UserNotFoundException();
    }
}
