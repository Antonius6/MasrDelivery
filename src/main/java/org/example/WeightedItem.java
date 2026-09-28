package org.example;

public class WeightedItem extends MenuItem {
    WeightedItem(String name, double price, int preTime, String category, boolean availability) {
        super(name, price, preTime, category, availability);
    }

    @Override
    public double calculatePrice(double weightInKg ) {
        return getPrice()*weightInKg;
    }
}
