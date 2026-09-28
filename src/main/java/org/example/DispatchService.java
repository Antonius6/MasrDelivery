package org.example;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

public class DispatchService {
    private final PriorityQueue<Order> pendingDispatchQueue = new PriorityQueue<>(
            Comparator.comparing((Order o) -> o.getCustomer().getTier() == CustomerTier.GOLD).reversed()
                    .thenComparing(Order::getPlacedAt)
    );

    public void addReadyOrder(Order order) {
        if (order.getStatus() != OrderStatus.READY) {
          throw new OrderNotReadyException("Order " + order.getOrderId() + " is already in not ready state");
        }
        pendingDispatchQueue.add(order);
    }

    public Order getReadyOrder() {
        return pendingDispatchQueue.poll();
    }


}
