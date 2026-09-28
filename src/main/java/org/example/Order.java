package org.example;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Order {

    private static int orderCounter = 6450;

    private final String orderId;
    private final Customer customer;
    private final Restaurant restaurant;
    private final String deliveryAddress;
    private final District deliveryDistrict;
    private OrderStatus status;
    private Rider assignedRider;
    private final LocalDateTime placedAt;
    private List<OrderLineItem> lineItems = new ArrayList<>();
    private final List<OrderObserver> listener = new ArrayList<>();
    private  LocalDateTime deliveredAt;
    private final double distanceKm;



    public Promotion getPromotion() {
        return promotion;
    }

    private double subTotal;
    private double deliveryFee;
    private double serviceFee;
    private double discountAmount;
    private double total;
    private Promotion promotion;
    private PlatformConfig  INSTANCE = PlatformConfig.INSTANCE();



    public Order(Builder builder) {
        this.orderId = builder.orderId;
        this.customer = builder.customer;
        this.restaurant = builder.restaurant;
        this.deliveryAddress = builder.deliveryAddress;
        this.deliveryDistrict = builder.deliveryDistrict;
        this.status = builder.status;
        this.lineItems = builder.lineItems != null ? List.copyOf(builder.lineItems) : List.of();
        this.placedAt = builder.placedAt;
        this.assignedRider = builder.assignedRider;
        this.promotion = builder.promotion;
        this.distanceKm = builder.distanceKm;

    }

    static class Builder {

        private String orderId;
        private Customer customer;
        private Restaurant restaurant;
        private String deliveryAddress;
        private District deliveryDistrict;
        private OrderStatus status;
        private Rider assignedRider;
        private LocalDateTime placedAt;
        private List<OrderLineItem> lineItems;
        private LocalDateTime deliveredAt;
        private Promotion promotion;
        private double distanceKm;


        public Builder orderId() {
            this.orderId = "ORDER-" + (orderCounter++);
            return this;
        }

        public Builder customer(Customer customer) {
            if (customer == null) {
                throw new IllegalArgumentException("customer cannot be null");
            }
            this.customer = customer;
            return this;
        }

        public Builder restaurant(Restaurant restaurant) {
            if (restaurant == null) {
                throw new IllegalArgumentException("restaurant cannot be null");
            }
            this.restaurant = restaurant;
            return this;
        }

        public Builder deliveryAddress(String deliveryAddress) {
            if (deliveryAddress == null || deliveryAddress.isBlank()) {
                throw new IllegalArgumentException("deliveryAddress cannot be null or blank");
            }
            this.deliveryAddress = deliveryAddress;
            return this;
        }

        public Builder deliveryDistrict(District deliveryDistrict) {
            this.deliveryDistrict = deliveryDistrict;
            return this;
        }

        public Builder status() {
            this.status = OrderStatus.PLACED;
            return this;
        }

        public Builder assignedRider(Rider assignedRider) {
            this.assignedRider = assignedRider;
            return this;
        }

        public Builder placedAt() {
            this.placedAt = LocalDateTime.now();
            return this;
        }

        public Builder lineItems(List<OrderLineItem> orderLineItems) {
            if (orderLineItems == null || orderLineItems.isEmpty()) {
                throw new IllegalArgumentException("orderLineItems cannot be null or empty");
            }
            this.lineItems = orderLineItems;
            return this;
        }

        public Builder deliveredAt() {
            this.deliveredAt = null;
            return this;
        }
        public Builder promotion(Promotion promotion) {
            this.promotion = promotion;
            return this;
        }

        public Builder distanceKm(double distanceKm) {
            if (distanceKm <= 0) {
                throw new IllegalArgumentException("distanceKm must be greater than 0");
            }
            this.distanceKm = distanceKm;
            return this;
        }


        public Order build(List<OrderObserver> observers) {
            if (customer == null) throw new IllegalArgumentException("Customer is required");
            if (restaurant == null) throw new IllegalArgumentException("Restaurant is required");
            if (deliveryAddress == null || deliveryAddress.isBlank()) throw new IllegalArgumentException("Delivery address is required");
            if (lineItems == null || lineItems.isEmpty()) throw new IllegalArgumentException("Order must have at least one item");
            if (distanceKm <= 0) throw new IllegalArgumentException("distance km must be greater than 0");
            Order order = new Order(this);
            if (observers != null) {
                observers.forEach(order::addObserver);
            }
            return order;

        }
    }

    public static int getOrderCounter() {
        return orderCounter;
    }

    public String getOrderId() {
        return orderId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public District getDeliveryDistrict() {
        return deliveryDistrict;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public Rider getAssignedRider() {
        return assignedRider;
    }

    public LocalDateTime getPlacedAt() {
        return placedAt;
    }

    public List<OrderLineItem> getLineItems() {
        return List.copyOf(this.lineItems);
    }

    public double getSubTotal() {
        return subTotal;
    }

    public double getDeliveryFee() {
        return deliveryFee;
    }

    public double getServiceFee() {
        return serviceFee;
    }

    public double getDiscountAmount() {
        return discountAmount;
    }

    public double getTotal() {
        return total;
    }



    public void calculateTotal(double distanceInKm, Promotion promotion) {
        this.subTotal = round2(lineItems.stream()
                .mapToDouble(OrderLineItem::calculateLineTotal)
                .sum());

        double baseDeliveryFee = INSTANCE.getBaseDeliveryFee();
        if (distanceInKm > INSTANCE.getBaseDeliveryDistanceKm()) {
            baseDeliveryFee += (distanceInKm - INSTANCE.getBaseDeliveryDistanceKm()) * INSTANCE.getExtraFeePerKm();
        }

        this.deliveryFee = round2(this.customer.getTier().calculateDiscountedDeliveryFee(baseDeliveryFee));

        this.serviceFee = round2(subTotal * INSTANCE.getServiceFeeRate());

        this.discountAmount = 0.0;
        if (promotion != null) {
            boolean isFirst = customer.firstTimeCustomer();
            promotion.validatePromotion(subTotal, deliveryDistrict, isFirst);

            if (promotion.waivesDeliveryFee()) {
                this.deliveryFee = 0.0;
            } else {
                this.discountAmount = round2(promotion.calculateDiscount(this.subTotal));
            }
        }

        double calculatedTotal = this.subTotal + this.deliveryFee + this.serviceFee - this.discountAmount;
        this.total = round2(Math.max(0.0, calculatedTotal));
    }

    private static double round2(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(orderId, order.orderId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(orderId);
    }

    public void transitionStatus(OrderStatus newStatus) {
        if (!this.status.canTransition(newStatus)) {
            throw new OrderIllegalTransitions("Order cannot jump from " + this.status + " to " + newStatus);
        }
        OrderStatus oldStatus = this.status;
        this.status = newStatus;
        if (newStatus == OrderStatus.DELIVERED && this.deliveredAt == null) {
            this.deliveredAt =LocalDateTime.now();
        }
        notifyObservers(oldStatus, newStatus);
    }
    public void setAssignedRider(Rider rider) {
        this.assignedRider = rider;
    }


    public void cancelOrder() {
        if (!this.status.canCancel()) {
            throw new OrderCancelException("Order cannot be cancelled because status is " + this.status);
        }
        OrderStatus oldStatus = this.status;
        this.status = OrderStatus.CANCELLED;
        lineItems.forEach(li -> li.getMenuItem().setStock(li.getMenuItem().getStock() + li.getQuantityOrWeight()));
        notifyObservers(oldStatus, OrderStatus.CANCELLED);
    }

    public void addObserver(OrderObserver observer) {
        if (observer != null) {
            listener.add(observer);
        } else {
            throw new IllegalArgumentException("Observer is null");
        }
    }

    public void removeObserver(OrderObserver observer) {
        listener.remove(observer);
    }

    private void notifyObservers(OrderStatus oldStatus, OrderStatus newStatus) {
        for (OrderObserver observer : listener) {
            observer.onOrderStatusChanged(this, oldStatus, newStatus);
        }
    }

    public LocalDateTime getDeliveredAt() {
        return deliveredAt;
    }
    public double getDistanceKm() {
        return distanceKm;
    }
}