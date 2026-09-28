package org.example;

public class CustomerInsufficientBalance extends CustomerException {
    public CustomerInsufficientBalance(String message) {
        super(message);
    }
}
