/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cgtcalc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;

/**
 *
 * @author mat
 */
public class ProcessTradesMethods {

    private static final boolean DEBUG = false;
    private static final String DEBUG_NAME = "Costain";

    /**
     * Process all the trades in the reverse of the order provided. All the buy
     * transactions are processed first, followed by all of the sell
     * transactions.  <b>This is the incorrect method</b>
     *
     * @param allTradesReverse
     * @param tradingPositions
     */
    public static void method1(ArrayList<Trade> allTradesReverse, HashMap<String, TradingPosition> tradingPositions) {
        // Somewhere to hold the trades in their new order
        ArrayList<Trade> allTrades = new ArrayList<>();
        
        System.out.println("[INFO]Processing transactions in method 1 ... reverse the order of the data,"
                + " then process all \"Buy\" transactions first, then all \"Sell\" transactions.");
        System.out.println("[INFO]This is the 'non-ideal' way of doing things.");

        // Swap the order of the trades and put them into their new home
        // Capture some other things as we go
        Date lastDate = null;
        for (int i = allTradesReverse.size() - 1; i >= 0; i--) {
            Trade trade = allTradesReverse.get(i);
            allTrades.add(trade);

            // Check dates are in order
            if (lastDate == null) {
                lastDate = trade.getTradeDate();
            } else {
                if (lastDate.compareTo(trade.getTradeDate()) > 0) {
                    System.err.println("[FATAL]TradeDate not in correct order.");
                    System.err.flush();
                    System.exit(1);
                }
            }
            lastDate = trade.getTradeDate();
        }

        // Process all the buy transations
        for (Trade trade : allTrades) {
            if (trade.isBuy()) {
                String stockName = trade.getStockName();

                // Do we already have a trading position for this stock?
                TradingPosition tp = null;
                if (tradingPositions.containsKey(stockName)) {
                    // Already there, so fetch existing item.
                    tp = tradingPositions.get(stockName);

                } else {
                    tp = new TradingPosition();
                    tradingPositions.put(stockName, tp);
                }

                tp.buyStock(trade);
                
                // Print debug if we need it
                printDebug(trade, tp);
            }
        }

        // Process all the sell transactions
        for (Trade trade : allTrades) {
            if (trade.isSell()) {
                String stockName = trade.getStockName();

                // Do we already have a trading position for this stock?
                TradingPosition tp = null;
                if (!tradingPositions.containsKey(stockName)) {
                    // Should be there
                    System.err.println("[FATAL]Trying to sell something that doesn't exist: " + trade.getStockName() + " " + trade.getReference());
                    System.err.flush();
                    System.exit(1);

                }
                tp = tradingPositions.get(stockName);

                tp.sellStock(trade);

            // Print debug if we need it
            printDebug(trade, tp);
            }
        }
    }

    /**
     * Process all of the transactions in order of date and buy/sell within
     * that. The transactions are sorted by Transaction Date, and where 2
     * transactions are on the same day, then the buy transactions are sorted to
     * be before the sell ones.<b>This is the correct method</b>
     *
     * @param allTrades
     * @param tradingPositions
     */
    public static void method2(ArrayList<Trade> allTrades, HashMap<String, TradingPosition> tradingPositions) {
        System.out.println("[INFO]Processing transactions in method 2 ... sort by date and within "
                + "that \"Buy\" transactions first, then process top to bottom.");
        System.out.println("[INFO]This is the way things should be done.");

        // Sort the data first
        Collections.sort(allTrades, new Comparator<Trade>() {
            @Override
            public int compare(Trade lhs, Trade rhs) {
                // -1 - less than, 1 - greater than, 0 - equal, all inversed for descending

                // If dates are the same then we sort "buy" before "sell"
                if (lhs.getTradeDate().equals(rhs.getTradeDate())) {
                    if (lhs.isBuy() && rhs.isBuy()) {
                        return 0;
                    } else if (lhs.isBuy() && rhs.isSell()) {
                        return -1;
                    } else {
                        return 1;
                    }
                } else {
                    return (lhs.getTradeDate().compareTo(rhs.getTradeDate()));
                }
            }
        });

        // Check the order is ok
        Trade lastTrade = null;

        for (Trade trade : allTrades) {

            // Check dates are in order
            if (lastTrade == null) {
                // Do nothing
            } else {
                if (lastTrade.getTradeDate().equals(trade.getTradeDate())) {
                    if (lastTrade.isSell() && trade.isBuy()) {
                        System.err.println("[FATAL]Sells before buys on day " + lastTrade.getTradeDate());
                        System.exit(1);
                    }
                }

                if (lastTrade.getTradeDate().compareTo(trade.getTradeDate()) > 0) {
                    System.err.println("[FATAL]TradeDate not in correct order.");
                    System.err.flush();
                    System.exit(1);
                }
            }
            lastTrade = trade;
        }

        // Process all the buy transations
        for (Trade trade : allTrades) {
//            if(!trade.getStockName().equalsIgnoreCase("Aston Martin")) {
//                continue;
//            }
            
            TradingPosition tp = null;

            if (trade.isBuy()) {
                String stockName = trade.getStockName();

                // Do we already have a trading position for this stock?
                if (tradingPositions.containsKey(stockName)) {
                    // Already there, so fetch existing item.
                    tp = tradingPositions.get(stockName);
                } else {
                    tp = new TradingPosition();
                    tradingPositions.put(stockName, tp);
                }

                tp.buyStock(trade);
            }

            if (trade.isSell()) {
                String stockName = trade.getStockName();

                // Do we already have a trading position for this stock?
                if (!tradingPositions.containsKey(stockName)) {
                    // Should be there
                    System.err.println("[FATAL]Trying to sell something that doesn't exist: " + trade.getStockName() + " " + trade.getReference());
                    System.err.flush();
                    System.exit(1);

                }
                tp = tradingPositions.get(stockName);

                tp.sellStock(trade);

            }
            
            // Print debug if we need it
            printDebug(trade, tp);
        }
    }

    private static void printDebug(Trade trade, TradingPosition tp) {
        if (DEBUG) {

            if (trade.getStockName().startsWith(DEBUG_NAME)) {
                System.out.println(trade.toString());
                System.out.println(tp.toString());
                System.out.flush();
            }
        }
    }

}
