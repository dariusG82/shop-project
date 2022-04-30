package dariusG82.classes.services;

import dariusG82.classes.custom_exeptions.ClientDoesNotExistExeption;
import dariusG82.classes.custom_exeptions.WrongDataPathExeption;
import dariusG82.classes.partners.Client;

import java.io.IOException;
import java.util.ArrayList;

public class BusinessService extends Service {

    public void addNewClientToDatabase(Client client) throws IOException, WrongDataPathExeption {
        ArrayList<Client> clients = dataService.getAllClients();

        if (clients != null) {
            clients.add(client);
            dataService.updateClientsDatabase(clients);
        } else {
            throw new WrongDataPathExeption();
        }
    }

    public boolean isClientInDatabase(String clientName) {
        ArrayList<Client> clients = dataService.getAllClients();

        if (clients == null) {
            return false;
        }
        for (Client client : clients) {
            if (client.clientName().equals(clientName)) {
                return true;
            }
        }
        return false;
    }



    public void deleteClientFromDatabase(Client clientToDelete) throws WrongDataPathExeption {
        ArrayList<Client> clients = dataService.getAllClients();


        if (clients != null) {
            for (Client client : clients) {
                if (client.equals(clientToDelete)) {
                    clients.remove(clientToDelete);
                    break;
                }
            }
            dataService.updateClientsDatabase(clients);
        } else {
            throw new WrongDataPathExeption();
        }
    }

}
