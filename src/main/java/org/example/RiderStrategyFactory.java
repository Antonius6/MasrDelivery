package org.example;

public class RiderStrategyFactory {

    public static DispatchStrategy createStrategy(VehicleType vehicleType) {
        if (vehicleType == null) {
            throw new IllegalArgumentException("VehicleType cannot be null");
        }
        return switch (vehicleType) {
            case CAR -> new CarStrategy();
            case MOTORCYCLE -> new MotorcycleStrategy();
            case BICYCLE -> new BicycleStrategy();
            default -> throw new IllegalArgumentException("Unknown VehicleType: " + vehicleType);
        };
    }
}