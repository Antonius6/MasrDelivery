package org.example;

import java.util.Objects;

public abstract class MenuItem {


    private final String name;
    private final String itemId;
    private static int count = 0;
    private double price;
    private int preTime;
    private String category;
    private boolean availability;

    public double getStock() {
        return stock;
    }



    private double stock;

    MenuItem(String name, double price,int preTime, String category, boolean availability) {
        if (price <= 0) {
            throw new IllegalArgumentException("price must be greater than zero");
        }
        this.name = name;
        this.itemId = "ITEM_" + count++;
        this.price = price;
        this.preTime = preTime;
        this.category = category;
        this.availability = availability;
    }

    public String getName() {
        return name;
    }

    public String getItemId() {
        return itemId;
    }

    public static int getCount() {
        return count;
    }

    public double getPrice() {
        return price;
    }

    public int getPreTime() {
        return preTime;
    }

    public String getCategory() {
        return category;
    }

    public boolean isAvailability() {
        return availability;
    }

    public abstract double calculatePrice(double quantity);



    public void setStock(double stock) {

        if(stock < 0){
            throw new IllegalArgumentException("stock cannot be negative");
        }
        this.stock = stock;
    }

    public boolean hasEnoughStock(double quantity) {
        if (quantity < 0){
            throw new IllegalArgumentException("quantity cannot be negative");
        }
        return stock >= quantity;
    }

    public void deductStock(double quantity) {
        if (!hasEnoughStock(quantity)){
            throw new StockShortageException ("Not enough stock for item " + name+"Available"+stock);
        }
        stock -= quantity;
    }

    void setAvailability(boolean availability) {
        this.availability = availability;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        MenuItem menuItem = (MenuItem) o;
        return Objects.equals(itemId, menuItem.itemId);
    }
    @Override
    public String toString() {
        return name + " | " + price + " EGP | " + category +
                " | " + (availability ? "Available" : "Unavailable");
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(itemId);
    }
}
