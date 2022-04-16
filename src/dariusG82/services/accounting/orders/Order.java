package dariusG82.services.accounting.orders;

import dariusG82.services.warehouse.items.Item;

import java.util.ArrayList;

public abstract class Order {

    public abstract ArrayList<Item> getOrderItems();
}

