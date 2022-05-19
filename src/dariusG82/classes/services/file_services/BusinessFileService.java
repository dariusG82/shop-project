package dariusG82.classes.services.file_services;

import dariusG82.classes.custom_exeptions.WrongDataPathExeption;
import dariusG82.classes.data.interfaces.BusinessInterface;
import dariusG82.classes.data.interfaces.DataManagement;
import dariusG82.classes.partners.Client;

import java.io.IOException;
import java.util.ArrayList;

public class BusinessFileService extends FileService implements BusinessInterface {

    private final DataManagement dataService;

    public BusinessFileService(DataManagement dataService) {
        this.dataService = dataService;
    }

    @Override
    public void addNewClientToDatabase(Client client) throws IOException, WrongDataPathExeption {
        ArrayList<Client> clients = dataService.getAllClients();

        if (clients != null) {
            clients.add(client);
            dataService.updateClientsDatabase(clients);
        } else {
            throw new WrongDataPathExeption();
        }
    }

    @Override
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
