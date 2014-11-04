package org.tharrisx.util.report;

/**
 * Defines an API for String report generation.
 * 
 * @author tharrisx
 * @since 1.0.0
 * @version 1.0.0
 */
public interface ReportGenerator {

  String getReport(String[] headers, String[][] data);
}
