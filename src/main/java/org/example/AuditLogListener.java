package org.example;

public class AuditLogListener  implements OrderObserver {
    @Override
    public void onOrderStatusChanged(Order order, OrderStatus oldStatus, OrderStatus newStatus) {
        System.out.println("[Audit Log] " + order.getOrderId() + " transitioned from " + oldStatus + " to " + newStatus);
    }
}
