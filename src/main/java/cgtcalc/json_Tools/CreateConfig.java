package cgtcalc.json_Tools;

import com.google.gson.Gson;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author mat
 */
public class CreateConfig {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        String workingDir = "/home/mat/Documents/HL/";

        Configuration[] configurations = {
            new Configuration(workingDir + "Trans20-21.csv",
                "", workingDir + "ClosingPositions20-21hmrc.csv",
                workingDir + "HtmlReport20-21hmrc.html", "", 1),
            new Configuration(workingDir + "Trans20-21-plus2.csv",
                "", workingDir + "ClosingPositions20-21compare.csv",
                workingDir + "HtmlReport20-21compare.html", "", 2),
            new Configuration(workingDir + "Trans21-22+fixup.csv",
                workingDir + "ClosingPositions20-21hmrc.csv", workingDir + "ClosingPositions21-22hmrc.csv",
                workingDir + "HtmlReport21-22hmrc.html", "", 2),
            new Configuration(workingDir + "Trans22-23.csv",
                workingDir + "ClosingPositions21-22hmrc.csv", workingDir + "ClosingPositions22-23hmrc.csv",
                workingDir + "HtmlReport22-23hmrc.html", "", 2),
            new Configuration(workingDir + "Trans23-24.csv",
                workingDir + "ClosingPositions22-23hmrc.csv", workingDir + "ClosingPositions23-24hmrc.csv",
                workingDir + "HtmlReport23-24hmrc.html", "", 2),
            new Configuration(workingDir + "Trans24-25.csv",
                workingDir + "ClosingPositions23-24hmrc.csv", workingDir + "ClosingPositions24-25hmrc.csv",
                workingDir + "HtmlReport24-25hmrc.html", "", 2),
        };

        Gson gson = new Gson();

        String[] fileNames = {
            workingDir + "Config20-21hmrc.json",
            workingDir + "Config20-21comparison.json",
            workingDir + "Config21-22hmrc.json",
            workingDir + "Config22-23hmrc.json",
            workingDir + "Config23-24hmrc.json",
            workingDir + "Config24-25hmrc.json",
        };
        
        if (fileNames.length != configurations.length){
            System.err.println("There needs to be as many file anmes as configurations");
            System.exit(1);
        }

        for (int i = 0; i < fileNames.length; i++) {
            File file = new File(fileNames[i]);

            try {
                BufferedWriter config = new BufferedWriter(new FileWriter(file));
                config.write(gson.toJson(configurations[i]));
                config.append('\n');
                config.flush();
                config.close();
                System.out.println("Created configuration file: "+fileNames[i]);
            } catch (IOException ex) {
                System.err.println("Failed to create " + fileNames[i] + " with error " + ex.getMessage());
                System.exit(1);
            }

        }
    }
}