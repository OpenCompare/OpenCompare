package org.opencompare.api.java.util;

import org.opencompare.api.java.FeatureGroup;
import org.opencompare.api.java.io.IOCell;
import org.opencompare.api.java.io.IOMatrix;
import org.opencompare.api.java.io.IONode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Created by smangin on 7/1/15.
 */
public class MatrixAnalyser {

    private IOMatrix matrix;
    private MatrixComparator comparator;
    private int height;
    private int width;
    private int currentLine;
    private int currentColumn;
    private int relativeLine;
    private int relativeColumn;
    private int headerOffset;
    private boolean stop;
    private boolean transposed;

    public MatrixAnalyser(IOMatrix matrix, MatrixComparator comparator) {
        this.matrix = matrix;
        this.comparator = comparator;
        reset();
    }
    public MatrixAnalyser reset() {
        this.height = this.matrix.getNumberOfRows() - 1;
        this.width = this.matrix.getNumberOfColumns() - 1;
        this.currentLine = 0;
        this.currentColumn = 0;
        this.relativeLine = 1;
        this.relativeColumn = 1;
        this.headerOffset = 0;
        this.stop = false;
        this.transposed = false;
        return this;
    }

    /*
    Method that allow transposition
     */
    public MatrixAnalyser set(IOCell cell, int i, int j, int rowspan, int colspan) {
        if (transposed) {
            matrix.setCell(cell, j, i, colspan, rowspan);
        } else {
            matrix.setCell(cell, i, j, rowspan, colspan);
        }
        return this;
    }
    public IOCell get(int i, int j) {
        return transposed ? matrix.getOrCreateCell(j, i) : matrix.getOrCreateCell(i, j);
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
        IOCell cell1 = get(line, column);
        IOCell cell2 = get(relativeLine, relativeColumn);
        return comparator.compareCells(cell1, cell2);
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
        set(
                get(getCurrentLine(), 0),
                getCurrentLine(),
                getCurrentColumn(),
                1,
                getWidth()
        );
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
        set(
                get(0, getCurrentColumn()),
                getCurrentLine(),
                getCurrentColumn(),
                getHeight(),
                1
        );
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
        }
        if (result) {
            // Save information in the matrix
            set(
                    get(getCurrentLine(), getCurrentColumn()),
                    getCurrentLine(),
                    getCurrentColumn(),
                    getRelativeLine() - getCurrentLine(),
                    1
            );
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
        }
        if (result) {
            // Save information in the matrix
            set(
                    get(getCurrentLine(), getCurrentColumn()),
                    getCurrentLine(),
                    getCurrentColumn(),
                    1,
                    getRelativeColumn() - getCurrentColumn()
            );
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
    public MatrixAnalyser setTransposition(boolean value) {
        reset();
        this.transposed = value;
        return this;
    }

    /*
    launches the algorithm (fluid API)
     */
    public MatrixAnalyser process() {
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
    public int getHeaderOffset() {
        process();
        return headerOffset;
    }

    public int getHeaderColumnOffset() {
        //TODO check for same named product detection as rowspan (invalid case)
        //setTransposition(!this.transposed).process();
        //int result = getCurrentLine() + 1;
        //setTransposition(!this.transposed).process();
        //return result;
        return 1;
    }

    public int getHeaderHeight() {
        process();
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

    public IONode getHeaderNode() {
        IONode<String> root = new IONode<>("root");
//        for (int j = getHeaderColumnOffset(); j < getWidth(); j++) {
//            String name = get(getHeaderOffset(), j).getContent();
//            IONode parentNode = new IONode(name, getHeaderHeight() > 1, j);
//            for (IONode node: root.iterable()) {
//                if (node.getName().equals(name)) {
//                    parentNode = node;
//                }
//            }
//            if (!root.iterable().contains(parentNode)) {
//                root.add(parentNode);
//            }
//
//            if (getHeaderHeight() > 1) {
//                IONode subparentNode = null;
//                for (int i = getHeaderOffset() + 1; i < getHeaderHeight() - 1; i++) {
//                    String subname = get(i, j).getContent();
//                    subparentNode = new IONode(name, true, j);
//                    for (IONode subnode: root.iterable()) {
//                        if (subnode.getName().equals(subname)) {
//                            subparentNode = subnode;
//                        }
//                    }
//                    if (parentNode.isNodeAncestor(subparentNode) || parentNode.isNodeDescendant(subparentNode)) {
//                        continue;
//                    }
//                    parentNode.add(subparentNode);
//                }
//                if (subparentNode != null) {
//                    parentNode = subparentNode;
//                }
//                IONode node = new IONode(get(getHeaderHeight() - 1, j).getContent(), false, j);
//                parentNode.add(node);
//            }
//        }
        return root;
    }
}
