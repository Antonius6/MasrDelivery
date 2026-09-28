package org.example;
public class OrderLineItem {
    private final MenuItem menuItem;
    private final double quantityOrWeight;

    public OrderLineItem(MenuItem menuItem, double quantityOrWeight) {
        this.menuItem = menuItem;
        this.quantityOrWeight = quantityOrWeight;
    }

    public double calculateLineTotal() {
        return menuItem.calculatePrice(quantityOrWeight);
    }

    public MenuItem getMenuItem() { return menuItem; }
    public double getQuantityOrWeight() { return quantityOrWeight; }
}