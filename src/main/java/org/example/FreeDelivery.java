package org.example;

public class FreeDelivery implements PromotionStrategy {

    private final double deliveryFee;

    public FreeDelivery(double deliveryFee) {
        this.deliveryFee = deliveryFee;
    }

    @Override
    public double Discount(double subtotal) {
        return Math.round(deliveryFee);
    }
    @Override
    public boolean appliesToDeliveryFee() {
        return true;
    }
}
