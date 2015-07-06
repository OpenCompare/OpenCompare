package org.opencompare.api.java.util;

import org.opencompare.api.java.io.IOCell;
import org.opencompare.api.java.io.IOMatrix;

import static java.lang.String.format;

/**
 * Created by smangin on 7/1/15.
 */
public class MatrixHeaderDetector {

    private IOMatrix matrix;
    private int height = 0;
    private int width = 0;
    private int currentLine = 0;
    private int currentColumn = 0;
    private int relativeLine = 1;
    private int relativeColumn = 1;
    private int headerOffset = 0;
    private boolean stop = false;

    public MatrixHeaderDetector(IOMatrix matrix) {
        this.matrix = matrix;
        height = matrix.getNumberOfRows();
        width = matrix.getNumberOfColumns();
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
       return get(line, column).equals(get(relativeLine, relativeColumn));
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
        System.out.println("isUniqueLine" + currentLine);
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
        System.out.println("isUniqueColumn" + currentColumn);
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
        }
        if (result) {
            currentLine = relativeLine - 1;
            System.out.println("Rowspan on column " + currentColumn + " For line " + currentLine + " and line " + relativeLine);
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
        }
        if (result) {
            if (currentColumn != 0) {
                nextLine();
                firstColumn();
            }
            System.out.println("Colspan on line " + currentLine + " For col " + currentColumn + " and col " + relativeColumn);
        }
        return result;
    }

    private boolean validPosition() {
        System.out.println("New loop " + relativeLine + " > " + relativeColumn);
        return relativeLine < height - 1 && relativeColumn < width - 1 && !stop;
    }

    private void previousLine() {
        currentLine--;
        relativeLine = currentLine + 1;
        System.out.println("Previous line" + currentLine);
    }

    private void nextLine() {
        currentLine++;
        relativeLine = currentLine + 1;
        System.out.println("Next line" + currentLine);
    }

    private void firstColumn() {
        currentColumn = 0;
        relativeColumn = 1;
        System.out.println("First column");
    }

    private void nextColumn() {
        currentColumn++;
        relativeColumn = currentColumn + 1;
        System.out.println("Next column" + currentColumn);
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
