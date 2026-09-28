package org.example;

public class PercentageDiscount implements PromotionStrategy {

    private final double percentage;
    private final double maxCap;
    public PercentageDiscount(double percentage, double maxCap) {
        this.percentage = percentage;
        this.maxCap = maxCap;
    }

    @Override
    public double Discount(double subtotal) {
        double discount = subtotal * percentage;
        if (discount > maxCap) {
            discount = Math.min(maxCap, discount);
        }
        return discount;
    }


}
