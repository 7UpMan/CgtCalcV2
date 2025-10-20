/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cgtcalc;

import java.util.ArrayList;
import org.apache.commons.csv.CSVRecord;

/**
 *
 * @author mat
 */
public class TradingPosition {

    private String stockName = null;
    private float qtyOfStock = 0;
    private float pricePaid = 0;
    private float profit = 0;
    private int numTrades = 0;
    private int numBuy = 0;
    private int numSell = 0;
    private float tradingFees = 0;
    private float totalBuySpend = 0;
    private float totalSellSpend = 0;
    private final ArrayList<Trade> tradeList;
    private ArrayList<TradingPositionPart> tradingPositionParts;

    /**
     * General constructor.
     */
    public TradingPosition() {
        this.tradeList = new ArrayList<>();
        this.tradingPositionParts = new ArrayList<>();
    }

    /**
     * Constructor used when you are loading an opening position from a previous year.  This is
     * essentially a buy transaction.
     * @param record 
     */
    public TradingPosition(CSVRecord record) {
        this.tradeList = new ArrayList<>();
        this.tradingPositionParts = new ArrayList<>();
        this.stockName = record.get("Stock Name");
        this.qtyOfStock = Tools.parseFloat(record.get("Qty"));
        this.pricePaid = Tools.parseFloat(record.get("AvgPrice(GBP)")) * qtyOfStock;
        this.totalBuySpend = this.pricePaid;
    }

    /**
     * Add a trade to the current stock position.  This will increate the quantity of
     * stock on hand, and the total price paid for the stock.
     *
     * @param trade
     */
    public void buyStock(Trade trade) {
        qtyOfStock += trade.getQuantity();
        pricePaid += trade.getTradeValueExFees();
        stockName = trade.getStockName();
        numTrades++;
        numBuy ++;
        tradingFees += trade.getFees();
        totalBuySpend += trade.getTradeValueExFees();
        tradeList.add(trade);
        tradingPositionParts.add(new TradingPositionPart(stockName, qtyOfStock,pricePaid,tradingFees,totalBuySpend, profit));
    }

    /**
     * Take a trade off the current stock position.  This will generate a profit (or loss),
     * reduce the quantity on hand, reduce the price paid for the stock, but will not
     * affect the average price paid for it.
     * 
     * @param trade 
     */
    public void sellStock(Trade trade) {
        // Get the AvgCost so it doesn't change within funciton
        float avgCost = getAvgCost();

        // Record the profit
        float lineProfit = trade.getTradeValueExFees() - avgCost * trade.getQuantity();
        profit += lineProfit;

        // Adjust the qty
        qtyOfStock -= trade.getQuantity();

        // Reduce the price paid, making sure the avg price doesn't change
        pricePaid -= trade.getQuantity() * avgCost;
        pricePaid = Tools.round(pricePaid, 4);

        // Check we have not sold more than we have
        if (qtyOfStock < 0 || (int) (pricePaid * 100) < 0) {
            System.out.println("[ERROR]Qty or price gone negative for " + trade.getReference() + " " + stockName 
                    + " qty: " + qtyOfStock + " price: " + pricePaid);
            System.out.flush();
        }

        numTrades++;
        numSell ++;
        tradingFees += trade.getFees();
        totalSellSpend += trade.getTradeValueExFees();
        tradeList.add(trade);
        tradingPositionParts.add(new TradingPositionPart(stockName, qtyOfStock,pricePaid,tradingFees,totalBuySpend, profit));
    }

    /**
     * Returns the average price paid for the stock in GBP.
     *
     * @return
     */
    public float getAvgCost() {
        // If we don't hold any, return 0.
        if (qtyOfStock == 0) {
            return 0;
        }

        return pricePaid / qtyOfStock;
    }

    /**
     * How many trades does this position represent.
     *
     * @return
     */
    public int getNumTrades() {
        return numTrades;
    }

    /**
     * The total profit generated so far for this trading position.
     * @return 
     */
    public float getProfit() {
        return profit;
    }

    /**
     * Get the total trading fees so far on this position.
     * @return 
     */
    public float getTradingFees() {
        return tradingFees;
    }

    /**
     * The name of the stock.
     * @return 
     */
    public String getStockName() {
        return stockName;
    }

    /**
     * The current quantity of stock in this position.
     * @return 
     */
    public float getQtyOfStock() {
        return qtyOfStock;
    }

    /**
     * The total price paid for the stock that is current in the position.
     * @return the pricePaid
     */
    public float getPricePaid() {
        return Tools.round(pricePaid, 2);
    }

    /**
     * Total spend on Buy transactions for the current position.
     * @return 
     */
    public float getTotalBuySpend() {
        return totalBuySpend;
    }

    /**
     * Total funds received for all the Sell transactions for the current position.
     * @return 
     */
    public float getTotalSellSpend() {
        return totalSellSpend;
    }

    /**
     * A list of all the trades that make up the current position.
     * @return 
     */
    public ArrayList<Trade> getTradeList() {
        return tradeList;
    }
    
    /**
     * An array with one object per Trade, (matching getTradeList() ) that holds
     * the calculated items for that Trade.
     * @return 
     */
    public ArrayList<TradingPositionPart> getTradingPositionParts() {
        return tradingPositionParts;
    }
    
    /**
     * Total number of Buy Trades.
     * @return 
     */
    public int getNumBuy() {
        return numBuy;
    }
    
    /**
     * Total number of Sell Trades.
     * @return 
     */
    public int getNumSell() {
        return numSell;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(stockName);
        sb.append(", ");
        sb.append("Qty: ");
        sb.append(qtyOfStock);
        sb.append(", ");
        sb.append("Total spend: ");
        sb.append(pricePaid);
        sb.append(", ");
        sb.append("Profit: ");
        sb.append(profit);
        sb.append(", ");
        sb.append("Average cost: ");
        sb.append(getAvgCost());
        return sb.toString();
    }
}
