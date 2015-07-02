package org.opencompare.api.java.util;

import com.sun.xml.internal.fastinfoset.util.StringArray;

import java.io.PrintStream;
import java.util.List;

import static java.lang.String.format;

/**
 * Created by smangin on 7/1/15.
 */
public class MatrixHeaderDetector {

    private List<String[]> matrix;
    private int height = 0;
    private int width = 0;
    private int currentLine = 0;
    private int currentColumn = 0;
    private int relativeLine = 1;
    private int relativeColumn = 1;
    private int headerOffset = 0;

    private class PrettyPrinter {

        private static final char BORDER_KNOT = '+';
        private static final char HORIZONTAL_BORDER = '-';
        private static final char VERTICAL_BORDER = '|';

        private static final String DEFAULT_AS_NULL = "(NULL)";

        private final PrintStream out;
        private final String asNull;

        public PrettyPrinter(PrintStream out) {
            this(out, DEFAULT_AS_NULL);
        }

        public PrettyPrinter(PrintStream out, String asNull) {
            if ( out == null ) {
                throw new IllegalArgumentException("No print stream provided");
            }
            if ( asNull == null ) {
                throw new IllegalArgumentException("No NULL-value placeholder provided");
            }
            this.out = out;
            this.asNull = asNull;
        }

        public void print(List<String[]> table) {
            if ( table == null ) {
                throw new IllegalArgumentException("No tabular data provided");
            }
            if ( table.size() == 0 ) {
                return;
            }
            final int[] widths = new int[getMaxColumns(table)];
            adjustColumnWidths(table, widths);
            printPreparedTable(table, widths, getHorizontalBorder(widths));
        }

        private void printPreparedTable(List<String[]> table, int widths[], String horizontalBorder) {
            final int lineLength = horizontalBorder.length();
            out.println(horizontalBorder);
            for ( final String[] row : table ) {
                if ( row != null ) {
                    out.println(getRow(row, widths, lineLength));
                    out.println(horizontalBorder);
                }
            }
        }

        private String getRow(String[] row, int[] widths, int lineLength) {
            final StringBuilder builder = new StringBuilder(lineLength).append(VERTICAL_BORDER);
            final int maxWidths = widths.length;
            for ( int i = 0; i < maxWidths; i++ ) {
                builder.append(padRight(getCellValue(safeGet(row, i, null)), widths[i])).append(VERTICAL_BORDER);
            }
            return builder.toString();
        }

        private String getHorizontalBorder(int[] widths) {
            final StringBuilder builder = new StringBuilder(256);
            builder.append(BORDER_KNOT);
            for ( final int w : widths ) {
                for ( int i = 0; i < w; i++ ) {
                    builder.append(HORIZONTAL_BORDER);
                }
                builder.append(BORDER_KNOT);
            }
            return builder.toString();
        }

        private int getMaxColumns(List<String[]> rows) {
            int max = 0;
            for ( final String[] row : rows ) {
                if ( row != null && row.length > max ) {
                    max = row.length;
                }
            }
            return max;
        }

        private void adjustColumnWidths(List<String[]> rows, int[] widths) {
            for ( final String[] row : rows ) {
                if ( row != null ) {
                    for ( int c = 0; c < widths.length; c++ ) {
                        final String cv = getCellValue(safeGet(row, c, asNull));
                        final int l = cv.length();
                        if ( widths[c] < l ) {
                            widths[c] = l;
                        }
                    }
                }
            }
        }

        private String padRight(String s, int n) {
            return format("%1$-" + n + "s", s);
        }

        private String safeGet(String[] array, int index, String defaultValue) {
            return index < array.length ? array[index] : defaultValue;
        }

        private String getCellValue(Object value) {
            return value == null ? asNull : value.toString();
        }

    }
    public MatrixHeaderDetector(List<String[]> matrix) {
        this.matrix = matrix;
        getMatrixSize();
    }

    private void getMatrixSize() {
        height = matrix.size();
        width = matrix.get(0).length;
    }

    private String get(int i, int j){
        return matrix.get(i)[j];
    }

    public int getWidth() {
        return width;
    }
    public int getHeight() {
        return height;
    }

    private boolean oneLineDetect() {
        int col = 1;
        while (col < width) {
            if (! get(currentLine, 0).equals(get(currentLine, col))) {
                return false;
            }
            col++;
        }
        //System.out.println("oneLineDetect" + currentLine);
        if (currentLine > 0) {
            previousLine();
        }
        else if (currentLine == 0) {
            nextLine();
            headerOffset++;
        }
        return true;
    }

    private boolean oneColDetect() {
        int line = 1;
        while (line < height) {
            if (! get(0, currentColumn).equals(get(line, currentColumn))) {
                return false;
            }
            line++;
        }
        //System.out.println("oneColDetect" + currentColumn);
        nextColumn();
        return true;
    }

    private boolean rowspanDetect() {
        boolean result = false;
        while (get(currentLine, currentColumn).equals(get(relativeLine, currentColumn))) {
            result = true;
            if (relativeLine == height -1) {
                break;
            }
            relativeLine++;
        }
        if (result) {
            currentLine = relativeLine - 1;
            //System.out.println("Rowspan on column " + currentColumn + " For line " + currentLine + " and line " + relativeLine);
        }
        return result;
    }

    private boolean colspanDetect() {
        boolean result = false;
        while (get(currentLine, currentColumn).equals(get(currentLine, relativeColumn))) {
            result = true;
            if (relativeColumn == width - 1) {
                break;
            }
            relativeColumn++;
        }
        if (result) {
            if (currentColumn != 0) {
                nextLine();
                firstColumn();
            }
            //System.out.println("Colspan on line " + currentLine + " For col " + currentColumn + " and col " + relativeColumn);
        }
        return result;
    }

    private boolean validPosition() {
        //System.out.println("New loop " + relativeLine + " > " + relativeColumn);
        return relativeLine < height -1 && relativeColumn < width - 1;
    }

    private void previousLine() {
        currentLine--;
        relativeLine = currentLine + 1;
        //System.out.println("Previous line" + currentLine);
    }

    private void nextLine() {
        currentLine++;
        relativeLine = currentLine + 1;
        //System.out.println("Next line" + currentLine);
    }

    private void firstColumn() {
        currentColumn = 0;
        relativeColumn = 1;
        //System.out.println("First column");
    }

    private void nextColumn() {
        currentColumn++;
        relativeColumn = currentColumn + 1;
        //System.out.println("Next column" + currentColumn);
    }

    private void nextPosition() {
        relativeLine = currentLine + 1;
        relativeColumn = currentColumn + 1;
    }

    public Integer getHeaderOffset() {
        return headerOffset;
    }

    public Integer getHeaderHeight() {
        return getFeaturesSubMatrix().size();
    }

    public List<String[]> getFeaturesSubMatrix(){

        while (validPosition()) {
            if (oneLineDetect()) continue;
            if (oneColDetect()) continue;
            if (rowspanDetect()) continue;
            if (colspanDetect()) continue;
            nextColumn();
            nextPosition();
        }
        return matrix.subList(headerOffset, currentLine + 1);
    }

    public void print() {
        PrettyPrinter printer = new PrettyPrinter(System.out);
        printer.print(matrix);
    }
    public void printHeader() {
        PrettyPrinter printer = new PrettyPrinter(System.out);
        printer.print(getFeaturesSubMatrix());
    }
}
