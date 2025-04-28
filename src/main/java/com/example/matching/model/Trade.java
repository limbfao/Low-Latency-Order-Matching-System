package com.example.matching.model;

public class Trade {
    private final long buyOrderId;
    private final long sellOrderId;
    private final String symbol;
    private final double price;
    private final int quantity;

    public Trade(long buyOrderId, long sellOrderId, String symbol, double price, int quantity) {
        this.buyOrderId = buyOrderId;
        this.sellOrderId = sellOrderId;
        this.symbol = symbol;
        this.price = price;
        this.quantity = quantity;
    }

    public long getBuyOrderId() {
        return buyOrderId;
    }

    public long getSellOrderId() {
        return sellOrderId;
    }

    public String getSymbol() {
        return symbol;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public String toString() {
        return "Trade{" +
                "buyOrderId=" + buyOrderId +
                ", sellOrderId=" + sellOrderId +
                ", symbol='" + symbol + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                '}';
    }
}
