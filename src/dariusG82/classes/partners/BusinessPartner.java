package dariusG82.classes.partners;

import java.util.Objects;

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

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BusinessPartner partner = (BusinessPartner) o;
        return Objects.equals(partnerName, partner.partnerName) && Objects.equals(businessID, partner.businessID) && Objects.equals(streetAddress, partner.streetAddress) && Objects.equals(city, partner.city) && Objects.equals(country, partner.country);
    }
}
