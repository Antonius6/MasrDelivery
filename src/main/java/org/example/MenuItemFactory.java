package org.example;

import java.util.List;

public class MenuItemFactory {


    public static MenuItem createItem(String type, String name, double price, int preTime, String category, boolean availability, List<MenuItem> bundledItems) {
        switch (type.toUpperCase()) {
            case "STANDARD" -> {
                return new StandardItem(name, price, preTime, category, availability);
            }
            case "WEIGHTED" -> {
                return new WeightedItem(name, price, preTime, category, availability);
            }
            case "COMBO" -> {
                if(bundledItems == null||bundledItems.isEmpty())
                    throw new IllegalArgumentException("Combo item must contain at least one bundled menu item");
                return new ComboItem(name, price, preTime, category, availability, bundledItems);
            }
            default -> {
                throw new IllegalArgumentException("Invalid menu item type: "+type);
            }
        }
    }

}

