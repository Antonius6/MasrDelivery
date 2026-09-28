package org.example;

public class StandardItem extends MenuItem{
    StandardItem(String name, double price, int preTime, String category, boolean availability) {
        super(name, price, preTime, category, availability);
    }

    @Override
    public double calculatePrice(double quantity) {
        return getPrice()*quantity;
    }
}
