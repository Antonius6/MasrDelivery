package org.example;

public class CarStrategy implements DispatchStrategy{
    @Override
    public double getMaxDeliveryDistanceKm() {
        return 30.0;
    }

    @Override
    public double getMaxOrderWeightKg() {
        return 20.0;
    }

    @Override
    public double getAverageSpeedKmPh() {
        return 120.0;
    }

    @Override
    public boolean canHandleOrder(double distanceKm, double orderWeightKg) {
        return distanceKm<= getMaxDeliveryDistanceKm() && orderWeightKg<= getMaxOrderWeightKg();
    }
}
