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
            if (client.getClientName().equals(clientName)) {
                return true;
            }
        }
        return false;
    }

    public Client getClientName(String name) throws ClientDoesNotExistExeption, WrongDataPathExeption {
        ArrayList<Client> clients = dataService.getAllClients();

        if (clients != null) {
            for (Client client : clients) {
                if (client.getClientName().equals(name)) {
                    return client;
                }
            }
        } else {
            throw new WrongDataPathExeption();
        }
        throw new ClientDoesNotExistExeption();
    }

    public void deleteClientFromDatabase(Client partner) throws WrongDataPathExeption {
        ArrayList<Client> clients = dataService.getAllClients();


        if (clients != null) {
            for (Client client : clients) {
                if (client.equals(partner)) {
                    clients.remove(partner);
                    break;
                }
            }
            dataService.updateClientsDatabase(clients);
        } else {
            throw new WrongDataPathExeption();
        }
    }


}
