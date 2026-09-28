package org.example;

public enum CustomerTier {
    BRONZE(0,0.0),
    SILVER(10,0.10),
    GOLD(30,1.00);

    private final int minCompletedOrders;
    private final double deliveryDiscountPercentage;

    CustomerTier(int minCompletedOrders, double deliveryDiscountPercentage) {
        this.minCompletedOrders = minCompletedOrders;
        this.deliveryDiscountPercentage = deliveryDiscountPercentage;
    }


    public static CustomerTier fromOrderCount(int count) {
        if (count >= GOLD.minCompletedOrders) {
            return GOLD;
        } else if (count >= SILVER.minCompletedOrders) {
            return SILVER;
        }
        return BRONZE;
    }

    public double calculateDiscountedDeliveryFee(double baseDeliveryFee) {
        return baseDeliveryFee * (1.0 - deliveryDiscountPercentage);
    }

    public int getMinCompletedOrders() {
        return minCompletedOrders;
    }

    public double getDeliveryDiscountPercentage() {
        return deliveryDiscountPercentage;
    }

}
