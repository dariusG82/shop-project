package dariusG82.services.partners;

public class BusinessPartner {
    public String partnerName;
    public String businessID;
    public String streetAddress;
    public String city;
    public String country;

    public BusinessPartner(String partnerName, String businessID, String streetAddress, String city, String country) {
        this.partnerName = partnerName;
        this.businessID = businessID;
        this.streetAddress = streetAddress;
        this.city = city;
        this.country = country;
    }

    public String getPartnerName() {
        return partnerName;
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
