package org.opencompare.api.java.util;

import org.opencompare.api.java.io.IOCell;
import org.opencompare.api.java.io.IOMatrix;

import static java.lang.String.format;

/**
 * Created by smangin on 7/1/15.
 */
public class MatrixHeaderDetector {

    private IOMatrix matrix;
    private int height;
    private int width;
    private int currentLine;
    private int currentColumn;
    private int relativeLine;
    private int relativeColumn;
    private int headerOffset;
    private boolean stop;
    private boolean transposed;

    public MatrixHeaderDetector(IOMatrix matrix) {
        this.matrix = matrix;
        reset();
    }
    public void reset() {
        this.height = this.matrix.getNumberOfRows() - 1;
        this.width = this.matrix.getNumberOfColumns() - 1;
        this.currentLine = 0;
        this.currentColumn = 0;
        this.relativeLine = 1;
        this.relativeColumn = 1;
        this.headerOffset = 0;
        this.stop = false;
    }

    /*
    Method that allow transposition
     */
    public IOCell get(int i, int j) {
        return transposed ? matrix.getCell(j, i) : matrix.getCell(i, j);
    }
    private int getInternalHeight() {
        return transposed ? width : height;
    }
    private int getInternalWidth() {
        return transposed ? height : width;
    }
    private int getCurrentLine() {
        return transposed ? currentColumn : currentLine;
    }
    private int getCurrentColumn() {
        return transposed ? currentLine : currentColumn;
    }
    private int getRelativeLine() {
        return transposed ? relativeColumn : relativeLine;
    }
    private int getRelativeColumn() {
        return transposed ? relativeLine : relativeColumn;
    }
    private void setCurrentLine(int value) {
        if (transposed) {
            currentColumn = value;
        } else {
            currentLine = value;
        }
    }
    private void setCurrentColumn(int value) {
        if (transposed) {
            currentLine = value;
        } else {
            currentColumn = value;
        }
    }
    private void setRelativeLine(int value) {
        if (transposed) {
            relativeColumn = value;
        } else {
            relativeLine = value;
        }
    }
    private void setRelativeColumn(int value) {
        if (transposed) {
            relativeLine = value;
        } else {
            relativeColumn = value;
        }
    }

    /*
    Functional methods
     */
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
        for (int col = 0; col <= getInternalWidth(); col++) {
            if (isDifferent(getCurrentLine(), 0, getCurrentLine(), col)) {
                return false;
            }
        }
        // Save information in the matrix
        get(getCurrentLine(), 0).setColspan(getWidth());
        //System.out.println("\t" + "isUniqueLine(" + getCurrentLine() + ")");
        if (getCurrentLine() > 0) {
            previousLine();
            stop();
        } else if (getCurrentLine() == 0) {
            nextLine();
            headerOffset++;
        }
        return true;
    }

    private boolean isUniqueColumn() {
        for (int line = 0; line < getInternalHeight(); line++) {
            if (isDifferent(0, getCurrentColumn(), line, getCurrentColumn())) {
                return false;
            }
        }
        // Save information in the matrix
        get(0, getCurrentColumn()).setRowspan(getHeight());
        nextColumn();
        return true;
    }

    private boolean isRowspan() {
        boolean result = false;
        while (isEqual(getCurrentLine(), getCurrentColumn(), getRelativeLine(), getCurrentColumn())) {
            result = true;
            if (getRelativeLine() == getInternalHeight()) {
                break;
            }
            setRelativeLine(getRelativeLine() + 1);
            // Save information in the matrix
            get(getCurrentLine(), getCurrentColumn()).setRowspan(getRelativeLine() - getCurrentLine() + 1);
        }
        if (result) {
            setCurrentLine(getRelativeLine() - 1);
        }
        return result;
    }

    private boolean isColspan() {
        boolean result = false;
        while (isEqual(getCurrentLine(), getCurrentColumn(), getCurrentLine(), getRelativeColumn())) {
            result = true;
            if (getRelativeColumn() == getInternalWidth()) {
                break;
            }
            setRelativeColumn(getRelativeColumn() + 1);
            // Save information in the matrix
            get(getCurrentLine(), getCurrentColumn()).setColspan(getRelativeColumn() - getCurrentColumn() + 1);
        }
        if (result) {
            if (getCurrentColumn() != 0) {
                nextLine();
                firstColumn();
            }
        }
        return result;
    }

    /*
    Positionning methods
     */
    private void nextPosition() {
        setRelativeLine(getCurrentLine() + 1);
        setRelativeColumn(getCurrentColumn() + 1);
    }

    private boolean validPosition() {
        return getRelativeLine() < getInternalHeight() && getRelativeColumn() < getInternalWidth() && !stop;
    }

    private void previousLine() {
        setCurrentLine(getCurrentLine() - 1);
        setRelativeLine(getCurrentLine() + 1);
    }
    private void previousColumn() {
        setCurrentColumn(getCurrentColumn() - 1);
        setRelativeColumn(getCurrentColumn() + 1);
    }
    private void nextLine() {
        setCurrentLine(getCurrentLine() + 1);
        setRelativeLine(getCurrentLine() + 1);
    }
    private void nextColumn() {
        setCurrentColumn(getCurrentColumn() + 1);
        setRelativeColumn(getCurrentColumn() + 1);
    }
    private void firstColumn() {
        setCurrentColumn(0);
        setRelativeColumn(1);
    }

    /*
    Called when the algorithm must stop processing after the end of a loop
     */
    private void stop() {
        stop = true;
    }

    /*
    transpose the matrix process (fluid API)
     */
    public MatrixHeaderDetector setTransposition(boolean value) {
        this.transposed = value;
        reset();
        return this;
    }

    /*
    launches the algorithm (fluid API)
     */
    public MatrixHeaderDetector process() {
        while (validPosition()) {
            if (isUniqueLine()) continue;
            if (isUniqueColumn()) continue;
            if (isRowspan()) continue;
            if (isColspan()) continue;
            nextColumn();
            nextPosition();
        }
        return this;
    }

    /*
    Results
     */
    public Integer getHeaderOffset() {
        return headerOffset;
    }

    public Integer getHeaderHeight() {
        return getCurrentLine() + 1;
    }

    public int getWidth() {
        return getInternalWidth() + 1;
    }

    public int getHeight() {
        return getInternalHeight() + 1;
    }

    public IOMatrix getMatrix() {
        return matrix;
    }

}
