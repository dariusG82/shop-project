package dariusG82.services.partners;

import dariusG82.services.custom_exeptions.PartnerDoesNotExistExeption;
import dariusG82.services.custom_exeptions.WrongDataPathExeption;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class BusinessService {

    private final String path = "src/dariusG82/services/data/clients.txt";

    public void addNewBusinessPartner(BusinessPartner partner) throws IOException, WrongDataPathExeption {
        ArrayList<BusinessPartner> partners = getPartners();

        if (partners != null) {
            partners.add(partner);
            writeDataToFile(partners);
        } else {
            throw new WrongDataPathExeption();
        }
    }

    public boolean isClientInDatabase(String partnerName) {
        ArrayList<BusinessPartner> partners = getPartners();

        if (partners == null) {
            return false;
        }
        for (BusinessPartner partner : partners) {
            if (partner.getPartnerName().equals(partnerName)) {
                return true;
            }
        }
        return false;
    }

    public BusinessPartner getPartnerByName(String name) throws PartnerDoesNotExistExeption, WrongDataPathExeption {
        ArrayList<BusinessPartner> partners = getPartners();

        if (partners != null) {
            for (BusinessPartner partner : partners) {
                if (partner.getPartnerName().equals(name)) {
                    return partner;
                }
            }
        } else {
            throw new WrongDataPathExeption();
        }
        throw new PartnerDoesNotExistExeption();
    }

    public void deletePartner(BusinessPartner partner) throws WrongDataPathExeption {
        ArrayList<BusinessPartner> partners = getPartners();


        if (partners != null) {
            for (BusinessPartner businessPartner : partners) {
                if (businessPartner.equals(partner)) {
                    partners.remove(partner);
                    break;
                }
            }
            writeDataToFile(partners);
        } else {
            throw new WrongDataPathExeption();
        }
    }

    private ArrayList<BusinessPartner> getPartners() {
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
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }
}
