package cgtcalc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

/**
 *
 * @author mat
 */
public class FileHandler {

    private Reader transactionFile = null;
    private Reader previousClosingPosition = null;
    private CSVPrinter closingPositions = null;
    private final String[] closingPositionsHeader = {"Stock Name", "Profit", "Spent Buy",
        "Spend Sell", "Qty", "Total Spend", "AvgPrice(GBP)", "Num Trades",
        "Trading Fees"};

    public FileHandler() {
        // Do nothing
    }

    public void openTransactionFile(String fileName) {
        
        try {
            // Open the file
            transactionFile = new FileReader(fileName);

            // Skip the first lines that we dont need
            getLine(transactionFile);
            getLine(transactionFile);
            getLine(transactionFile);
            getLine(transactionFile);
            getLine(transactionFile);
        } catch (FileNotFoundException ex) {
            System.out.println("Input file not found " + fileName + ", with error " + ex.getMessage());
            System.exit(1);
        }
    }

    public Iterable< CSVRecord> getTransactionRecords() {
        Iterable< CSVRecord> retVal = null;
        try {
            // Define the format of the file
            CSVFormat.Builder csvBuilder = CSVFormat.Builder.create(CSVFormat.EXCEL);
            csvBuilder.setHeader();
            CSVFormat csvFormat = csvBuilder.build();

            // Return a set of rows
            retVal = csvFormat.parse(transactionFile);

        } catch (IOException ex) {
            System.out.println("Trying to read transaction file and got IO Exception: " + ex.getMessage());
            System.exit(1);
        }
        return retVal;
    }

    public void closeTransactionfile() {
        try {
            transactionFile.close();
        } catch (IOException ex) {
            System.out.println("Trying to close transaction file and got IO Exception: " + ex.getMessage());
            System.exit(1);
        }
    }

    /**
     * A way to skip the first few lines of the file which have a different
     * format
     *
     * @param in
     * @return
     */
    private String getLine(Reader in) {
        StringBuilder sb = new StringBuilder();
        int newChar;
        try {
            while ((newChar = in.read()) != -1) {
                // Check for new line
                if (newChar == 10) {
                    // End of line
                    break;
                }
                sb.append((char) newChar);
            }

        } catch (IOException ex) {

            System.err.println("Failed to read line");
            System.exit(1);
        }

        return sb.toString();
    }

    public void closeClosingPosition() {
        try {

            if (closingPositions != null) {
                closingPositions.flush();
                closingPositions.close();
            }
        } catch (IOException ex) {
            System.err.println("Error message: " + ex.getMessage());
            System.err.println("Could not close positions file");
            System.exit(1);
        }
    }

    public void writeClosingPosition(String fileName, TradingPosition tp) {

        // If the file is not open, then create it, and add the header
        if (closingPositions == null) {
            try {

                // Define the format of the file
                CSVFormat.Builder csvBuilder = CSVFormat.Builder.create(CSVFormat.EXCEL);
                csvBuilder.setHeader(closingPositionsHeader);
                CSVFormat csvFormat = csvBuilder.build();

                // Create file along with header
                closingPositions = new CSVPrinter(new FileWriter(fileName), csvFormat);

            } catch (IOException ex) {
                System.err.println("Error message: " + ex.getMessage());
                System.err.println("Could not create closing positions file");
                System.exit(1);
            }
        }

        // Now write the row
        try {
            closingPositions.print(tp.getStockName());
            closingPositions.print(tp.getProfit());
            closingPositions.print(tp.getTotalBuySpend());
            closingPositions.print(tp.getTotalSellSpend());
            closingPositions.print(tp.getQtyOfStock());
            closingPositions.print(tp.getPricePaid());
            closingPositions.print(tp.getAvgCost());
            closingPositions.print(tp.getNumTrades());
            closingPositions.print(tp.getTradingFees());
            closingPositions.println();
        } catch (IOException ex) {
            System.err.println("Error message: " + ex.getMessage());
            System.err.println("Could not write line to positions file");
            System.exit(1);
        }
    }

    /**
     * Open a previously saved "Closing Positions" file to put stock into the
     * system.
     *
     * @param fileName
     */
    public void openPreviousClosingPositions(String fileName) {
        try {
            // Open the file
            previousClosingPosition = new FileReader(fileName);
        } catch (FileNotFoundException ex) {
            System.out.println("Input file not found " + fileName + ", with error " + ex.getMessage());
            System.exit(1);
        }
    }

    Iterable< CSVRecord> getPreviousClosingPositionRecords() {
        Iterable< CSVRecord> retVal = null;

        try {
            // Define the format of the file
            CSVFormat.Builder csvBuilder = CSVFormat.Builder.create(CSVFormat.EXCEL);
            csvBuilder.setHeader();
            CSVFormat csvFormat = csvBuilder.build();

            // Return a set of rows
            retVal = csvFormat.parse(previousClosingPosition);

        } catch (IOException ex) {
            System.out.println("Trying to read Previous Closing Positions file and got IO Exception: " + ex.getMessage());
            System.exit(1);
        }

        return retVal;
    }

    public void closePreviousClosingPositions() {
        try {
            previousClosingPosition.close();
        } catch (IOException ex) {
            System.out.println("Trying to close Previous Closing Positions file and got IO Exception: " + ex.getMessage());
            System.exit(1);
        }
    }

    public void writeHtmlOutput(String fileName, String content) {
        BufferedWriter outputReportFile;

        try {
            // Create the file

            //Specify the file name and path here
            File file = new File(fileName);

            // This logic will make sure that the file gets created if it is not present at the
            // specified location
            if (!file.exists()) {
                file.createNewFile();
            }
            outputReportFile = new BufferedWriter(new FileWriter(file));

            // Write the content
            outputReportFile.write(content);

            // Close the file
            outputReportFile.flush();
            outputReportFile.close();

        } catch (IOException ex) {
            System.err.println("Error message: " + ex.getMessage());
            System.err.println("Could not create HTML Report file " + fileName);
            System.exit(1);
        }
    }

    public String readFile(String file) {
        BufferedReader reader = null;
        StringBuilder stringBuilder = new StringBuilder();
        try {
            reader = new BufferedReader(new FileReader(file));
            String line = null;
            
            String ls = System.getProperty("line.separator");

            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
                stringBuilder.append(ls);
            }

            return stringBuilder.toString();
        } catch (IOException ex) {
            System.err.println("Unable to read file " + file + " with error " + ex.getMessage());
            System.exit(1);
        } finally {
            if( reader != null) {
                
                try {
                    reader.close();
                } catch (IOException ex) {
                    // Don't care
                }
            }
        }
        return stringBuilder.toString();
    }
}
