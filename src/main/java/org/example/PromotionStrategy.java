package org.example;

public interface PromotionStrategy {
    public double Discount(double amount);
    default boolean appliesToDeliveryFee() {
        return false;
    }
}
