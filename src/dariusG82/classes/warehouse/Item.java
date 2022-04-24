package dariusG82.classes.warehouse;

import java.util.Objects;
import java.util.Random;

public class Item {
    protected final String itemName;
    protected final double purchasePrice;
    protected double salePrice;
    protected int currentQuantity;

    public Item(String itemName){
        this.itemName = itemName;
        this.purchasePrice = 0.0;
        this.salePrice = 0.0;
        this.currentQuantity = 0;
    }

    public Item(String itemName, double purchasePrice, int currentQuantity) {
        this.itemName = itemName;
        this.purchasePrice = purchasePrice;
        this.salePrice = setSalePrice();
        this.currentQuantity = currentQuantity;
    }

    public Item(String itemName, double purchasePrice, double salePrice, int currentQuantity) {
        this.itemName = itemName;
        this.purchasePrice = purchasePrice;
        this.salePrice = salePrice;
        this.currentQuantity = currentQuantity;
    }

    public String getItemName() {
        return itemName;
    }

    public double getPurchasePrice() {
        return purchasePrice;
    }

    public double getSalePrice() {
        return salePrice;
    }

    public int getCurrentQuantity() {
        return currentQuantity;
    }

    public void updateQuantity(int amount){
        currentQuantity += amount;
    }

    private double setSalePrice() {
        Random random = new Random();
        return Math.round((getPurchasePrice() * random.nextDouble(1.15, 1.6))*100.0)/100.0;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return Double.compare(item.purchasePrice, purchasePrice) == 0 && Double.compare(item.salePrice, salePrice) == 0 && Objects.equals(itemName, item.itemName);
    }
}
