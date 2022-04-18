package dariusG82.services.partners;

import dariusG82.services.partners.helpers.PartnerDoesNotExistExeption;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class BusinessService {

    private final String path = "src/dariusG82/services/data/clients.txt";

    public void addNewBusinessPartner(BusinessPartner partner) throws IOException {
        ArrayList<BusinessPartner> partners = getPartners();

        assert partners != null;
        partners.add(partner);

        writeDataToFile(partners);
    }

    public BusinessPartner getPartnerByName(String name) throws PartnerDoesNotExistExeption{
        ArrayList<BusinessPartner> partners = getPartners();

        assert partners != null;
        for (BusinessPartner partner : partners){
            if(partner.getPartnerName().equals(name)){
                return partner;
            }
        }

        throw new PartnerDoesNotExistExeption();
    }

    public void deletePartner(BusinessPartner partner) {
        ArrayList<BusinessPartner> partners = getPartners();

        assert partners != null;
        partners.remove(partner);

        writeDataToFile(partners);
    }

    private ArrayList<BusinessPartner> getPartners(){
        try {
            Scanner scanner = new Scanner(new File(path));
            ArrayList<BusinessPartner> partners = new ArrayList<>();

            while (scanner.hasNext()) {
                String partnerName = scanner.nextLine();
                String businessID = scanner.nextLine();
                String streetAddress = scanner.nextLine();
                String city = scanner.nextLine();
                String country = scanner.nextLine();
                scanner.nextLine();

                partners.add(new BusinessPartner(partnerName, businessID, streetAddress, city, country));
            }

            return partners;
        } catch (FileNotFoundException e) {
            return null;
        }
    }

    private void writeDataToFile(ArrayList<BusinessPartner> partners) {
        try {
            PrintWriter printWriter = new PrintWriter(new FileWriter(path));

            for (BusinessPartner partner : partners) {
                printWriter.println(partner.getPartnerName());
                printWriter.println(partner.getBusinessID());
                printWriter.println(partner.getStreetAddress());
                printWriter.println(partner.getCity());
                printWriter.println(partner.getCountry());
                printWriter.println();
            }

            printWriter.close();
        } catch (IOException e){
            // TODO Catch Exception - create new file
        }

    }
}
