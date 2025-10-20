/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cgtcalc.html_output;

import cgtcalc.Tools;
import cgtcalc.TradingPosition;

/**
 *
 * @author mat
 */
public class TransactionSummaryTable {

    // The summary of trades
    private StringBuilder reportBuffer;
    String reportTitle;
    String reportId;
    boolean addStockHyperlink = false;
    float totalFees = 0;
    float totalProfit = 0;
    boolean addTotals = false;
    float totalPurchaseCost = 0;

    // Create the output report
    public TransactionSummaryTable() {
        this.reportBuffer = new StringBuilder();
        reportTitle = "Trading Summary";
        reportId = "trading-summary";
    }

    public String getTable() {
        StringBuilder sb = new StringBuilder();
        sb.append(tableHead());
        sb.append(reportBuffer);
        sb.append(tableFooter());

        return sb.toString();
    }

    /**
     * Set the title for this section along with the HTML id that section.
     * @param title
     * @param id
     * @return 
     */
    public TransactionSummaryTable setTitle(String title, String id) {
        reportTitle = title;
        reportId = id;

        return this;
    }

    public TransactionSummaryTable setStockHyperlink(boolean hyperLink) {
        addStockHyperlink = hyperLink;

        return this;
    }

    private String tableHead() {
        StringBuilder sb = new StringBuilder();

        sb.append("<h1 id=\"");
        sb.append(reportId);
        sb.append("\">");
        sb.append(reportTitle);
        sb.append("</h1>\n");
        sb.append("   <table class=\"table table-bordered table-sm table-dark\">\n");
        sb.append("    <thead>\n");
        sb.append("     <tr>\n");
        sb.append("      <th>Stock</th>\n");
        sb.append("      <th>Total Fees</th>\n");
        sb.append("      <th>Total Held</th>\n");
        sb.append("      <th>Avg Cost &pound;</th>\n");
        sb.append("      <th>Purchase Cost &pound;</th>\n");
        sb.append("      <th>Total Profit ex Fees</th>\n");
        sb.append("     </tr>\n");
        sb.append("    </thead>\n");
        sb.append("    <tbody>\n");

        return sb.toString();
    }

    /**
     * Adds a summary table row to the buffer.
     *
     * @param tp
     */
    public void addRow(TradingPosition tp) {
        reportBuffer.append("     <tr>\n");
        reportBuffer.append("      <td>");
        if (addStockHyperlink) {
            reportBuffer.append("      <a href=\"#");
            reportBuffer.append(Tools.space2minus(tp.getStockName()));
            reportBuffer.append("\">");
        } else {
            reportBuffer.append("      ");
        }
        reportBuffer.append(tp.getStockName());
        if (addStockHyperlink) {
            reportBuffer.append("</a>");
        }
        reportBuffer.append("</td>\n");
        reportBuffer.append("      <td class=\"text-right\">");
        reportBuffer.append(String.format("&pound;%,.2f", tp.getTradingFees()));
        reportBuffer.append("</td>\n");
        reportBuffer.append("      <td class=\"text-right\">");
        reportBuffer.append(String.format("%,.0f", tp.getQtyOfStock()));
        reportBuffer.append("</td>\n");
        reportBuffer.append("      <td class=\"text-right\">");
        reportBuffer.append(String.format("&pound;%,.2f", tp.getAvgCost()));
        reportBuffer.append("</td>\n");
        reportBuffer.append("      <td class=\"text-right\">");
        reportBuffer.append(String.format("&pound;%,.2f", tp.getPricePaid()));
        reportBuffer.append("</td>\n");
        reportBuffer.append("      <td class=\"text-right\">");
        reportBuffer.append(String.format("&pound;%,.2f", tp.getProfit()));
        reportBuffer.append("</td>\n");
        reportBuffer.append("     </tr>\n");
    }

    public void addTotals(float totalFees, float totalProfit, float totalPurchaseCost) {
        this.totalFees = totalFees;
        this.totalProfit = totalProfit;
        this.totalPurchaseCost = totalPurchaseCost;
        addTotals = true;

    }

    private String tableFooter() {
        StringBuilder sb = new StringBuilder();

        sb.append("    </tbody>\n");
        if (addTotals) {
            // Total of columns
            sb.append("    <tfoot>\n");
            sb.append("     <tr>\n");
            sb.append("      <td>Totals</td>\n");
            sb.append("      <td class=\"text-right\">");
            sb.append(String.format("&pound;%,.2f", totalFees));
            sb.append("</td>\n");
            sb.append("      <td class=\"text-right\">");
            sb.append("&nbsp;");
            sb.append("</td>\n");
            sb.append("      <td class=\"text-right\">");
            sb.append("&nbsp;");
            sb.append("</td>\n");
            sb.append("      <td class=\"text-right\">");
            sb.append(String.format("&pound;%,.2f", totalPurchaseCost));
            sb.append("</td>\n");
            sb.append("      <td class=\"text-right\">");
            sb.append(String.format("&pound;%,.2f", totalProfit));
            sb.append("</td>\n");
            sb.append("     </tr>\n");
            
            // Total Profit
            sb.append("     <tr>\n");
            sb.append("      <td>Total Profit (Profit - Fees)</td>\n");
            sb.append("      <td class=\"text-right\">");
            sb.append("&nbsp;");
            sb.append("</td>\n");
            sb.append("      <td class=\"text-right\">");
            sb.append("&nbsp;");
            sb.append("</td>\n");
            sb.append("      <td class=\"text-right\">");
            sb.append("&nbsp;");
            sb.append("</td>\n");
            sb.append("      <td class=\"text-right\">");
            sb.append("&nbsp;");
            sb.append("</td>\n");
            sb.append("      <td class=\"text-right\">");
            sb.append(String.format("&pound;%,.2f", totalProfit-totalFees));
            sb.append("</td>\n");
            sb.append("     </tr>\n");
            
            sb.append("    </tfoot>\n");
        }
        
        sb.append("   </table>\n");

        return sb.toString();
    }
}
