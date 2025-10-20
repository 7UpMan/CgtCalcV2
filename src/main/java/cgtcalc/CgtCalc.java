package cgtcalc;

import cgtcalc.html_output.ContentsTable;
import cgtcalc.html_output.Page;
import cgtcalc.html_output.StockDetailTable;
import cgtcalc.html_output.TaxSummaryTable;
import cgtcalc.html_output.TransactionSummaryTable;
import cgtcalc.html_output.WarningsTable;
import cgtcalc.json_Tools.Configuration;

import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import org.apache.commons.csv.CSVRecord;

/**
 *
 * @author mat
 */
public class CgtCalc {

    // Something to handle the csv files
    private final FileHandler fileHandler;
    
    private float openingStockValue = 0;

    public static void main(String[] args) {

        // Somewhere to hold the trades
        ArrayList<Trade> allTrades = new ArrayList<>();
        ArrayList<Trade> warningTrades = new ArrayList<>();

        // Somewhere to hold the trading positions
        HashMap<String, TradingPosition> tradingPositions = new HashMap<>();

        CgtCalc cgt = new CgtCalc();

        // Load the configuration
        Configuration config = cgt.getConfig(args);
        String transactionsFileName = config.getTransactionFile();
        String openingPositionsFileName = config.getOpeningPositionsFile();
        String closingPositionsFileName = config.getClosingPositionsFile();
        String htmlReportFileName = config.getHtmlReportFile();
        String modsFileName = config.getModsFile();
        int calculationMethod = config.getCalculationMethod();
        

        // Load opening balances if we have them
        String openingHtml = cgt.loadOpeningPositions(tradingPositions, openingPositionsFileName);

        // Read trades for this year
        cgt.readTransactionFile(transactionsFileName, allTrades, warningTrades);
        
        // Add any mods if they exist
        if(modsFileName != null && !modsFileName.isEmpty()) {
            cgt.readTransactionFile(modsFileName, allTrades, warningTrades);
        }

        // Process those trades
        cgt.processTransactionFile(tradingPositions, allTrades, calculationMethod);

        // Generate the reports
        cgt.generateReport(tradingPositions, closingPositionsFileName, openingHtml, 
                htmlReportFileName, calculationMethod, warningTrades);
    }

    /**
     * @param args the command line arguments
     */
    private CgtCalc() {
        this.fileHandler = new FileHandler();
    }

    /**
     * Read the JSON configuration file which is specified as args[0].
     * @param args
     * @return 
     */
    private Configuration getConfig(String[] args) {

        if (args.length != 1) {

            System.err.print("Incorrect usage: "+this.getClass().getName());
            for(String s: args) {
                System.err.printf(" %s",s);
            }
            System.err.println();
            System.err.println("There were " + args.length + " arguments");
            System.err.println("Usage: " + this.getClass().getName() + " configFilePath");
            System.exit(1);
        }

        String configJson = fileHandler.readFile(args[0]);

        Gson gson = new Gson();

        return gson.fromJson(configJson, Configuration.class);
    }

    /**
     * Read in opening positions.  When this tool is run it will generate a "Closing Positions"
     * file that hold information about the stocks held at the end of a year along with the 
     * price paid for them.  That then becomes the input file to the following year as the "Opening Positions" file.
     * Confusingly the file is usually called "Closing Positions".<br>If the file name
     * is null or empty, then no file is loaded.<br>A table of the transactions is
     * also added to the HTML output.
     * @param tradingPositions
     * @param fileName
     * @return 
     */
    private String loadOpeningPositions(HashMap<String, TradingPosition> tradingPositions, String fileName) {
        // Only open the file if we were given a name
        if (fileName == null || fileName.isEmpty()) {
            return "";
        }

        // Open the file
        fileHandler.openPreviousClosingPositions(fileName);

        // A table for the opening balances
        TransactionSummaryTable tst = new TransactionSummaryTable().setTitle("Opening Positions","opening-positions").setStockHyperlink(false);
        
        // Read all the records
        for (CSVRecord cdvRecord : fileHandler.getPreviousClosingPositionRecords()) {
            TradingPosition tp = new TradingPosition(cdvRecord);
//            if(!tp.getStockName().equalsIgnoreCase("VanEck Investments")) {
//                continue;
//            }

            // Only store the position if the qty is not zero
            if (tp.getQtyOfStock() != 0) {
                tradingPositions.put(tp.getStockName(), tp);

                tst.addRow(tp);
                openingStockValue += tp.getPricePaid();
            }
        }
        
        tst.addTotals(0, 0, openingStockValue);

        // Return the opening positions HTML table
        return tst.getTable();
    }
    
    /**
     * Reads a transaction file from HL and returns an ArrayList of processed items.
     * Each item is of class Trade.  The top of the HL file (which is not in CSV format) is
     * skipped.
     * @param fileName
     * @param allTrades
     * @param warningTrades 
     */
    private void readTransactionFile(String fileName, ArrayList<Trade> allTrades, ArrayList<Trade> warningTrades) {
        // Open file and sktip header
        fileHandler.openTransactionFile(fileName);


        // Get an array of all the trades
        for (CSVRecord csvRecord : fileHandler.getTransactionRecords()) {
            Trade trade = new Trade(csvRecord);

            // Need to skip 1 record because not sure how to handle the sale of a right to a rights issue
            if (trade.getReference().equals("S846779102")) {
                continue;
            }
            
//            if(!trade.getStockName().equalsIgnoreCase("VanEck Investments")) {
//                continue;
//            }

            // We only process buy or sell transactions; we ignore the rest
            if (trade.isBuy() || trade.isSell()) {
                // Only store buy and sell trades
                allTrades.add(trade);
            }
            
            // We need to report some transactions types for further analysis
            if (trade.isOther() || trade.getReferenceNum() == 0) {
                warningTrades.add(trade);
                System.out.println("[WARN]Skipping transacion: "+trade);
            }
        }

        fileHandler.closeTransactionfile();
    }

    private void processTransactionFile(HashMap<String, TradingPosition> tradingPositions, 
            ArrayList<Trade> allTrades,
            int calculationMethod) {

        // Process the transactions based on chosen method
        switch (calculationMethod) {
            case 1 -> ProcessTradesMethods.method1(allTrades, tradingPositions);
            case 2 -> ProcessTradesMethods.method2(allTrades, tradingPositions);
            default -> {
                System.err.println("[ERR]Invalid calculation method "+ calculationMethod);
                System.exit(1);
            }
        }

    }

    private void generateReport(HashMap<String, TradingPosition> tradingPositions,
            String closingPositionsFileName, String openingHtml, String htmlFileName,
            int calculationMethod, ArrayList<Trade> warningTrades) {

        // Some counters taht we need
        int processedTrades = 0;
        int numSellTrades = 0;
        float totalProfit = 0;
        float totalFees = 0;
        float totalPurchaseCost = 0;
        float totalBuySpend = 0;
        float totalSellSpend = 0;


        // We want to do this in alphabetical order, so get the current list of keys
        ArrayList<String> tradingPositionKeys = new ArrayList<>();
        tradingPositionKeys.addAll(tradingPositions.keySet());

        // Now sort the keys
        Collections.sort(tradingPositionKeys, new Comparator<String>() {
            @Override
            public int compare(String lhs, String rhs) {
                // -1 - less than, 1 - greater than, 0 - equal, all inversed for descending

                return (lhs.compareTo(rhs));

            }
        });

        // Somewhere to store the tables of trades
        StringBuilder tradesDetail = new StringBuilder();
        tradesDetail.append(StockDetailTable.getSectionHeading());

        TransactionSummaryTable tst = new TransactionSummaryTable().setTitle("Closing Positions","closing-positions").setStockHyperlink(true);

        for (String stockName : tradingPositionKeys) {
            TradingPosition tp = tradingPositions.get(stockName);
            tst.addRow(tp);

            tradesDetail.append(new StockDetailTable(tp).getTable());

            fileHandler.writeClosingPosition(closingPositionsFileName, tp);

            // Update some counters
            processedTrades += tp.getNumTrades();
            numSellTrades += tp.getNumSell();
            totalProfit += tp.getProfit();
            totalFees += tp.getTradingFees();
            totalPurchaseCost += tp.getPricePaid();
            totalBuySpend += tp.getTotalBuySpend();
            totalSellSpend += tp.getTotalSellSpend();
        }
        
        // Add summary table totals
        tst.addTotals(totalFees, totalProfit, totalPurchaseCost);
        
        // Calculate the tax position
        TaxSummaryTable txSt = new TaxSummaryTable( totalFees,  totalProfit,  numSellTrades,
             totalBuySpend,  totalSellSpend,  totalPurchaseCost, openingStockValue);
        String taxTable = txSt.getTable();

        // Table of contents
        ContentsTable toc = new ContentsTable();
        
        // Warnings
        WarningsTable wt = new WarningsTable(warningTrades);
        
        
        // Put everything that you have together into an HTML report
        Page page = new Page(calculationMethod);
        page.addSegment(toc.getTable());
        page.addSegment(taxTable);
        page.addSegment(openingHtml);
        page.addSegment(tst.getTable());
        page.addSegment(tradesDetail.toString());
        page.addSegment(wt.getTable());
        fileHandler.writeHtmlOutput(htmlFileName, page.getPage());

        // Close the Closing Positions file
        fileHandler.closeClosingPosition();

        // Print some counters
        System.out.println("[INFO]Total  fees: £" + String.format("%,.2f", totalFees));
        System.out.println("[INFO]There were " + String.format("%,d", processedTrades) + " trades processed");
        System.out.println("[INFO]Total proffit was (ex fees) £" + String.format("%,.2f", totalProfit));
    }
}
