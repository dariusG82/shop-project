package dariusG82.items;

public class Item {
    public int itemID;
    public String itemName;
    public double purchasePrice;

    public Item(int itemID, String itemName, double purchasePrice) {
        this.itemID = itemID;
        this.itemName = itemName;
        this.purchasePrice = purchasePrice;
    }

    public int getItemID() {
        return itemID;
    }

    public String getItemName() {
        return itemName;
    }

    public double getPurchasePrice() {
        return purchasePrice;
    }
}
