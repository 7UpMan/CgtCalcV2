/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cgtcalc;

/**
 *
 * @author mat
 */
public class TradingPositionPart {

    private String stockName;
    private float qtyOfStock;
    private float pricePaid;
    private float tradingFees;
    private float totalBuySpend;
    private float profit;

    public TradingPositionPart(String stockName, float qtyOfStock, float pricePaid,
            float tradingFees, float totalBuySpend, float profit) {
        this.stockName = stockName;
        this.qtyOfStock = qtyOfStock;
        this.pricePaid = pricePaid;
        this.tradingFees = tradingFees;
        this.totalBuySpend = totalBuySpend;
        this.profit = profit;
    }

    /**
     * @return the stockName
     */
    public String getStockName() {
        return stockName;
    }

    /**
     * @param stockName the stockName to set
     */
    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    /**
     * @return the qtyOfStock
     */
    public float getQtyOfStock() {
        return qtyOfStock;
    }

    /**
     * @param qtyOfStock the qtyOfStock to set
     */
    public void setQtyOfStock(float qtyOfStock) {
        this.qtyOfStock = qtyOfStock;
    }

    /**
     * @return the pricePaid
     */
    public float getPricePaid() {
        return pricePaid;
    }

    /**
     * @param pricePaid the pricePaid to set
     */
    public void setPricePaid(float pricePaid) {
        this.pricePaid = pricePaid;
    }

    /**
     * @return the tradingFees
     */
    public float getTradingFees() {
        return tradingFees;
    }

    /**
     * @param tradingFees the tradingFees to set
     */
    public void setTradingFees(float tradingFees) {
        this.tradingFees = tradingFees;
    }

    /**
     * @return the totalBuySpend
     */
    public float getTotalBuySpend() {
        return totalBuySpend;
    }

    /**
     * @param totalBuySpend the totalBuySpend to set
     */
    public void setTotalBuySpend(float totalBuySpend) {
        this.totalBuySpend = totalBuySpend;
    }
    
    public float getProfit() {
        return profit;
    }
    
    /**
     * Returns the average price paid for the stock.
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
}
