package dariusG82.classes.services.file_services;

import dariusG82.classes.custom_exeptions.UserNotFoundException;
import dariusG82.classes.custom_exeptions.WrongDataPathExeption;
import dariusG82.classes.data.interfaces.AdminInterface;
import dariusG82.classes.data.interfaces.DataManagement;
import dariusG82.classes.data.interfaces.FileReaderInterface;
import dariusG82.classes.users.User;
import dariusG82.classes.users.UserType;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

import static dariusG82.classes.services.file_services.FileService.CURRENT_DATE;

public class AdminFileService implements AdminInterface, FileReaderInterface {

    private final DataManagement dataService;
    public AdminFileService(DataManagement dataService){
        this.dataService = dataService;
    }

    @Override
    public boolean isUsernameUnique(String username) {
        ArrayList<User> users = dataService.getAllUsers();
        for (User user : users) {
            if (user.username().equals(username)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void addNewUser(User user) throws IOException {
        ArrayList<User> users = dataService.getAllUsers();

        users.add(user);

        dataService.updateAllUsers(users);
    }

    @Override
    public void removeUser(String username) throws UserNotFoundException, IOException {
        ArrayList<User> users = dataService.getAllUsers();

        if (isUserInDatabase(username)) {
            users.removeIf(user -> user.username().equals(username));
            dataService.updateAllUsers(users);
            return;
        }

        throw new UserNotFoundException();
    }

    @Override
    public User getUserByType(String username, String password, UserType type) throws UserNotFoundException {
        User user = getUserByUsername(username);

        if(user != null && user.userType().equals(type) && user.password().equals(password)){
            return user;
        }

        throw new UserNotFoundException();
    }

    @Override
    public User getUserByUsername(String username){
        ArrayList<User> users = dataService.getAllUsers();

        for(User user : users){
            if(user.username().equals(username)){
                return user;
            }
        }
        return null;
    }

    public void updateCurrentDateInDataString(LocalDate currentDate) throws WrongDataPathExeption, IOException {
        ArrayList<String> datalist = reader.getDataStrings();

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
        reader.updateDataStrings(datalist);
    }

    private boolean isUserInDatabase(String username) {
        ArrayList<User> users = dataService.getAllUsers();

        for (User user : users) {
            if (user.username().equals(username)) {
                return true;
            }
        }
        return false;
    }
}
