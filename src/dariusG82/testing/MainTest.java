package dariusG82.testing;

import dariusG82.classes.services.sql_lite_services.DataFromSQLiteService;

public class MainTest {

    public static void main(String[] args) {
        SQLTests tests = new SQLTests();
        DataFromSQLiteService sqLiteService = new DataFromSQLiteService();

//        System.out.println(tests.getUserPassword("IrmIrm"));
        System.out.println(sqLiteService.getAllUsers());
        System.out.println(sqLiteService.getAllClients());
    }
}
