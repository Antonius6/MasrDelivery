package org.example;

public class PromotionStrategyFactory {

    public  static PromotionStrategy  createStrategy (String type, double value, double capOrFee){
        if (type == null) {
            throw new IllegalArgumentException("Promotion type cannot be null");
        }
        return switch (type.toUpperCase()) {
            case "PERCENTAGE" -> new PercentageDiscount(value, capOrFee);
            case "FIXED" -> new FixedAmountDiscount(value);
            case "FREE_DELIVERY" -> new FreeDelivery(value);
            default -> throw new IllegalArgumentException("Invalid promotion type: " + type);
        };
    }
}
