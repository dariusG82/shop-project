package dariusG82.classes.warehouse;

public class SoldItem extends Item{

    private final String salesPersonUsername;

    public SoldItem(String itemName, int soldQuantity, double salePrice, String salesPersonUsername){
        super(itemName);
        this.currentQuantity = soldQuantity;
        this.salePrice = salePrice;
        this.salesPersonUsername = salesPersonUsername;
    }

    public String getSalesPersonUsername() {
        return salesPersonUsername;
    }
}
