package dariusG82.services.partners;

public class BusinessPartner {
    private static int clientID;
    public String clientName;
    public String businessID;
    public String streetAddress;
    public String city;
    public String country;

    public BusinessPartner(String clientName, String businessID, String streetAddress, String city, String country) {
        clientID++;
        this.clientName = clientName;
        this.businessID = businessID;
        this.streetAddress = streetAddress;
        this.city = city;
        this.country = country;
    }

    public static int getClientID() {
        return clientID;
    }

    public String getClientName() {
        return clientName;
    }

    public String getBusinessID() {
        return businessID;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }
}
