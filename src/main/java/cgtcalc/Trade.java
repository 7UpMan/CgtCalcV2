package cgtcalc;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.csv.CSVRecord;

/**
 *
 * @author mat
 */
public class Trade {

    private Date tradeDate = null;
    private Date settleDate = null;
    private String reference = null;
    private String description = null;
    private float unitCost;
    private float quantity;
    private float tradeValueIncFees;

    public Trade(CSVRecord scvRecord) {
        try {
            tradeDate = new SimpleDateFormat("dd/MM/yyyy").parse(scvRecord.get("Trade date"));
            settleDate = new SimpleDateFormat("dd/MM/yyyy").parse(scvRecord.get("Settle date"));
            reference = scvRecord.get("Reference");
            description = scvRecord.get("Description");
            unitCost = Tools.parseFloat(scvRecord.get("Unit cost (p)"));
            quantity = Tools.parseFloat(scvRecord.get("Quantity"));
            tradeValueIncFees = Tools.parseFloat(scvRecord.get("Value (�)"));
        } catch (ParseException ex) {
            System.out.println("Could not parse data " + scvRecord.toString());
        }
    }

    /**
     * The price in pounds (not P) spent on the purchase. This is the qty x unit
     * price <b>excluding</b> the trading fees.
     *
     * @return
     */
    public float getTradeValueExFees() {
        return unitCost * quantity / 100;
    }

    /**
     * Get the fees in GBP for the transaction.
     * @return 
     */
    public float getFees() {
        if (isBuy()) {
            return (getTradeValueIncFees() * -1) - getTradeValueExFees();
        }

        if (isSell()) {
            return getTradeValueExFees() - getTradeValueIncFees();
        }

        return 0;
    }

    /**
     * Is this a buy transaction?
     *
     * @return
     */
    public boolean isBuy() {
        return reference.startsWith("B") && Tools.isDigit(reference.charAt(1));
    }

    /**
     * Is this a sell transaction?
     *
     * @return
     */
    public boolean isSell() {
        return reference.startsWith("S") && Tools.isDigit(reference.charAt(1));
    }
    
    /**
     * True if the trade is neither a buy or sell, i.e. it is a dfferent type.
     * @return 
     */
    public boolean isOther() {
        return ! (isBuy() || isSell());
    }

    /**
     * Get the Stock Name, which is the first 2 words (separated by spaces) of the Description. Note
     * that the description provided by HL has lots of other words in it too.
     * @return 
     */
    public String getStockName() {
        int firstSpace = description.indexOf(" ");
        int secondSpace = description.indexOf(" ", firstSpace + 1);
        
        // If no second space, just return the string
        return secondSpace==-1?description:description.substring(0, secondSpace);
    }

    /**
     * @return the Trade Date
     */
    public Date getTradeDate() {
        return tradeDate;
    }

    /**
     * @return the Settle Date
     */
    public Date getSettleDate() {
        return settleDate;
    }

    /**
     * @return the reference
     */
    public String getReference() {
        return reference;
    }

    /**
     * Return the number part of the reference number.
     *
     * @return
     */
    public int getReferenceNum() {
        int retVal = 0;
        try {
            retVal = Integer.parseInt(reference.substring(1));
        } catch (NumberFormatException nfe) {
            System.err.println("Invalid reference: " + reference);
            // Do nothing
        }

        return retVal;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return the unitCost
     */
    public float getUnitCost() {
        return unitCost;
    }

    /**
     * @return the quantity
     */
    public float getQuantity() {
        return quantity;
    }

    /**
     * The price in pounds (not P) spent on the purchase. This is the qty x unit
     * price <b>including</b> the trading fees.
     *
     * @return
     */
    public float getTradeValueIncFees() {
        return tradeValueIncFees;
    }
    
    /**
     * A printable string that has most of the detail about the trade in it.
     * @return 
     */
    @Override
    public String toString() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        
        StringBuilder sb = new StringBuilder();
        if(isBuy()) {
            sb.append("Buy ");
        }
        if(isSell()) {
            sb.append("Sell ");
        }
        sb.append(getStockName());
        sb.append(" ");
        sb.append(format.format(getTradeDate()));
        sb.append(" ");
        sb.append(getReference());
        sb.append(" ");
        sb.append(getQuantity());
        sb.append(" ");
        sb.append(getUnitCost());
        sb.append(" ");
        sb.append(getTradeValueIncFees());
        sb.append(" ");
        return sb.toString();
    }

}
