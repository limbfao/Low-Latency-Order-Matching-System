package com.example.matching.model;

import java.util.concurrent.atomic.AtomicLong;

public class Order {
    private static final AtomicLong ID_GENERATOR = new AtomicLong(1);

    private final long orderId;
    private final String symbol; // e.g,. "AAPL", "GOOGL"
    private final boolean isBuy;  // true for buy, false for sell
    private final double price;
    private final int quantity;
    private int remainingQuantity;

    public Order(String symbol, boolean isBuy, double price, int quantity) {
        this.orderId = ID_GENERATOR.getAndIncrement();
        this.symbol = symbol;
        this.isBuy = isBuy;
        this.price = price;
        this.quantity = quantity;
        this.remainingQuantity = quantity;
    }

    public long getOrderId() {
        return orderId;
    }

    public String getSymbol() {
        return symbol;
    }

    public boolean isBuy() {
        return isBuy;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getRemainingQuantity() {
        return remainingQuantity;
    }

    public void execute(int quantity) {
        if (quantity > remainingQuantity) {
            throw new IllegalArgumentException("Execution quantity exceeds remaining quantity");
        }
        remainingQuantity -= quantity;
    }

    public boolean isFullyExecuted() {
        return remainingQuantity == 0;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", symbol='" + symbol + '\'' +
                ", isBuy=" + isBuy +
                ", price=" + price +
                ", quantity=" + quantity +
                ", remainingQuantity=" + remainingQuantity +
                '}';
    }

}
