package dariusG82.classes.data.interfaces;

import dariusG82.classes.custom_exeptions.WrongDataPathExeption;

import java.io.IOException;

public interface AccountingInterface {
    void countIncomeAndExpensesByDays() throws WrongDataPathExeption, IOException;
}
