package dariusG82.classes.data.interfaces;

import dariusG82.classes.custom_exeptions.ClientDoesNotExistExeption;
import dariusG82.classes.custom_exeptions.WrongDataPathExeption;
import dariusG82.classes.partners.Client;

import java.io.IOException;
import java.sql.SQLException;

public interface BusinessInterface {

    void addNewClientToDatabase(Client client) throws IOException, WrongDataPathExeption, SQLException;
    void deleteClientFromDatabase(Client client) throws WrongDataPathExeption, ClientDoesNotExistExeption, SQLException;
}
