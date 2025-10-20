/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cgtcalc;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 *
 * @author mat
 */
public class Tools {

    // Prevent instantiation - utility class only
    private Tools() {
    }

    /**
     * Check is something is a number or not. Null, or empty string are
     * considered not a number.
     *
     * @param s
     * @return    git add pom.xml src/main/java/cgtcalc/Tools.java README.md CHANGELOG.md PR_DESCRIPTION.md .github/workflows .gitignore PUBLISH-GITHUB.md
     */
    public static boolean isNumber(String s) {
        if (s == null) {
            return false;
        }
        if (s.isEmpty()) {
            return false;
        }

        boolean isNumber;
        try {
            Float.parseFloat(s);
            isNumber = true;
        } catch (NumberFormatException e) {
            isNumber = false;
        }
        return isNumber;
    }

    /**
     * Check is something is a number digit. This checks for just 0 through 9,
     * decimal points, plus signs and other things that make up numbers don't
     * count as digits.
     *
     * @param s
     * @return
     */
    public static boolean isDigit(char c) {
        return (c >= '0' && c <= '9');
    }

    // Remove all commas from a String, usually because they are numbers
    public static String removeCommas(String s) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) != ',') {
                sb.append(s.charAt(i));
            }
        }
        return sb.toString();
    }

    public static float parseFloat(String s) {
        String woCommas = Tools.removeCommas(s);
        float retVal = 0;

        try {
            retVal = Float.parseFloat(woCommas);
        } catch (Exception e) {
            // Do nothing as already 0
        }

        return retVal;
    }

    public static float round(float d, int decimalPlace) {
        return BigDecimal.valueOf(d).setScale(decimalPlace, RoundingMode.HALF_UP).floatValue();
    }
    
    /**
     * Swaps the first space in a string for a minus sign.
     * @param s
     * @return 
     */
    public static String space2minus(String s) {
        int space = s.indexOf(' ');
        return s.substring(0, space) + "-" + s.substring(space+1, s.length());
        
    }
}
