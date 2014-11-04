package org.tharrisx.util.report;

import org.tharrisx.util.text.StringPadder;
import org.tharrisx.util.text.StringUtils;

/**
 * A tabular formatting helper,
 * 
 * @author tharrisx
 * @since 1.0.0
 * @version 1.0.0
 */
public class SimpleReportGenerator implements ReportGenerator {

  @Override
  public String getReport(String[] headers, String[][] data) {
    StringBuilder retBuf = new StringBuilder();
    int[] columnWidths = getColumnWidths(headers, data);
    appendSeparator(retBuf, columnWidths);
    appendRow(retBuf, headers, columnWidths);
    appendSeparator(retBuf, columnWidths);
    for(int rowNum = 0; rowNum < data.length; rowNum++) {
      appendRow(retBuf, data[rowNum], columnWidths);
    }
    appendSeparator(retBuf, columnWidths);
    return retBuf.toString();
  }

  private int[] getColumnWidths(String[] headers, String[][] data) {
    int[] ret = new int[headers.length];
    for(int colNum = 0; colNum < headers.length; colNum++) {
      if(null != headers[colNum] && headers[colNum].length() > ret[colNum])
        ret[colNum] = headers[colNum].length();
    }
    for(int rowNum = 0; rowNum < data.length; rowNum++) {
      for(int colNum = 0; colNum < data[rowNum].length; colNum++) {
        if(null != data[rowNum][colNum] && data[rowNum][colNum].length() > ret[colNum])
          ret[colNum] = data[rowNum][colNum].length();
      }
    }
    return ret;
  }

  private void appendRow(StringBuilder retBuf, String[] row, int[] columnWidths) {
    retBuf.append(StringUtils.EOL_SYSTEM);
    retBuf.append("|");
    for(int colNum = 0; colNum < row.length; colNum++) {
      reportColumn(retBuf, null == row[colNum] ? "" : row[colNum], " ", columnWidths[colNum], "|");
    }
  }

  private void appendSeparator(StringBuilder retBuf, int[] columnWidths) {
    retBuf.append(StringUtils.EOL_SYSTEM);
    retBuf.append("+");
    for(int colNum = 0; colNum < columnWidths.length; colNum++) {
      reportColumn(retBuf, "", "-", columnWidths[colNum], "+");
    }
  }

  private void reportColumn(StringBuilder retBuf, String value, String padChar, int columnWidth, String columnTail) {
    retBuf.append(StringPadder.rightPad(value, padChar, columnWidth)).append(columnTail);
  }
}
