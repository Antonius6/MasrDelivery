package org.example;

public class RiderDashboardListener implements OrderObserver {
    @Override
    public void onOrderStatusChanged(Order order, OrderStatus oldStatus, OrderStatus newStatus) {
        if (newStatus == OrderStatus.ASSIGNED || newStatus == OrderStatus.READY) {
            System.out.println("[Rider Dashboard] Order " + order.getOrderId() + " updated to " + newStatus);
        }
    }
}
