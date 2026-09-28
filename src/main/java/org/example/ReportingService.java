package org.example;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

public class ReportingService {

    private final List<Order> orders;
    private final List<Restaurant> restaurants;
    private final List<Customer> customers;
    private final List<Rider> riders;

    ReportingService(List<Order> orders, List<Restaurant> restaurants, List<Customer> customers, List<Rider> riders) {
        this.orders = orders;
        this.restaurants = restaurants;
        this.customers = customers;
        this.riders = riders;
    }

    public double getTotalRevenue(LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null) {
            throw new IllegalArgumentException("Start and end dates cannot be null");
        }
        if (start.isAfter(end)) {
            throw new IllegalArgumentException("Start date cannot be after end date");
        }
        return orders.stream()
                .filter(r -> (!r.getPlacedAt().isBefore(start) && !r.getPlacedAt().isAfter(end)))
                .filter(r -> r.getStatus() == OrderStatus.DELIVERED)
                .mapToDouble(Order::getTotal)
                .sum();
    }

    public List<Restaurant> topFiveRestaurantsByRevenue(YearMonth month) {

        if(month == null) {
            throw new IllegalArgumentException("Month cannot be null");
        }
        if(month.isAfter(YearMonth.now())) {
            throw new IllegalArgumentException("Month cannot be after now");
        }
        Map<Restaurant, Double> map = orders.stream()
                .filter(r -> r.getStatus() == OrderStatus.DELIVERED)
                .filter(order -> YearMonth.from(order.getPlacedAt()).equals(month))
                .collect(Collectors.groupingBy(Order::getRestaurant,
                        Collectors.summingDouble(Order::getTotal))
                );

        return map.entrySet().stream()
                .sorted(Map.Entry.<Restaurant, Double>comparingByValue().reversed())
                .limit(5)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    public Map<District, Double> getAverageOrderValuePerDistrict() {
        return orders.stream().filter(r -> r.getStatus() == OrderStatus.DELIVERED)
                .collect(Collectors.groupingBy(Order::getDeliveryDistrict,

                        Collectors.averagingDouble(Order::getTotal))
                );


    }

    public List<Customer> getMostActiveCustomers(int limit) {
        if(limit < 1) {
            throw new IllegalArgumentException("Limit cannot be less than 1");
        }
        return orders.stream()
                .filter(r -> r.getStatus() == OrderStatus.DELIVERED)
                .collect(Collectors.groupingBy(Order::getCustomer, Collectors.counting()))
                .entrySet().stream()
                .sorted(Map.Entry.<Customer, Long>comparingByValue().reversed())
                .map(Map.Entry::getKey)
                .limit(limit)
                .toList();
    }

    public List<Rider> getTopRidersByDeliveries(int limit) {
        if(limit < 1) {
            throw new IllegalArgumentException("Limit cannot be less than 1");
        }
        return riders.stream()
                .sorted(Comparator.comparing(Rider::getCountCompletedDeliveries).reversed())
                .limit(limit)
                .toList();
    }

    public Map<OrderStatus, Long> getOrdersCountByStatus() {
        return orders.stream()
                .collect(Collectors.groupingBy(
                        Order::getStatus,
                        Collectors.counting()
                ));
    }

    public List<MenuItem> getMostPopularMenuItems(int limit) {
        if(limit < 1) {
            throw new IllegalArgumentException("Limit cannot be less than 1");
        }
        return orders.stream()
                .filter(order -> order.getStatus() == OrderStatus.DELIVERED)
                .flatMap(order -> order.getLineItems().stream())
                .collect(Collectors.groupingBy(
                        OrderLineItem::getMenuItem,
                        Collectors.summingDouble(OrderLineItem::getQuantityOrWeight)
                ))
                .entrySet().stream()
                .sorted(Map.Entry.<MenuItem, Double>comparingByValue().reversed())
                .limit(limit)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    public double getAverageDeliveryTimeMinutes() {
        return orders.stream()
                .filter(order -> order.getStatus() == OrderStatus.DELIVERED && order.getDeliveredAt() != null)
                .mapToLong(order -> java.time.Duration.between(order.getPlacedAt(), order.getDeliveredAt()).toMinutes())
                .average()
                .orElse(0.0);
    }


    public double getCancellationRate(){
        double totalOrders = orders.size();
        if(totalOrders == 0){
            return 0.0;
        }
        double cancelledOrders = orders.stream()
                .filter(r->r.getStatus()==OrderStatus.CANCELLED).count();
        return cancelledOrders/totalOrders;
    }


    public Map<District, Double> getRevenueByDistrict(){
        return  orders.stream().filter(r -> r.getStatus() == OrderStatus.DELIVERED)
                .collect(Collectors.groupingBy(Order::getDeliveryDistrict,Collectors.summingDouble(Order::getTotal)));
    }


}

