package com.example.matching.engine;

import com.example.matching.model.Order;
import com.example.matching.model.Trade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MatchingEngine {
    private static final Logger logger = LoggerFactory.getLogger(MatchingEngine.class);

    private final Map<String, OrderBook> orderBooks = new ConcurrentHashMap<>();

    public List<Trade> processOrder(Order order) {
        logger.info("Received order: {}", order);

        OrderBook orderBook = orderBooks.computeIfAbsent(order.getSymbol(), OrderBook::new);
        List<Trade> trades = orderBook.processOrder(order);

        trades.forEach(trade -> logger.info("Trade executed: {}", trade));
        return trades;
    }

    public void printOrderBooks() {
        orderBooks.values().forEach(orderBook -> logger.info("Order book for {}: {}", orderBook.getSymbol(), orderBook));
        // orderBooks.values().forEach(System.out::println);
    }
}