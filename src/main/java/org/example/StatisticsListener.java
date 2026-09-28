package org.example;

public class StatisticsListener implements OrderObserver {
    private int totalDeliveredOrders = 0;
    private int totalCancelledOrders = 0;

    @Override
    public void onOrderStatusChanged(Order order, OrderStatus oldStatus, OrderStatus newStatus) {
        if (newStatus == OrderStatus.DELIVERED) {
            totalDeliveredOrders++;
            System.out.println("[Statistics Log] New order delivered! Total delivered orders: " + totalDeliveredOrders);
        } else if (newStatus == OrderStatus.CANCELLED) {
            totalCancelledOrders++;
            System.out.println("[Statistics Log] Order cancelled. Total cancelled orders: " + totalCancelledOrders);
        }
    }

    public int getTotalDeliveredOrders() {
        return totalDeliveredOrders;
    }

    public int getTotalCancelledOrders() {
        return totalCancelledOrders;
    }

}
