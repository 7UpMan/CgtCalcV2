package cgtcalc.html_output;

import cgtcalc.Trade;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 *
 * @author mat
 */
public class WarningsTable {

    // The output file handler
    private final SimpleDateFormat dateFormat;
    
    List<Trade> warningTrades;


    // Create the output report
    public WarningsTable(List<Trade> warningTrades) {
        this.dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        this.warningTrades = warningTrades;
    }
    
    public String getTable() {
        StringBuilder sb = new StringBuilder();
        
        sb.append(tableHead());
        
        // Process the table rows
        for(Trade trade: warningTrades) {
            sb.append(getTableRow(trade));
        }

        sb.append(tableFooter());
        
        return sb.toString();
    }

    private String tableHead() {
        StringBuilder sb = new StringBuilder();
        
        sb.append("<h2 id=\"warnings\">");
        sb.append("Warnings and manual modifications");
        sb.append("</h2>\n");
        sb.append("   <table class=\"table table-bordered table-sm table-dark\">\n");
        sb.append("    <thead>\n");
        sb.append("     <tr>\n");
        sb.append("      <th>Date</th>\n");
        sb.append("      <th>Reference</th>\n");
        sb.append("      <th>Description</th>\n");
        sb.append("      <th>Qty</th>\n");
        sb.append("      <th>Unit Cost &pound;</th>\n");
        sb.append("      <th>Spend</th>\n");
        sb.append("      <th>Fees</th>\n");
        sb.append("     </tr>\n");
        sb.append("    </thead>\n");
        sb.append("    <tbody>\n");
        
        return sb.toString();
    }


    private String getTableRow(Trade trade) {
        StringBuilder sb = new StringBuilder();
        
        sb.append("     <tr>\n");
        sb.append("      <td>");
        sb.append(dateFormat.format(trade.getTradeDate()));
        sb.append("      </td>");
        sb.append("      <td>");
        sb.append(trade.getReference());
        sb.append("      </td>\n");
        sb.append("      <td>");
        sb.append(trade.getDescription());
        sb.append("      </td>\n");
        sb.append("      <td class=\"text-right\">");
        sb.append(String.format("%,.0f", trade.getQuantity()));
        sb.append("      </td>\n");
        sb.append("      <td class=\"text-right\">");
        sb.append(String.format("&pound;%,.4f", trade.getUnitCost() / 100));
        sb.append("      </td>\n");
        sb.append("      <td class=\"text-right\">");
        sb.append(String.format("&pound;%,.2f", trade.getTradeValueIncFees()));
        sb.append("      </td>\n");
        sb.append("      <td class=\"text-right\">");
        sb.append(String.format("&pound;%,.2f", trade.getFees()));
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
    
//    public static String getSectionHeading() {
//        return ("<h1 id=\"transaction-detail\">Transaction Detail</h1>\n");
//    }
}
