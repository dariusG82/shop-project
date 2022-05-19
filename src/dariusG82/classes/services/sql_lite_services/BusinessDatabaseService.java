package dariusG82.classes.services.sql_lite_services;

import dariusG82.classes.custom_exeptions.ClientDoesNotExistExeption;
import dariusG82.classes.data.interfaces.BusinessInterface;
import dariusG82.classes.partners.Client;

import java.sql.*;

import static dariusG82.classes.services.sql_lite_services.SQL_Query.SELECT_CLIENT_BY_NAME;

public class BusinessDatabaseService extends SQLService implements BusinessInterface {

    @Override
    public void addNewClientToDatabase(Client client) throws SQLException {
        String query = "INSERT INTO clients VALUES(?,?,?,?,?)";

        Connection conn = this.connectToDB();
        PreparedStatement statement = conn.prepareStatement(query);
        statement.setString(1, client.businessID());
        statement.setString(2, client.clientName());
        statement.setString(3, client.streetAddress());
        statement.setString(4, client.city());
        statement.setString(5, client.country());

        statement.executeUpdate();
    }

    @Override
    public void deleteClientFromDatabase(Client client) throws ClientDoesNotExistExeption, SQLException {
        String query = "DELETE FROM clients WHERE clientName = ?";

        if (getClientByName(client.clientName()) == null) {
            throw new ClientDoesNotExistExeption(client.clientName());
        }

        Connection conn = this.connectToDB();
        PreparedStatement statement = conn.prepareStatement(query);
        statement.setString(1, client.clientName());
        statement.executeUpdate();
    }

    private Client getClientByName(String businessName) throws SQLException {
        String query = SELECT_CLIENT_BY_NAME.getQuery() + "'" + businessName + "'";

        Connection conn = this.connectToDB();
        Statement statement = conn.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        if(resultSet == null){
            return null;
        }

        String clientId = resultSet.getString("clientID");
        String clientName = resultSet.getString("clientName");
        String clientStreetAddress = resultSet.getString("clientStreetAddress");
        String clientCityAddress = resultSet.getString("clientCityAddress");
        String clientCountryAddress = resultSet.getString("clientCountryAddress");

        return new Client(clientName, clientId, clientStreetAddress, clientCityAddress, clientCountryAddress);
    }
}
