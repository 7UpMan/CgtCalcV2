/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cgtcalc.html_output;

import cgtcalc.Tools;
import cgtcalc.Trade;
import cgtcalc.TradingPosition;
import cgtcalc.TradingPositionPart;
import java.text.SimpleDateFormat;

/**
 *
 * @author mat
 */
public class StockDetailTable {

    // The output file handler
    private final SimpleDateFormat dateFormat;

    // The detail of the tranactions
    //private final StringBuffer tradesTable;
    
    private final TradingPosition tp;

    // Create the output report
    public StockDetailTable(TradingPosition tradingPosition) {
        tp = tradingPosition;
        //this.tradesTable = new StringBuffer();
        this.dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    }
    
    public String getTable() {
        StringBuilder sb = new StringBuilder();
        
        sb.append(tableHead());
        
        // Process the table rows
        for(int i = 0; i< tp.getTradeList().size(); i++) {
            Trade trade = tp.getTradeList().get(i);
            TradingPositionPart tpp = tp.getTradingPositionParts().get(i);
            sb.append(getTableRow(trade, tpp));
        }

        sb.append(tableFooter());
        
        return sb.toString();
    }

    private String tableHead() {
        StringBuilder sb = new StringBuilder();
        
        sb.append("<h2 id=\"");
        sb.append(Tools.space2minus(tp.getStockName()));
        sb.append("\">");
        sb.append("Information for stock: ");
        sb.append(tp.getStockName());
        sb.append("</h2>\n");
        sb.append("   <table class=\"table table-bordered table-sm table-dark\">\n");
        sb.append("    <thead>\n");
        sb.append("     <tr>\n");
        sb.append("      <th>Date</th>\n");
        sb.append("      <th>Buy / Sell</th>\n");
        sb.append("      <th>Qty</th>\n");
        sb.append("      <th>Unit Cost &pound;</th>\n");
        sb.append("      <th>Spend</th>\n");
        sb.append("      <th>Fees</th>\n");
        sb.append("      <th>Total Held</th>\n");
        sb.append("      <th>Avg Cost &pound;</th>\n");
        sb.append("      <th>Line Profit</th>\n");
        sb.append("      <th>Total Profit</th>\n");
        sb.append("     </tr>\n");
        sb.append("    </thead>\n");
        sb.append("    <tbody>\n");
        
        return sb.toString();
    }


    private String getTableRow(Trade trade, TradingPositionPart tpp) {
        StringBuilder sb = new StringBuilder();
        
        sb.append("     <tr>\n");
        sb.append("      <td>");
        sb.append(dateFormat.format(trade.getTradeDate()));
        sb.append("      </td>");
        sb.append("      <td>");
        sb.append(trade.isBuy() ? "Buy" : trade.isSell() ? "Sell" : "");
        sb.append("      </td>\n");
        sb.append("      <td class=\"text-right\">");
        sb.append(String.format("%,.0f", trade.getQuantity()));
        sb.append("      </td>\n");
        sb.append("      <td class=\"text-right\">");
        sb.append(String.format("&pound;%,.4f", trade.getUnitCost() / 100));
        sb.append("      </td>\n");
        sb.append("      <td class=\"text-right\">");
        sb.append(String.format("&pound;%,.2f", trade.getTradeValueExFees()));
        sb.append("      </td>\n");
        sb.append("      <td class=\"text-right\">");
        sb.append(String.format("&pound;%,.2f", trade.getFees()));
        sb.append("      </td>\n");
        sb.append("      <td class=\"text-right\">");
        sb.append(String.format("%,.0f", tpp.getQtyOfStock()));
        sb.append("      </td>\n");
        sb.append("      <td class=\"text-right\">");
        sb.append(String.format("&pound;%,.2f", tpp.getAvgCost()));
        sb.append("      </td>\n");
        sb.append("      <td class=\"text-right\">");
        sb.append(String.format("&pound;%,.2f", tpp.getProfit()));
        sb.append("      </td>\n");
        sb.append("      <td class=\"text-right\">");
        sb.append(String.format("&pound;%,.2f", tpp.getProfit()));
        sb.append("      </td>\n");
        sb.append("     </tr>\n");
        
        return sb.toString();
    }

    private String tableFooter() {
        StringBuilder sb = new StringBuilder();
        sb.append("    </tbody>\n");
        sb.append("   </table>\n");
        
        return sb.toString();
    }
    
    public static String getSectionHeading() {
        return ("<h1 id=\"transaction-detail\">Transaction Detail</h1>\n");
    }
}
