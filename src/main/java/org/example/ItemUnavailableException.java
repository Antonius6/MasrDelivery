package org.example;

public class ItemUnavailableException extends OrderException {
    public ItemUnavailableException(String message) {
        super(message);
    }
}
