package com.example.matching.engine;

import com.example.matching.model.Order;
import com.example.matching.model.Trade;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MatchingEngine {
    private final Map<String, OrderBook> orderBooks = new HashMap<>();

    public List<Trade> processOrder(Order order) {
        OrderBook orderBook = orderBooks.computeIfAbsent(order.getSymbol(), OrderBook::new);
        return orderBook.processOrder(order);
    }

    public void printOrderBooks() {
        orderBooks.values().forEach(System.out::println);
    }
}