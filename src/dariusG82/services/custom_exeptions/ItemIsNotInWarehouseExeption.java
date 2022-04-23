package dariusG82.services.custom_exeptions;

public class ItemIsNotInWarehouseExeption extends Exception{
    public ItemIsNotInWarehouseExeption(){
        super("Item is not in warehouse");
    }
}
