package org.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ComboItem extends MenuItem{


    private final List<MenuItem> bundledItems;
    ComboItem(String name, double price, int preTime, String category, boolean availability,List<MenuItem> bundledItems) {
        super(name, price, preTime, category, availability);
        this.bundledItems = new ArrayList<>(bundledItems);
    }

    @Override
    public double calculatePrice(double quantity) {
        return getPrice()* quantity;
    }

    public List<MenuItem> getBundledItems() {
        return Collections.unmodifiableList(bundledItems);
    }
}
