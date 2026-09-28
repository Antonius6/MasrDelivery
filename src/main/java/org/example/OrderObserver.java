package org.example;

public interface OrderObserver {
    void onOrderStatusChanged(Order order, OrderStatus oldStatus, OrderStatus newStatus);
}
