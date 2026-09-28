package org.example;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        StatisticsListener statisticsListener = new StatisticsListener();
        List<OrderObserver> globalObservers = List.of(
                new AuditLogListener(),
                new CustomerNotificationListener(),
                new RiderDashboardListener(),
                statisticsListener
        );

        Map<String, Customer> customers = new HashMap<>();
        Map<String, Order> orders = new HashMap<>();
        Map<String, Restaurant> restaurants = new LinkedHashMap<>();
        Map<String, Rider> riders = new HashMap<>();
        Map<String, Promotion> promotions = new HashMap<>();
        SearchService searchService = new SearchService();


        int option;
        do {
            mainMenu();
            option = readInt(in);
            switch (option) {
                case 1 -> customerMenu(in, restaurants, customers, searchService, orders, promotions, globalObservers);
                case 2 -> restaurantMenu(in, restaurants, orders);
                case 3 -> riderMenu(in, riders, orders);
                case 4 -> adminMenu(in, customers, restaurants, riders, orders, promotions);

            }

        }
        while (option != 0);


    }

    static int readInt(Scanner in) {
        while (!in.hasNextInt()) {
            System.out.println("Invalid input. please enter a number ");
            in.next();
        }
        int value = in.nextInt();
        in.nextLine();
        return value;
    }

    static double readDouble(Scanner in) {
        while (!in.hasNextDouble()) {
            System.out.println("Invalid input. please enter a number ");
            in.next();
        }
        double value = in.nextDouble();
        in.nextLine();
        return value;
    }

    static String readString(Scanner in) {
        while (!in.hasNextLine()) {
            System.out.println("Invalid input. please enter a line ");
            in.next();
        }
        return in.nextLine();

    }

    static void mainMenu() {
        System.out.println("===============================================");
        System.out.println("MASR DELIVERY - Main Menu");
        System.out.println("===============================================");
        System.out.println("1. Customer");
        System.out.println("2. Restaurant");
        System.out.println("3. Rider");
        System.out.println("4. Admin & Reports");
        System.out.println("0. Exit");
        System.out.println("===============================================");
        System.out.println("Choose an option: ");
    }

    static void customerMenu(Scanner in, Map<String, Restaurant> restaurants, Map<String, Customer> customers, SearchService searchService, Map<String, Order> orders, Map<String, Promotion> promotions, List<OrderObserver> globalObservers) {
        System.out.println("========================================");
        System.out.println("Customer Menu");
        System.out.println("1.Search & Browse Restaurant");
        System.out.println("2.Create New Order");
        System.out.println("3.View Orders & Cancel Orders");
        System.out.println("4.Register New Customer");
        System.out.println("5.Track Order");
        System.out.println("6.Deposit To Wallet");
        System.out.println("0.Exit");

        int choice = readInt(in);
        switch (choice) {
            case 1 -> {
                try {
                    System.out.println("\n--- Search & Browse Restaurant ---");
                    RestaurantSearchCriteria.Builder builder = new RestaurantSearchCriteria.Builder();

                    System.out.println("Enter search keyword  or press Enter to skip : ");
                    String query = readString(in);
                    if (!query.isBlank()) {
                        builder.textQuery(query);
                    }

                    System.out.println("Show open restaurant only? (y/n)");
                    String openChoice = readString(in);
                    if (openChoice.equalsIgnoreCase("y")) {
                        builder.openOnly(true);
                    }

                    System.out.println("Enter minimum rating ( 0 to 5 , or -1 to skip): ");
                    double minRating = readDouble(in);
                    if (minRating > 0) {
                        builder.minRating(minRating);
                    }

                    System.out.println("Enter maximum price ( 1 to you need , or -1 to skip): ");

                    double maxPrice = readDouble(in);
                    if (maxPrice > 0) {
                        builder.price(maxPrice);
                    }

                    System.out.println("Enter cuisine which you or press enter to  skip : ");
                    String cuisineChoice = readString(in);
                    if (!cuisineChoice.isBlank()) {
                        builder.cuisine(cuisineChoice);
                    }

                    System.out.println("Enter keyword to Search with district (y/n): ");
                    String searchKeyword = readString(in);
                    if (searchKeyword.equalsIgnoreCase("y")) {
                        System.out.println("Enter your option from 0 to 4");
                        District.optionDistrict();
                        int option = readInt(in);
                        District d = District.district(option);
                        builder.district(d);
                    }

                    RestaurantSearchCriteria criteria = builder.build();

                    List<Restaurant> results = searchService.search(new ArrayList<>(restaurants.values()), criteria);

                    System.out.println("--- Search Results " + results.size() + " found ----");
                    if (results.isEmpty()) {
                        System.out.println("No restaurant match your search criteria");
                    } else {
                        results.forEach(System.out::println);
                    }
                } catch (IllegalArgumentException e) {
                    System.out.println("Invalid search input: " + e.getMessage());
                }
            }
            case 2 -> {
                try {
                    System.out.println("Enter your id please : ");
                    String customerId = readString(in);
                    Customer customer = customers.get(customerId);
                    if (customer == null) {
                        System.out.println("Customer with id " + customerId + " not found");
                        break;
                    }
                    System.out.println("Enter restaurant id please : ");
                    String restaurantId = readString(in);
                    Restaurant restaurant = restaurants.get(restaurantId);
                    if (restaurant == null) {
                        System.out.println("Restaurant with id " + restaurantId + " not found");
                        break;
                    }
                    if (!restaurant.isOpen()) {
                        throw new RestaurantClosedException("Restaurant with id " + restaurantId + " is closed");
                    }
                    System.out.println("---- Saved Addresses ----");
                    customer.getAddresses().forEach(address -> System.out.println("- " + address));
                    System.out.println("Please enter delivery addresses");
                    String addresses = readString(in);

                    System.out.println("Choose delivery district:");
                    District.optionDistrict();
                    District deliveryDistrict = District.district(readInt(in));
                    if (deliveryDistrict == null) {
                        throw new IllegalArgumentException("Invalid delivery district");
                    }

                    System.out.println("----Menu----");
                    restaurant.getMenu().forEach((key, value) -> System.out.println("Menu: " + key + " - " + value));
                    List<OrderLineItem> lineItems = new ArrayList<>();
                    int flag;


                    do {

                        System.out.println("please enter menu item ID :");
                        String menuItemId = readString(in);
                        MenuItem item = restaurant.getMenu().get(menuItemId);
                        if (item == null) {
                            System.out.println("Menu item with id " + menuItemId + " not found");
                        } else if (!item.isAvailability()) {
                            throw new ItemUnavailableException("Menu item " + menuItemId + " is not available for this restaurant");
                        } else {
                            System.out.println("please enter quantity of item");
                            double quantity = readDouble(in);
                            if (quantity > 0) {
                                double alreadyInCart = lineItems.stream()
                                        .filter(li -> li.getMenuItem().equals(item))
                                        .mapToDouble(OrderLineItem::getQuantityOrWeight)
                                        .sum();

                                if (!item.hasEnoughStock(alreadyInCart + quantity)) {
                                    System.out.println("Not enough stock for item " + item.getName());
                                } else {
                                    lineItems.add(new OrderLineItem(item, quantity));
                                    System.out.println("Added to order");
                                }
                            } else {
                                System.out.println("Quantity must be greater than 0");
                            }
                        }


                        System.out.println("Enter 1 to add another item, or 0 to finish: ");
                        flag = readInt(in);
                    }
                    while (flag != 0);

                    if (lineItems.isEmpty()) {
                        System.out.println("Order cancelled. No items were added.");
                        break;
                    }

                    System.out.println("Enter delivery distance (km) between restaurant and delivery address: ");
                    double distanceKm = readDouble(in);

                    System.out.println("Enter Promo Code (or press Enter to skip): ");
                    String promoCode = readString(in);


                    Order.Builder builder = new Order.Builder()
                            .orderId()
                            .status()
                            .placedAt()
                            .customer(customer)
                            .restaurant(restaurant)
                            .deliveryAddress(addresses)
                            .deliveryDistrict(deliveryDistrict)
                            .lineItems(lineItems)
                            .distanceKm(distanceKm);

                    if (!promoCode.isBlank()) {
                        Promotion promo = promotions.get(promoCode.toUpperCase());
                        if (promo == null) {
                            throw new InvalidPromotionException("Promotion code '" + promoCode + "' does not exist.");
                        }
                        builder.promotion(promo);
                    }

                    Order order = builder.build(globalObservers);
                    order.calculateTotal(order.getDistanceKm(), order.getPromotion());

                    if (customer.getWalletBalance() < order.getTotal()) {
                        System.out.println("Insufficient wallet balance! Order Total: " + order.getTotal() + " EGP, Balance: " + customer.getWalletBalance() + " EGP");
                        break;
                    }
                    customer.deduct(order.getTotal());
                    order.getLineItems().forEach(li -> li.getMenuItem().deductStock(li.getQuantityOrWeight()));
                    orders.put(order.getOrderId(), order);
                    System.out.println("Order " + order.getOrderId() + " has been successfully saved.");
                } catch (MasrDeliveryException | IllegalArgumentException e) {
                    System.out.println("Failed to place order: " + e.getMessage());
                }
            }
            case 3 -> {
                System.out.println("View & Cancel Order");
                System.out.println("Enter your customer ID : ");
                String customerId = readString(in);

                Customer customer = customers.get(customerId);
                if (customer == null) {
                    System.out.println("Customer with id " + customerId + " not found");
                    break;
                }

                List<Order> customerOrders = orders.values().stream()
                        .filter(o -> o.getCustomer().getCustomerID().equals(customerId))
                        .sorted((o1, o2) -> o2.getPlacedAt().compareTo(o1.getPlacedAt()))
                        .toList();

                if (customerOrders.isEmpty()) {
                    System.out.println("No orders found for this customer.");
                    break;
                }

                System.out.println("--- Your orders ---");
                customerOrders.forEach(o ->
                        System.out.println("Order ID: " + o.getOrderId() + " | Status: " + o.getStatus() + " | Total: " + o.getTotal() + " EGP")
                );

                System.out.println("Enter Order ID to cancel (or press Enter to skip): ");
                String orderId = readString(in);

                if (!orderId.isBlank()) {
                    Order orderToCancel = orders.get(orderId);

                    if (orderToCancel == null || !orderToCancel.getCustomer().getCustomerID().equals(customerId)) {
                        System.out.println("Order not found or does not belong to you.");
                    } else {
                        try {
                            orderToCancel.cancelOrder();
                            customer.refund(orderToCancel.getTotal());
                            System.out.println("Order " + orderId + " has been cancelled successfully.");
                            System.out.println("Refunded: " + orderToCancel.getTotal() + " EGP | New Wallet Balance: " + customer.getWalletBalance() + " EGP");
                        } catch (Exception e) {
                            System.out.println("Cancellation failed: " + e.getMessage());
                        }
                    }
                }
            }
            case 4 -> {
                System.out.println("--- Register New Customer ---");
                System.out.println("Enter your name: ");
                String name = readString(in);
                System.out.println("Enter your mobile number: ");
                String phone = readString(in);
                System.out.println("Enter your address: ");
                String address = readString(in);

                try {
                    Customer customer = new Customer(name, phone, address);
                    customers.put(customer.getCustomerID(), customer);
                    System.out.println("Customer " + customer.getCustomerID() + " has been successfully registered.");
                } catch (CustomerEgyptianNumberException e) {
                    System.out.println("Registration failed: " + e.getMessage());
                }
            }
            case 5 -> {
                System.out.println("--- Track Order ---");
                System.out.println("Enter Order ID: ");
                String orderId = readString(in);

                Order order = orders.get(orderId);
                if (order == null) {
                    System.out.println("Order with id " + orderId + " not found");
                    break;
                }
                Duration elapsed = Duration.between(order.getPlacedAt(), LocalDateTime.now());
                long hours = elapsed.toHours();
                long minutes = elapsed.toMinutesPart();

                System.out.println("Order ID: " + order.getOrderId());
                System.out.println("Status: " + order.getStatus());
                System.out.println("Placed At: " + order.getPlacedAt());
                System.out.println("Elapsed Time: " + hours + "h " + minutes + "m since placement");

            }
            case 6 -> {
                System.out.println("--- Deposit To Wallet ---");
                System.out.println("Enter your customer ID: ");
                String customerId = readString(in);
                Customer customer = customers.get(customerId);
                if (customer == null) {
                    System.out.println("Customer with id " + customerId + " not found");
                    break;
                }
                System.out.println("Enter amount to deposit: ");
                double amount = readDouble(in);
                try {
                    customer.deposit(amount);
                    System.out.println("New Wallet Balance: " + customer.getWalletBalance() + " EGP");
                } catch (CustomerException e) {
                    System.out.println("Deposit failed: " + e.getMessage());
                }
            }
            case 0 -> System.out.println("Thank you for using our Order Manager");
        }


    }

    static void restaurantMenu(Scanner in, Map<String, Restaurant> restaurants, Map<String, Order> orders) {
        System.out.println("========================================");
        System.out.println("RESTAURANT MENU");
        System.out.println("========================================");
        System.out.println("Enter Restaurant ID: ");
        String restaurantId = readString(in);

        Restaurant restaurant = restaurants.get(restaurantId);
        if (restaurant == null) {
            System.out.println("Restaurant with ID " + restaurantId + " not found.");
            return;
        }

        System.out.println("Welcome, " + restaurant.getName());
        System.out.println("1. Toggle Menu Item Availability");
        System.out.println("2. Add New Menu Item");
        System.out.println("3. Manage Orders (Accept/Reject/Update Status)");
        System.out.println("4. View Orders & Today's Revenue");
        System.out.println("5. Adjust Daily Stock");
        System.out.println("6. Open / Close Restaurant");
        System.out.println("0. Back to Main Menu");
        System.out.println("Choose an option: ");

        int choice = readInt(in);
        switch (choice) {
            case 1 -> {
                System.out.println("--- Toggle Item Availability ---");
                String itemId = readString(in);

                MenuItem item = restaurant.getMenu().get(itemId);
                if (item == null)
                    System.out.println("Item with ID " + itemId + " not found");
                else {
                    boolean newStatus = !item.isAvailability();
                    item.setAvailability(newStatus);
                    System.out.println("Item '" + item.getName() + "' status changed to: "
                            + (newStatus ? "AVAILABLE" : "UNAVAILABLE"));
                }
            }
            case 2 -> {
                System.out.println("---- Add New Menu Item ----");

                System.out.println("Enter Item Name: ");
                String name = readString(in);

                System.out.println("Enter Price: ");
                double price = readDouble(in);

                System.out.println("Enter Preparation Time (in minutes): ");
                int prepTime = readInt(in);

                System.out.println("Enter Category: ");
                String category = readString(in);

                System.out.println("Select Item Type:");
                System.out.println("1. STANDARD");
                System.out.println("2. WEIGHTED");
                System.out.println("3. COMBO");
                int typeChoice = readInt(in);

                String type = switch (typeChoice) {
                    case 1 -> "STANDARD";
                    case 2 -> "WEIGHTED";
                    case 3 -> "COMBO";
                    default -> "";
                };

                if (type.isBlank()) {
                    System.out.println("Invalid item type selected.");
                    break;
                }

                List<MenuItem> bundledItems = new ArrayList<>();

                if ("COMBO".equals(type)) {
                    if (restaurant.getMenu().isEmpty()) {
                        System.out.println("Cannot create COMBO: Restaurant menu is empty. Add standard items first.");
                        break;
                    }

                    System.out.println("---- Select Bundled Items for Combo ----");
                    restaurant.getMenu().forEach((key, value) -> System.out.println(key + " - " + value.getName()));

                    int flag;
                    do {
                        System.out.println("Enter Menu Item ID to add to combo: ");
                        String bundledId = readString(in);
                        MenuItem bundledItem = restaurant.getMenu().get(bundledId);

                        if (bundledItem != null) {
                            bundledItems.add(bundledItem);
                            System.out.println("Added '" + bundledItem.getName() + "' to combo.");
                        } else {
                            System.out.println("Item with ID " + bundledId + " not found.");
                        }

                        System.out.println("Enter 1 to add another item to combo, or 0 to finish: ");
                        flag = readInt(in);
                    } while (flag != 0);
                }

                try {
                    MenuItem newItem = MenuItemFactory.createItem(type, name, price, prepTime, category, true, bundledItems);

                    System.out.println("Enter initial stock quantity: ");
                    double initialStock = readDouble(in);
                    newItem.setStock(initialStock);

                    restaurant.addMenuItem(newItem);
                    System.out.println("Item '" + name + "' added successfully with ID " + newItem.getItemId() + " as " + type + ".");
                } catch (IllegalArgumentException e) {
                    System.out.println("Failed to create item: " + e.getMessage());
                }
            }
            case 3 -> {
                System.out.println("---- Manage Restaurant Orders ----");

                List<Order> restaurantOrders = orders.values().stream()
                        .filter(o -> o.getRestaurant().getId().equals(restaurant.getId()))
                        .toList();

                if (restaurantOrders.isEmpty()) {
                    System.out.println("No orders found for this restaurant.");
                    break;
                }

                System.out.println("---- Orders List ----");
                restaurantOrders.forEach(o ->
                        System.out.println("Order ID: " + o.getOrderId() + " | Customer: " + o.getCustomer().getCustomerName() + " | Status: " + o.getStatus() + " | Total: " + o.getTotal() + " EGP")
                );

                System.out.println("Enter Order ID to update status (or press Enter to skip): ");
                String orderId = readString(in);

                if (!orderId.isBlank()) {
                    Order targetOrder = orders.get(orderId);

                    if (targetOrder == null || !targetOrder.getRestaurant().getId().equals(restaurant.getId())) {
                        System.out.println("Order not found or belongs to another restaurant.");
                        break;
                    }

                    System.out.println("Current Status: " + targetOrder.getStatus());
                    System.out.println("Choose New Status:");
                    System.out.println("1. ACCEPT ORDER");
                    System.out.println("2. PREPARING");
                    System.out.println("3. READY");
                    System.out.println("4. CANCEL / REJECT");
                    int statusChoice = readInt(in);

                    try {
                        switch (statusChoice) {
                            case 1 -> {
                                targetOrder.transitionStatus(OrderStatus.ACCEPTED);
                                System.out.println("Order " + orderId + " is now ACCEPTED.");
                            }
                            case 2 -> {
                                targetOrder.transitionStatus(OrderStatus.PREPARING);
                                System.out.println("Order " + orderId + " is now PREPARING.");
                            }
                            case 3 -> {
                                targetOrder.transitionStatus(OrderStatus.READY);
                                System.out.println("Order " + orderId + " is now READY for rider pickup.");
                            }
                            case 4 -> {
                                targetOrder.cancelOrder();
                                targetOrder.getCustomer().refund(targetOrder.getTotal());
                                System.out.println("Order " + orderId + " has been cancelled and customer refunded.");
                            }
                            default -> System.out.println("Invalid choice.");
                        }
                    } catch (OrderIllegalTransitions | OrderCancelException e) {
                        System.out.println("Failed to update order: " + e.getMessage());
                    }
                }
            }
            case 4 -> {
                System.out.println("---- Today's Orders & Revenue ----");

                List<Order> restaurantOrders = orders.values().stream()
                        .filter(o -> o.getRestaurant().getId().equals(restaurant.getId()))
                        .toList();

                if (restaurantOrders.isEmpty()) {
                    System.out.println("No orders recorded for this restaurant.");
                    break;
                }

                double totalRevenue = restaurantOrders.stream()
                        .filter(o -> o.getStatus() != OrderStatus.CANCELLED)
                        .mapToDouble(Order::getTotal)
                        .sum();

                System.out.println("Total Orders Count: " + restaurantOrders.size());
                System.out.println("Total Net Revenue: " + totalRevenue + " EGP");
                System.out.println("----------------------------------");

                restaurantOrders.forEach(o ->
                        System.out.println("ID: " + o.getOrderId() + " | Date: " + o.getPlacedAt() + " | Status: " + o.getStatus() + " | Amount: " + o.getTotal() + " EGP")
                );
            }
            case 5 -> {
                System.out.println("--- Adjust Daily Stock ---");
                restaurant.getMenu().forEach((key, value) -> System.out.println(key + " - " + value.getName() + " | Current stock: " + value.getStock()));

                System.out.println("Enter Item ID to adjust stock: ");
                String itemId = readString(in);

                MenuItem item = restaurant.getMenu().get(itemId);
                if (item == null) {
                    System.out.println("Item with ID " + itemId + " not found");
                    break;
                }

                System.out.println("Enter new stock quantity: ");
                double newStock = readDouble(in);

                try {
                    item.setStock(newStock);
                    System.out.println("Stock for '" + item.getName() + "' updated to " + newStock);
                } catch (IllegalArgumentException e) {
                    System.out.println("Failed to update stock: " + e.getMessage());
                }

            }
            case 6 -> {
                boolean nowOpen = !restaurant.isOpen();
                restaurant.setStatus(nowOpen ? RestaurantStatus.OPEN : RestaurantStatus.CLOSED);
                System.out.println("Restaurant '" + restaurant.getName() + "' is now " + (nowOpen ? "OPEN" : "CLOSED"));
            }
            case 0 -> System.out.println("Returning to Main Menu...");
            default -> System.out.println("Invalid option.");
        }
    }

    static void riderMenu(Scanner in, Map<String, Rider> riders, Map<String, Order> orders) {
        System.out.println("========================================");
        System.out.println("              RIDER MENU                ");
        System.out.println("========================================");

        System.out.println("Enter your Rider ID: ");
        String riderId = readString(in);

        Rider currentRider = riders.get(riderId);
        if (currentRider == null) {
            System.out.println("Rider with ID " + riderId + " not found!");
            return;
        }

        int choice;
        do {
            System.out.println("Welcome, " + currentRider.getRiderName() + "!");
            System.out.println("Status: " + (currentRider.isAvailable() ? "AVAILABLE" : "BUSY"));
            System.out.println("1. View & Pick Up READY Orders");
            System.out.println("2. Complete / Deliver Order");
            System.out.println("3. View History & Earnings");
            System.out.println("0. Back to Main Menu");
            System.out.print("Choose an option: ");

            choice = readInt(in);

            switch (choice) {
                case 1 -> {
                    System.out.println("--- READY Orders ---");
                    List<Order> readyOrders = orders.values().stream()
                            .filter(o -> o.getStatus() == OrderStatus.READY)
                            .toList();

                    if (readyOrders.isEmpty()) {
                        System.out.println("No orders ready for pickup at the moment.");
                        break;
                    }

                    readyOrders.forEach(o ->
                            System.out.println("Order ID: " + o.getOrderId() + " | Delivery Fee: " + o.getDeliveryFee() + " EGP")
                    );

                    System.out.println("Enter Order ID to pick up (or press Enter to skip): ");
                    String orderId = readString(in);

                    if (!orderId.isBlank()) {
                        Order orderToPick = orders.get(orderId);

                        if (orderToPick != null && orderToPick.getStatus() == OrderStatus.READY) {
                            try {
                                double distanceKm = orderToPick.getDistanceKm();
                                System.out.println("Enter total weight (kg) for this order: ");
                                double weightKg = readDouble(in);

                                currentRider.assignOrder(orderToPick, distanceKm, weightKg);

                                orderToPick.setAssignedRider(currentRider);
                                orderToPick.transitionStatus(OrderStatus.ASSIGNED);
                                orderToPick.transitionStatus(OrderStatus.OUT_FOR_DELIVERY);

                                System.out.println(" Order " + orderId + " is now OUT FOR DELIVERY!");
                            } catch (IllegalArgumentException | RiderAlreadyBusyException | OrderIllegalTransitions e) {
                                System.out.println(" Failed to assign order: " + e.getMessage());
                            }
                        } else {
                            System.out.println("Invalid Order ID or Order is not READY.");
                        }
                    }
                }
                case 2 -> {
                    System.out.println("--- Deliver Order ---");
                    List<Order> activeOrders = orders.values().stream()
                            .filter(o -> o.getStatus() == OrderStatus.OUT_FOR_DELIVERY
                                    && currentRider.equals(o.getAssignedRider()))
                            .toList();

                    if (activeOrders.isEmpty()) {
                        System.out.println("You have no orders currently out for delivery.");
                        break;
                    }

                    activeOrders.forEach(o ->
                            System.out.println("Order ID: " + o.getOrderId() + " | Customer: " + o.getCustomer().getCustomerName())
                    );

                    System.out.println("Enter Order ID to mark as DELIVERED (or press Enter to skip): ");
                    String orderId = readString(in);

                    if (!orderId.isBlank()) {
                        Order orderToDeliver = orders.get(orderId);

                        if (orderToDeliver != null
                                && orderToDeliver.getStatus() == OrderStatus.OUT_FOR_DELIVERY
                                && currentRider.equals(orderToDeliver.getAssignedRider())) {

                            try {
                                orderToDeliver.transitionStatus(OrderStatus.DELIVERED);
                                currentRider.completeOrder();
                                orderToDeliver.getCustomer().incrementCompletedOrders();

                                System.out.println(" Order " + orderId + " DELIVERED successfully at " + orderToDeliver.getDeliveredAt() + "!");
                            } catch (OrderIllegalTransitions | IllegalStateException e) {
                                System.out.println(" Failed to complete order: " + e.getMessage());
                            }
                        } else {
                            System.out.println("Invalid Order ID.");
                        }
                    }
                }
                case 3 -> {
                    System.out.println("--- History & Earnings ---");

                    System.out.println("Total Deliveries Completed: " + currentRider.getCountCompletedDeliveries());

                    List<Order> deliveredOrders = orders.values().stream()
                            .filter(o -> o.getStatus() == OrderStatus.DELIVERED
                                    && currentRider.equals(o.getAssignedRider()))
                            .toList();

                    if (deliveredOrders.isEmpty()) {
                        System.out.println("No completed deliveries yet.");
                        break;
                    }

                    double totalEarnings = deliveredOrders.stream()
                            .mapToDouble(Order::getDeliveryFee)
                            .sum();

                    System.out.println("Total Earnings from Delivery Fees: " + totalEarnings + " EGP");
                    System.out.println("----------------------------------");

                    deliveredOrders.forEach(o ->
                            System.out.println("Order ID: " + o.getOrderId() + " | Delivery Fee: " + o.getDeliveryFee() + " EGP | Delivered At: " + o.getDeliveredAt())
                    );
                }
                case 0 -> System.out.println("Returning to Main Menu...");
                default -> System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 0);
    }

    static void adminMenu(Scanner in, Map<String, Customer> customers, Map<String, Restaurant> restaurants, Map<String, Rider> riders, Map<String, Order> orders, Map<String, Promotion> promotions) {


        int choice;
        do {

            ReportingService reportingService = new ReportingService(
                    new ArrayList<>(orders.values()),
                    new ArrayList<>(restaurants.values()),
                    new ArrayList<>(customers.values()),
                    new ArrayList<>(riders.values())
            );
            System.out.println("========================================");
            System.out.println("              ADMIN MENU                ");
            System.out.println("========================================");
            System.out.println("1. General Platform Stats (Revenue, Delivery Time, Cancellation Rate)");
            System.out.println("2. Top Entities (Restaurants, Customers, Riders, Items)");
            System.out.println("3. District Insights (Revenue & Average Value)");
            System.out.println("4. Orders Count by Status");
            System.out.println("5. Add Restaurant");
            System.out.println("6. Remove Restaurant");
            System.out.println("7. Add Rider");
            System.out.println("8. Create New Promotion");
            System.out.println("9. View All Cuisine Categories");
            System.out.println("0. Back to Main Menu");
            System.out.print("Choose an option: ");

            choice = readInt(in);

            switch (choice) {
                case 1 -> {
                    System.out.println("--- General Platform Stats ---");

                    LocalDateTime startOfYear = LocalDateTime.now().withDayOfYear(1).withHour(0).withMinute(0);
                    double totalRevenue = reportingService.getTotalRevenue(startOfYear, LocalDateTime.now());

                    System.out.println("Total Platform Revenue (YTD): " + totalRevenue + " EGP");
                    System.out.println("Average Delivery Time: " + reportingService.getAverageDeliveryTimeMinutes() + " mins");
                    System.out.println("Order Cancellation Rate: " + (reportingService.getCancellationRate() * 100) + " %");
                }
                case 2 -> {
                    System.out.println("--- Top Entities ---");
                    System.out.println("Enter limit for top lists (e.g., 5): ");
                    int limit = readInt(in);

                    System.out.println("Top " + limit + " Restaurants by Revenue (This Month):");
                    reportingService.topFiveRestaurantsByRevenue(YearMonth.now())
                            .forEach(r -> System.out.println("- " + r.getName()));

                    System.out.println("Most Active Customers:");
                    reportingService.getMostActiveCustomers(limit)
                            .forEach(c -> System.out.println("- " + c.getCustomerName()));

                    System.out.println("Top Riders by Deliveries:");
                    reportingService.getTopRidersByDeliveries(limit)
                            .forEach(r -> System.out.println("- " + r.getRiderName() + " (" + r.getCountCompletedDeliveries() + " deliveries)"));

                    System.out.println("Most Popular Menu Items:");
                    reportingService.getMostPopularMenuItems(limit)
                            .forEach(item -> System.out.println("- " + item.getName()));
                }
                case 3 -> {
                    System.out.println("--- District Insights ---");

                    System.out.println("Revenue By District:");
                    reportingService.getRevenueByDistrict().forEach((district, rev) ->
                            System.out.println(district + ": " + rev + " EGP")
                    );

                    System.out.println("Average Order Value Per District:");
                    reportingService.getAverageOrderValuePerDistrict().forEach((district, avg) ->
                            System.out.println(district + ": " + avg + " EGP")
                    );
                }
                case 4 -> {
                    System.out.println("--- Orders Count by Status ---");
                    reportingService.getOrdersCountByStatus().forEach((status, count) ->
                            System.out.println(status + ": " + count + " orders")
                    );
                }
                case 5 -> {
                    System.out.println("--- Add New Restaurant ---");
                    System.out.println("Enter restaurant name: ");
                    String name = readString(in);

                    System.out.println("Choose district:");
                    District.optionDistrict();
                    int districtChoice = readInt(in);
                    District district = District.district(districtChoice);

                    if (district == null) {
                        System.out.println("Invalid district choice.");
                        break;
                    }

                    Restaurant newRestaurant = new Restaurant(name, district, RestaurantStatus.OPEN);

                    System.out.println("Enter cuisine (or press Enter to skip): ");
                    String cuisine = readString(in);
                    if (!cuisine.isBlank()) {
                        newRestaurant.addCuisine(cuisine);
                    }

                    System.out.println("Enter rating (0 to 5): ");
                    double rating = readDouble(in);
                    try {
                        newRestaurant.setRating(rating);
                    } catch (IllegalArgumentException e) {
                        System.out.println("Invalid rating, kept as 0: " + e.getMessage());
                    }

                    restaurants.put(newRestaurant.getId(), newRestaurant);
                    System.out.println("Restaurant " + newRestaurant.getName() + " created.");


                }
                case 6 -> {
                    System.out.println("--- Remove Restaurant ---");
                    System.out.println("Enter restaurant ID to remove: ");
                    String restaurantId = readString(in);

                    Restaurant restaurant = restaurants.get(restaurantId);
                    if (restaurant == null) {
                        System.out.println("Restaurant with ID " + restaurantId + " not found.");
                        break;
                    }

                    restaurants.remove(restaurantId);
                    System.out.println("Restaurant '" + restaurant.getName() + "' removed successfully.");
                }
                case 7 -> {
                    System.out.println("--- Add New Rider ---");
                    System.out.println("Enter rider name: ");
                    String name = readString(in);

                    System.out.println("Choose vehicle type:");
                    System.out.println("1. MOTORCYCLE");
                    System.out.println("2. BICYCLE");
                    System.out.println("3. CAR");
                    int vehicleChoice = readInt(in);

                    VehicleType vehicle = switch (vehicleChoice) {
                        case 1 -> VehicleType.MOTORCYCLE;
                        case 2 -> VehicleType.BICYCLE;
                        case 3 -> VehicleType.CAR;
                        default -> null;
                    };

                    if (vehicle == null) {
                        System.out.println("Invalid vehicle type.");
                        break;
                    }

                    Rider newRider = new Rider(name, vehicle);
                    riders.put(newRider.getRiderId(), newRider);
                    System.out.println("Rider added successfully! ID: " + newRider.getRiderId());
                }
                case 8 -> {
                    System.out.println("--- Create New Promotion ---");
                    System.out.println("Enter promotion code: ");
                    String code = readString(in);


                    System.out.println("Select promotion type:");
                    System.out.println("1. PERCENTAGE (with max cap)");
                    System.out.println("2. FIXED (fixed amount off)");
                    System.out.println("3. FREE_DELIVERY");
                    int typeChoice = readInt(in);

                    String type = switch (typeChoice) {
                        case 1 -> "PERCENTAGE";
                        case 2 -> "FIXED";
                        case 3 -> "FREE_DELIVERY";
                        default -> "";
                    };

                    if (type.isBlank()) {
                        System.out.println("Invalid promotion type.");
                        break;
                    }

                    double value = 0.0;
                    double capOrFee = 0.0;

                    if (type.equals("PERCENTAGE")) {
                        System.out.println("Enter discount percentage (e.g. 0.20 for 20%): ");
                        value = readDouble(in);
                        System.out.println("Enter max discount cap (EGP): ");
                        capOrFee = readDouble(in);
                    } else if (type.equals("FIXED")) {
                        System.out.println("Enter fixed discount amount (EGP): ");
                        value = readDouble(in);
                    }
                    try {
                        PromotionStrategy strategy = PromotionStrategyFactory.createStrategy(type, value, capOrFee);

                        Promotion.Builder builder = new Promotion.Builder()
                                .code(code)
                                .strategy(strategy);

                        System.out.println("Enter minimum subtotal (0 for none): ");
                        double minSubtotal = readDouble(in);
                        builder.minSubtotal(minSubtotal);

                        System.out.println("Restrict to a specific district? (y/n): ");
                        String restrictDistrict = readString(in);
                        if (restrictDistrict.equalsIgnoreCase("y")) {
                            District.optionDistrict();
                            int districtChoice = readInt(in);
                            builder.restrictedDistrict(District.district(districtChoice));
                        }

                        System.out.println("Restrict to first-time customers only? (y/n): ");
                        String firstTimeOnly = readString(in);
                        builder.firstTimeCustomerOnly(firstTimeOnly.equalsIgnoreCase("y"));

                        Promotion promotion = builder.build();
                        promotions.put(promotion.getCode(), promotion);
                        System.out.println("Promotion '" + promotion.getCode() + "' created successfully.");
                    } catch (IllegalArgumentException e) {
                        System.out.println("Failed to create promotion: " + e.getMessage());
                    }
                }
                case 9 -> {
                    System.out.println("--- All Cuisine Categories on Platform ---");
                    SearchService searchService = new SearchService();
                    Set<String> cuisines = searchService.getAllDistinctCuisines(new ArrayList<>(restaurants.values()));
                    if (cuisines.isEmpty()) {
                        System.out.println("No cuisines registered yet.");
                    } else {
                        cuisines.forEach(c -> System.out.println("- " + c));
                    }
                }
                case 0 -> System.out.println("Returning to Main Menu...");
                default -> System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 0);
    }


}