package org.example;

public class CustomerNotificationListener implements OrderObserver {
    @Override
    public void onOrderStatusChanged(Order order, OrderStatus oldStatus, OrderStatus newStatus) {
        System.out.println("[Notification] Customer " + order.getCustomer().getCustomerName() +
                ": Your order " + order.getOrderId() + " is now " + newStatus);
    }
}
