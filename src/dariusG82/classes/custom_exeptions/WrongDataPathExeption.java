package dariusG82.classes.custom_exeptions;

public class WrongDataPathExeption extends Exception {

    public WrongDataPathExeption() {
        super("Path for data file iss wrong or doesn't exists ");
    }
}
