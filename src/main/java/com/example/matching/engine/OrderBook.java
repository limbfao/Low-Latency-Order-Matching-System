package com.example.matching.engine;

import com.example.matching.model.Order;
import com.example.matching.model.Trade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class OrderBook {
    private static final Logger logger = LoggerFactory.getLogger(OrderBook.class);

    private final String symbol;

    private final PriorityQueue<Order> buyOrders = new PriorityQueue<>(
            (o1, o2) -> {
                if (o1.getPrice() != o2.getPrice()) {
                    return Double.compare(o2.getPrice(), o1.getPrice());
                }
                return Long.compare(o1.getOrderId(), o2.getOrderId());
            });

    private final PriorityQueue<Order> sellOrders = new PriorityQueue<>(
            (o1, o2) -> {
                if (o1.getPrice() != o2.getPrice()) {
                    return Double.compare(o1.getPrice(), o2.getPrice());
                }
                return Long.compare(o1.getOrderId(), o2.getOrderId());
            });

    public OrderBook(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

    public List<Trade> processOrder(Order incomingOrder) {
        logger.info("Processing new order: {}", incomingOrder);

        List<Trade> trades = new ArrayList<>();

        if (incomingOrder.isBuy()) {
            synchronized (buyOrders) {
                matchOrders(incomingOrder, sellOrders, trades);
                if (!incomingOrder.isFullyExecuted()) {
                    buyOrders.add(incomingOrder);
                    logger.info("Added to buy orders: {}", incomingOrder);
                }
            }
        } else {
            synchronized (sellOrders) {
                matchOrders(incomingOrder, buyOrders, trades);
                if (!incomingOrder.isFullyExecuted()) {
                    sellOrders.add(incomingOrder);
                    logger.info("Added to sell orders: {}", incomingOrder);
                }
            }
        }

        return trades;
    }

    private void matchOrders(Order incomingOrder, PriorityQueue<Order> oppositeOrders, List<Trade> trades) {
        while (!oppositeOrders.isEmpty() && !incomingOrder.isFullyExecuted()) {
            Order bestOppositeOrder = oppositeOrders.peek();

            if ((incomingOrder.isBuy() && incomingOrder.getPrice() >= bestOppositeOrder.getPrice()) ||
                    (!incomingOrder.isBuy() && incomingOrder.getPrice() <= bestOppositeOrder.getPrice())) {

                int matchQuantity = Math.min(incomingOrder.getRemainingQuantity(), bestOppositeOrder.getRemainingQuantity());
                double matchPrice = bestOppositeOrder.getPrice();

                trades.add(new Trade(
                        incomingOrder.isBuy() ? incomingOrder.getOrderId() : bestOppositeOrder.getOrderId(),
                        incomingOrder.isBuy() ? bestOppositeOrder.getOrderId() : incomingOrder.getOrderId(),
                        symbol,
                        matchPrice,
                        matchQuantity
                ));

                logger.info("Trade executed: {} -> {}", incomingOrder, bestOppositeOrder);

                incomingOrder.execute(matchQuantity);
                bestOppositeOrder.execute(matchQuantity);

                if (bestOppositeOrder.isFullyExecuted()) {
                    oppositeOrders.poll();
                    logger.info("Order fully executed and removed: {}", bestOppositeOrder);
                }
            } else {
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