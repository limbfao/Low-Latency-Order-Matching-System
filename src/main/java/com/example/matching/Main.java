package com.example.matching;

import com.example.matching.engine.MatchingEngine;
import com.example.matching.model.Order;
import com.example.matching.model.Trade;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        MatchingEngine engine = new MatchingEngine();

        // Example orders
        Order order1 = new Order("AAPL", true, 150.00, 100); // Buy 100 @ 150
        Order order2 = new Order("AAPL", false, 145.00, 50); // Sell 50 @ 145
        Order order3 = new Order("AAPL", false, 150.00, 75); // Sell 75 @ 150
        Order order4 = new Order("AAPL", true, 155.00, 50); // Buy 50 @ 155

        // Process orders
        List<Trade> trades1 = engine.processOrder(order1);
        List<Trade> trades2 = engine.processOrder(order2);
        List<Trade> trades3 = engine.processOrder(order3);
        List<Trade> trades4 = engine.processOrder(order4);

        // Print trades
        System.out.println("Trades for Order 1: " + trades1);
        System.out.println("Trades for Order 2: " + trades2);
        System.out.println("Trades for Order 3: " + trades3);
        System.out.println("Trades for Order 4: " + trades4);

        // Print order books
        engine.printOrderBooks();
    }
}