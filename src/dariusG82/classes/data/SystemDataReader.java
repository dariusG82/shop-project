package dariusG82.classes.data;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class SystemDataReader {

    private final String SYSTEM_DATA_PATH = "src/dariusG82/classes/data/systemData.txt";

    public ArrayList<String> getDataStrings() {
        try {
            Scanner scanner = new Scanner(new File(SYSTEM_DATA_PATH));
            ArrayList<String> dataList = new ArrayList<>();

            while (scanner.hasNext()) {
                String data = scanner.nextLine();
                scanner.nextLine();
                dataList.add(data);
            }

            return dataList;
        } catch (FileNotFoundException e) {
            return null;
        }
    }

    public void updateDataStrings(ArrayList<String> updatedDataStrings) throws IOException {
        PrintWriter printWriter = new PrintWriter(new FileWriter(SYSTEM_DATA_PATH));

        for (String dataString : updatedDataStrings) {
            printWriter.println(dataString);
            printWriter.println();
        }

        printWriter.close();
    }
}
