package org.opencompare.api.java.util;

import org.opencompare.api.java.io.IOMatrix;

import static java.lang.String.format;

/**
 * Created by smangin on 7/1/15.
 */
public class MatrixHeaderDetector {

    private IOMatrix IOMatrix;
    private int height = 0;
    private int width = 0;
    private int currentLine = 0;
    private int currentColumn = 0;
    private int relativeLine = 1;
    private int relativeColumn = 1;
    private int headerOffset = 0;

    public MatrixHeaderDetector(IOMatrix IOMatrix) {
        this.IOMatrix = IOMatrix;
        getMatrixSize();
        process();
    }

    private void getMatrixSize() {
        height = IOMatrix.getNumberOfRows();
        width = IOMatrix.getNumberOfColumns();
    }

    private String get(int i, int j) {
        return IOMatrix.getCell(i, j).getContent();
    }

    private void process() {
        while (validPosition()) {
            if (oneLineDetect()) continue;
            if (oneColDetect()) continue;
            if (rowspanDetect()) continue;
            if (colspanDetect()) continue;
            nextColumn();
            nextPosition();
        }
    }
    private boolean oneLineDetect() {
        int col = 1;
        while (col < width) {
            if (!get(currentLine, 0).equals(get(currentLine, col))) {
                return false;
            }
            col++;
        }
        //System.out.println("oneLineDetect" + currentLine);
        if (currentLine > 0) {
            previousLine();
        } else if (currentLine == 0) {
            nextLine();
            headerOffset++;
        }
        return true;
    }

    private boolean oneColDetect() {
        int line = 1;
        while (line < height) {
            if (!get(0, currentColumn).equals(get(line, currentColumn))) {
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
            if (relativeLine == height - 1) {
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
        return relativeLine < height - 1 && relativeColumn < width - 1;
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
        return currentLine + 1;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
