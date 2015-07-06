package org.opencompare.api.java.util;

import org.opencompare.api.java.io.IOCell;
import org.opencompare.api.java.io.IOMatrix;

import static java.lang.String.format;

/**
 * Created by smangin on 7/1/15.
 */
public class MatrixHeaderDetector {

    public IOMatrix getMatrix() {
        return matrix;
    }

    private IOMatrix matrix;
    private int height = 0;
    private int width = 0;
    private int currentLine = 0;
    private int currentColumn = 0;
    private int relativeLine = 1;
    private int relativeColumn = 1;
    private int headerOffset = 0;
    private boolean stop = false;

    public MatrixHeaderDetector(IOMatrix matrix) throws CloneNotSupportedException{
        this.matrix = matrix.clone();
        height = this.matrix.getNumberOfRows();
        width = this.matrix.getNumberOfColumns();
        while (validPosition()) {
            if (isUniqueLine()) continue;
            if (isUniqueColumn()) continue;
            if (isRowspan()) continue;
            if (isColspan()) continue;
            nextColumn();
            nextPosition();
        }
    }

    private IOCell get(int i, int j) {
        return matrix.getCell(i, j);
    }

    private boolean isEqual(int line, int column, int relativeLine, int relativeColumn) {
        String content1 = get(line, column).getContent();
        String content2 = get(relativeLine, relativeColumn).getContent();
        if (content1 == null && content2 == null) {
            return true;
        }
        if (content1.equals(content2)) {
            return true;
        }
        return false;
    }

    private boolean isDifferent(int line, int column, int relativeLine, int relativeColumn) {
        return !isEqual(line, column, relativeLine, relativeColumn);
    }

    private boolean isUniqueLine() {
        for (int col = 0; col < width; col++) {
            if (isDifferent(currentLine, 0, currentLine, col)) {
                return false;
            }
        }
        // Save information in the matrix
        matrix.getCell(currentLine, 0).setColspan(width-1);
        //System.out.println("\t" + "isUniqueLine(" + currentLine + ")");
        if (currentLine > 0) {
            previousLine();
            stop();
        } else if (currentLine == 0) {
            nextLine();
            headerOffset++;
        }
        return true;
    }

    private boolean isUniqueColumn() {
        for (int line = 0; line < height; line++) {
            if (isDifferent(0, currentColumn, line, currentColumn)) {
                return false;
            }
        }
        // Save information in the matrix
        matrix.getCell(0, currentColumn).setRowspan(height + 1);
        //System.out.println("\t" + "isUniqueColumn(" + currentColumn + ")");
        nextColumn();
        return true;
    }

    private boolean isRowspan() {
        boolean result = false;
        while (isEqual(currentLine, currentColumn, relativeLine, currentColumn)) {
            result = true;
            if (relativeLine == height - 1) {
                break;
            }
            relativeLine++;
            // Save information in the matrix
            matrix.getCell(currentLine, currentColumn).setRowspan(relativeLine - currentLine + 1);
        }
        if (result) {
            currentLine = relativeLine - 1;
            //System.out.println("\t" + "Rowspan");
        }
        return result;
    }

    private boolean isColspan() {
        boolean result = false;
        while (isEqual(currentLine, currentColumn, currentLine, relativeColumn)) {
            result = true;
            if (relativeColumn == width - 1) {
                break;
            }
            relativeColumn++;
            // Save information in the matrix
            matrix.getCell(currentLine, currentColumn).setColspan(relativeColumn - currentColumn + 1);
        }
        if (result) {
            if (currentColumn != 0) {
                nextLine();
                firstColumn();
            }
            //System.out.println("\t" + "Colspan");
        }
        return result;
    }

    private boolean validPosition() {
        //System.out.println("Loop [" + currentLine + "][" + currentColumn + "]([" + relativeLine + "][" + relativeColumn + "])" + " stop->" + stop);
        return relativeLine < height - 1 && relativeColumn < width - 1 && !stop;
    }

    private void previousLine() {
        currentLine--;
        relativeLine = currentLine + 1;
        //System.out.println("\t" + "Previous line");
    }

    private void nextLine() {
        currentLine++;
        relativeLine = currentLine + 1;
        //System.out.println("\t" + "Next line");
    }

    private void firstColumn() {
        currentColumn = 0;
        relativeColumn = 1;
        //System.out.println("\t" + "First column");
    }

    private void nextColumn() {
        currentColumn++;
        relativeColumn = currentColumn + 1;

        //System.out.println("\t" + "Next column");
    }

    private void nextPosition() {
        relativeLine = currentLine + 1;
        relativeColumn = currentColumn + 1;
    }

    private void stop() {
        stop = true;
    }

    public Integer getHeaderOffset() {
        return headerOffset;
    }

    public Integer getHeaderHeight() {
        return currentLine + 1;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
