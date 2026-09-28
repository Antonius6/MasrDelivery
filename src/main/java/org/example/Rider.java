package org.example;

import java.util.Objects;

public class Rider {
    private static int counterId = 2340;


    private final String riderId;
    private final String riderName;
    private VehicleType vehicle;
    private CurrentDistrict currentDistrict;
    private int countCompletedDeliveries = 0;
    private boolean available;

    private final DispatchStrategy dispatchStrategy;
    private Order activeOrder;


    public Rider(String riderName, VehicleType vehicle) {

        this.riderName = riderName;
        this.vehicle = vehicle;
        this.riderId = "RIDER-" + counterId++;
        this.dispatchStrategy = RiderStrategyFactory.createStrategy(vehicle);
        this.available = true;

    }

    public static int getCounterId() {
        return counterId;
    }

    public static void setCounterId(int counterId) {
        Rider.counterId = counterId;
    }

    public String getRiderId() {
        return riderId;
    }

    public String getRiderName() {
        return riderName;
    }

    public VehicleType getVehicle() {
        return vehicle;
    }

    public void setVehicle(VehicleType vehicle) {
        this.vehicle = vehicle;
    }

    public CurrentDistrict getCurrentDistrict() {
        return currentDistrict;
    }

    public void setCurrentDistrict(CurrentDistrict currentDistrict) {
        this.currentDistrict = currentDistrict;
    }

    public int getCountCompletedDeliveries() {
        return countCompletedDeliveries;
    }

    public void setCountCompletedDeliveries(int countCompletedDeliveries) {
        this.countCompletedDeliveries = countCompletedDeliveries;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public void incrementCompletedDeliveries() {
        this.countCompletedDeliveries++;
    }


    public void assignOrder(Order order,double distanceKm,double orderWeightKg) {
        if (order == null) {
            throw new IllegalArgumentException("Order cannot be null");
        }
        if (!this.available || this.activeOrder != null) {
            throw new RiderAlreadyBusyException("Rider " + riderName + " is already busy with an active order.");

        }

        if (!this.dispatchStrategy.canHandleOrder(distanceKm, orderWeightKg)) {
            throw new IllegalArgumentException("Rider's vehicle cannot handle this order's distance or weight.");
        }

        this.activeOrder = order;
        this.available = false;
    }




    public void completeOrder() {
        if (this.activeOrder == null) {
            throw new IllegalStateException("Rider has no active order to complete.");
        }

        this.countCompletedDeliveries++;
        this.activeOrder = null;
        this.available = true;
    }
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Rider rider = (Rider) o;
        return Objects.equals(riderId, rider.riderId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(riderId);
    }

    public DispatchStrategy getDispatchStrategy() {
        return dispatchStrategy;
    }
}
