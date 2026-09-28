package org.example;

public interface DispatchStrategy {

    double getMaxDeliveryDistanceKm();
    double getMaxOrderWeightKg();
    double getAverageSpeedKmPh();
    boolean canHandleOrder(double distanceKm, double orderWeightKg);

}
