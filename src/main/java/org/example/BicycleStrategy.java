package org.example;

public class BicycleStrategy implements DispatchStrategy {
    @Override
    public double getMaxDeliveryDistanceKm() {
        return 5.0;
    }

    @Override
    public double getMaxOrderWeightKg() {
        return 3.0;
    }

    @Override
    public double getAverageSpeedKmPh() {
        return 15.0;
    }

    @Override
    public boolean canHandleOrder(double distanceKm, double orderWeightKg) {
        return distanceKm<=getMaxDeliveryDistanceKm() && orderWeightKg<=getMaxOrderWeightKg();
    }
}
