package org.example;

public class RestaurantClosedException extends OrderException {
    public RestaurantClosedException(String message) {
        super(message);
    }
}
