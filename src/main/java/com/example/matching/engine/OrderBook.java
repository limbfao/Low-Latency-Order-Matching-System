package com.example.matching.engine;

import com.example.matching.model.Order;
import com.example.matching.model.Trade;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class OrderBook {

    private final String symbol;

    // Buy orders: Highest price first, then earliest order
    private final PriorityQueue<Order> buyOrders = new PriorityQueue<>(
            (o1, o2) -> {
                if (o1.getPrice() != o2.getPrice()) {
                    return Double.compare(o2.getPrice(), o1.getPrice()); // Higher price first
                }
                return Long.compare(o1.getOrderId(), o2.getOrderId()); // Earlier order first
            });

    // Sell orders: Lowest price first, then earliest order
    private final PriorityQueue<Order> sellOrders = new PriorityQueue<>(
            (o1, o2) -> {
                if (o1.getPrice() != o2.getPrice()) {
                    return Double.compare(o1.getPrice(), o2.getPrice()); // Lower price first
                }
                return Long.compare(o1.getOrderId(), o2.getOrderId()); // Earlier order first
            });

    public OrderBook(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }


    public List<Trade> processOrder(Order incomingOrder) {
        List<Trade> trades = new ArrayList<>();

        if (incomingOrder.isBuy()) {
            matchOrders(incomingOrder, sellOrders, trades);
            if (!incomingOrder.isFullyExecuted()) {
                buyOrders.add(incomingOrder);
            }
        } else {
            matchOrders(incomingOrder, buyOrders, trades);
            if (!incomingOrder.isFullyExecuted()) {
                sellOrders.add(incomingOrder);
            }
        }

        return trades;
    }


    private void matchOrders(Order incomingOrder, PriorityQueue<Order> oppositeOrders, List<Trade> trades) {
        while (!oppositeOrders.isEmpty() && !incomingOrder.isFullyExecuted()) {
            Order bestOppositeOrder = oppositeOrders.peek();

            // Check if the orders can be matched
            if ((incomingOrder.isBuy() && incomingOrder.getPrice() >= bestOppositeOrder.getPrice()) ||
                    (!incomingOrder.isBuy() && incomingOrder.getPrice() <= bestOppositeOrder.getPrice())) {

                int matchQuantity = Math.min(incomingOrder.getRemainingQuantity(), bestOppositeOrder.getRemainingQuantity());
                double matchPrice = bestOppositeOrder.getPrice();

                // Record the trade
                trades.add(new Trade(
                        incomingOrder.isBuy() ? incomingOrder.getOrderId() : bestOppositeOrder.getOrderId(),
                        incomingOrder.isBuy() ? bestOppositeOrder.getOrderId() : incomingOrder.getOrderId(),
                        symbol,
                        matchPrice,
                        matchQuantity
                ));

                // Update quantities
                incomingOrder.execute(matchQuantity);
                bestOppositeOrder.execute(matchQuantity);

                // Remove fully executed orders
                if (bestOppositeOrder.isFullyExecuted()) {
                    oppositeOrders.poll();
                }
            } else {
                // No match possible
                break;
            }
        }
    }

    @Override
    public String toString() {
        return "OrderBook{" +
                "symbol='" + symbol + '\'' +
                ", buyOrders=" + buyOrders +
                ", sellOrders=" + sellOrders +
                '}';
    }
}
