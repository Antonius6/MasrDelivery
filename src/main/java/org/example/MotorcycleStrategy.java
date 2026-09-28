package org.example;

public class MotorcycleStrategy implements DispatchStrategy{
    @Override
    public double getMaxDeliveryDistanceKm() {
        return 15.0;
    }

    @Override
    public double getMaxOrderWeightKg() {
        return 10.0 ;
    }

    @Override
    public double getAverageSpeedKmPh() {
        return 75.0;
    }

    @Override
    public boolean canHandleOrder(double distanceKm, double orderWeightKg) {
        return distanceKm <= getMaxDeliveryDistanceKm() && orderWeightKg <= getMaxOrderWeightKg();
    }
}
