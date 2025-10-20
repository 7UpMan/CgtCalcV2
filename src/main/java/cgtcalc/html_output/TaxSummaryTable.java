/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cgtcalc.html_output;

/**
 *
 * @author mat
 */
public class TaxSummaryTable {

    float totalFees = 0;
    float totalProfit = 0;
    int numSellTrades = 0;
    float totalBuyGbp = 0;
    float totalSellGbp = 0;
    float totalPurchaseCost = 0;
    float openingStockValue = 0;

    // Create the output report
    /**
     * 
     * @param totalFees The total of all the fees incurred.
     * @param totalProfit
     * @param numSellTrades The number of sell trades.
     * @param totalBuyGbp Total amount spent buying stocks ex fees.
     * @param totalSellGbp Total amount spent selling stocks ex fees.
     * @param closingStockValue The price spent on stock that is carried forward to next year.
     * @param openingStockValue The price spent on stock that was brought into this year.
     */
    public TaxSummaryTable(float totalFees, float totalProfit, int numSellTrades,
            float totalBuyGbp, float totalSellGbp, float closingStockValue, float openingStockValue) {
        this.totalFees = totalFees;
        this.totalProfit = totalProfit;
        this.numSellTrades = numSellTrades;
        this.totalBuyGbp = totalBuyGbp;
        this.totalSellGbp = totalSellGbp;
        this.totalPurchaseCost = closingStockValue;
        this.openingStockValue = openingStockValue;
    }

    public String getTable() {
        StringBuilder sb = new StringBuilder();
        sb.append(tableHead());
        sb.append(tableBody());
        sb.append(tableFooter());

        return sb.toString();
    }

    private String tableBody() {
        StringBuilder sb = new StringBuilder();

        //float allowableCosts = totalBuyGbp - totalPurchaseCost + openingStockValue + totalFees;
        float allowableCosts = totalBuyGbp - totalPurchaseCost + totalFees;
        float gainsForYear = totalSellGbp - allowableCosts;

        sb.append(tableRow("Opening Stock Value", String.format("&pound;%,.2f", openingStockValue)));
        sb.append(tableRow("Closing Stock Value", String.format("&pound;%,.2f", totalPurchaseCost)));
        sb.append(tableRow("Total Buy Transactions", String.format("&pound;%,.2f", totalBuyGbp)));
        sb.append(tableRow("Total Sell Transactions", String.format("&pound;%,.2f", totalSellGbp)));
        sb.append(tableRow("Fees", String.format("&pound;%,.2f", totalFees)));
        
        sb.append(tableRow("&nbsp;", "&nbsp;"));
        
        sb.append(tableRow("Number of disposals", String.format("%,d", numSellTrades)));
        sb.append(tableRow("Disposal proceeds", String.format("&pound;%,.2f", totalSellGbp)));
        
        sb.append(tableRow("Allowable costs = Total Buy Transactions - Closing Stock Value + Fees",
                String.format("&pound;%,.2f", allowableCosts)));

        sb.append(tableRow("Gains for the year = Total Sell Transactions - Allowable Costs", String.format("&pound;%,.2f", gainsForYear)));
        
        
        
        sb.append(tableRow(String.format("Gain got the year = TotalSell &pound;%,.2f - "
                + "TotalBuy &pound;%,.2f + Closing Stock &pound;%,.2f - Fees &pound;%,.2f", 
                totalSellGbp, totalBuyGbp,totalPurchaseCost,totalFees)
                , String.format("&pound;%,.2f", totalSellGbp-totalBuyGbp+totalPurchaseCost-totalFees)));
        
        

        return sb.toString();
    }

    private StringBuilder tableRow(String name, String value) {
        StringBuilder sb = new StringBuilder();
        sb.append("     <tr>\n");
        sb.append("      <td>");
        sb.append(name);
        sb.append("</td>\n");
        sb.append("      <td class=\"text-right\">");
        sb.append(value);
        sb.append("</td>\n");
        sb.append("     </tr>\n");

        return sb;
    }

    private String tableHead() {
        StringBuilder sb = new StringBuilder();

        sb.append("<h1 id=\"tax-summary\">Tax Summary</h1>\n");
        sb.append("   <table class=\"table table-bordered table-sm table-dark\">\n");
        sb.append("    <thead>\n");
        sb.append("     <tr>\n");
        sb.append("      <th>Description</th>\n");
        sb.append("      <th>Value</th>\n");
        sb.append("     </tr>\n");
        sb.append("    </thead>\n");
        sb.append("    <tbody>\n");

        return sb.toString();
    }

    private String tableFooter() {
        StringBuilder sb = new StringBuilder();

        sb.append("    </tbody>\n");
        sb.append("   </table>\n");

        return sb.toString();
    }
}
