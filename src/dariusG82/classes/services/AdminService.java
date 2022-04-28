package dariusG82.classes.services;

import dariusG82.classes.custom_exeptions.UserNotFoundException;
import dariusG82.classes.custom_exeptions.WrongDataPathExeption;
import dariusG82.classes.users.User;
import dariusG82.classes.users.UserType;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

public class AdminService extends Service {

    public boolean isUsernameUnique(String username) {
        ArrayList<User> users = dataService.getAllUsers();
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return false;
            }
        }
        return true;
    }

    public void addNewUser(User user) throws IOException {
        ArrayList<User> users = dataService.getAllUsers();

        users.add(user);

        dataService.updateAllUsers(users);
    }

    public void removeUser(String username) throws UserNotFoundException, IOException {
        ArrayList<User> users = dataService.getAllUsers();

        if (isUserExist(username)) {
            users.removeIf(user -> user.getUsername().equals(username));
            dataService.updateAllUsers(users);
            return;
        }

        throw new UserNotFoundException();
    }

    private boolean isUserExist(String username) {
        ArrayList<User> users = dataService.getAllUsers();

        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

    public User getUserByType(String username, String password, UserType type) throws UserNotFoundException {
        ArrayList<User> users = dataService.getAllUsers();

        for (User user : users) {
            if (user.getUserType().equals(type) &&
                    user.getUsername().equals(username) &&
                    user.getPassword().equals(password)) {
                return user;
            }
        }
        throw new UserNotFoundException();
    }

    public void updateCurrentDateInDataString(LocalDate currentDate) throws WrongDataPathExeption, IOException {
        ArrayList<String> datalist = dataService.getDataStrings();

        if (datalist == null) {
            throw new WrongDataPathExeption();
        }

        for (int index = 0; index < datalist.size(); index++) {
            String data = datalist.get(index);
            if (data.startsWith(CURRENT_DATE)) {
                String newData = data.substring(0, data.indexOf("-") + 1) + currentDate;
                datalist.set(index, newData);
                break;
            }
        }
        dataService.updateDataStrings(datalist);
    }
}
