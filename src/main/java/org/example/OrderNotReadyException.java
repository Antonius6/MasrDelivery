package org.example;

public class OrderNotReadyException extends OrderException  {
    public OrderNotReadyException(String message) {
        super(message);
    }
}
