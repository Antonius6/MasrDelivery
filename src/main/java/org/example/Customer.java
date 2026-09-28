package org.example;

import java.util.*;

public class Customer {
    private static int idCounter=4950;

    private final String customerID;
    private final String customerName;
    private String customerPhone;
    private double walletBalance;
    private final Set<String>addresses= new HashSet<>();
    private int completedOrder=0;
    private final Deque<String>recentSearch= new ArrayDeque<>(5);




    Customer(String CustomerName, String customerPhone,String address) {
        setMobileNumber(customerPhone);
        this.customerID= "CUST-" + idCounter++;
        this.customerName = CustomerName;
        addAddress(address);
    }

    public static int getIdCounter() {
        return idCounter;
    }

    public String getCustomerID() {
        return customerID;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public double getWalletBalance() {
        return walletBalance;
    }

    public Set<String> getAddresses() {
       return Collections.unmodifiableSet(addresses);
    }

    public CustomerTier getTier() {
        return CustomerTier.fromOrderCount(this.completedOrder);
    }

    public int getCompletedOrder() {
        return completedOrder;
    }

    public void addAddress(String address) {
        if (address != null && !address.isBlank()) {
            this.addresses.add(address);
        }
    }

    public void setMobileNumber(String mobileNumber) {
        if (mobileNumber == null || !mobileNumber.matches("^0(10|11|12|15)\\d{8}$")) {
            throw new CustomerEgyptianNumberException("Invalid Egyptian mobile number");
        }
        this.customerPhone = mobileNumber;
    }

    public void deposit(double amount) {
        if (amount <= 0) {
            throw new CustomerInsufficientBalance("Wallet balance cannot be negative");
        }
        this.walletBalance += amount;
    }

    public void deduct(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }
        if (this.walletBalance < amount) {
            throw new CustomerInsufficientBalance("Insufficient wallet balance");
        }
        this.walletBalance -= amount;
    }

    public void addRecentSearch(String query) {
        if (query == null || query.isEmpty()) {
            throw new CustomerSearchException("Search query cannot be null or empty");
        }


        if(recentSearch.contains(query)) {
            recentSearch.remove(query);
        }

        if(recentSearch.size() >= 5) {
            recentSearch.removeLast();
        }

        recentSearch.addFirst(query);

    }

    public void refund(double amount) {
        this.walletBalance += amount;
    }

    public List<String> getRecentSearch() {
        return List.copyOf(recentSearch);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return Objects.equals(customerID, customer.customerID);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(customerID);
    }

    public void incrementCompletedOrders() {
        this.completedOrder++;
    }

    public boolean firstTimeCustomer() {
        return this.completedOrder == 0;
    }
}
