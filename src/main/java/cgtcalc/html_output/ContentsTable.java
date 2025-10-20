/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cgtcalc.html_output;

/**
 *
 * @author mat
 */
public class ContentsTable {
    
    public ContentsTable() {
        
    }
    
    public String getTable() {
        StringBuilder sb = new StringBuilder();
        sb.append("<h1 id=\"toc\">Table of Contents</h1>\n");
        sb.append("<table class=\"table table-bordered table-sm table-dark\">\n");
        
        sb.append("<tr><td>");
        sb.append("<a href=\"#tax-summary\">Tax Summary</a>");
        sb.append("</td></tr>\n");
        
        sb.append("<tr><td>");
        sb.append("<a href=\"#opening-positions\">Opening Positions</a>");
        sb.append("</td></tr>\n");
        
        sb.append("<tr><td>");
        sb.append("<a href=\"#closing-positions\">Closing Positions</a>");
        sb.append("</td></tr>\n");
        
        sb.append("<tr><td>");
        sb.append("<a href=\"#transaction-detail\">Transactions Detail</a>");
        sb.append("</td></tr>\n");
        
        sb.append("<tr><td>");
        sb.append("<a href=\"#warnings\">Warnings and manual transactions</a>");
        sb.append("</td></tr>\n");
        
        sb.append("</table>\n");
        
        return sb.toString();
    }
}
