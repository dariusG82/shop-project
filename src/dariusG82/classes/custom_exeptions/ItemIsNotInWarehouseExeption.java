package dariusG82.classes.custom_exeptions;

public class ItemIsNotInWarehouseExeption extends Exception {
    public ItemIsNotInWarehouseExeption() {
        super("Item is not in warehouse");
    }
}
