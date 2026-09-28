package org.example;

public class FixedAmountDiscount implements PromotionStrategy {
    private final double fixedAmount;
    public FixedAmountDiscount(double fixedAmount) {
        this.fixedAmount = fixedAmount;
    }

    @Override
    public double Discount(double subtotal) {
        return Math.min(subtotal , fixedAmount);
    }
}
