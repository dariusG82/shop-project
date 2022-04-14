package dariusG82.clients;

public class Address {

    public String street;
    public int houseNr;
    public int roomNr;
    public String city;
    public String country;
    private final boolean addressHasRoom;

    public Address(String street, int houseNr, int roomNr, String city, String country) {
        this.street = street;
        this.houseNr = houseNr;
        this.roomNr = roomNr;
        this.city = city;
        this.country = country;
        addressHasRoom = true;
    }

    public Address(String street, int houseNr, String city, String country) {
        this.street = street;
        this.houseNr = houseNr;
        this.roomNr = 0;
        this.city = city;
        this.country = country;
        addressHasRoom = false;
    }

    public String getStreet() {
        return street;
    }

    public int getHouseNr() {
        return houseNr;
    }

    public int getRoomNr() {
        return roomNr;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    @Override
    public String toString() {
        return "Address: " + street + " street " + houseNr +
                printRoomNr() +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                ", addressHasRoom=" + addressHasRoom +
                '}';
    }

    private String printRoomNr(){
        return addressHasRoom ? "-" + roomNr : "";
    }
}
