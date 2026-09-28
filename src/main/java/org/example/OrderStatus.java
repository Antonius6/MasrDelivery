package org.example;

public enum OrderStatus {
    PLACED,
    ACCEPTED,
    PREPARING,
    READY,
    ASSIGNED,
    OUT_FOR_DELIVERY,
    DELIVERED,
    CANCELLED;

    public boolean canTransition( OrderStatus nextStatus) {
        return this.ordinal() + 1 == nextStatus.ordinal();
    }

    public boolean canCancel() {
        return this != OUT_FOR_DELIVERY && this != DELIVERED &&
                this != CANCELLED;
    }

}

