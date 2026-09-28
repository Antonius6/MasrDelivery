package org.example;

public class StockShortageException extends OrderException {
    public StockShortageException(String message) {
        super(message);
    }
}
