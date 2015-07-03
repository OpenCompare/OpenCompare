package org.opencompare.api.java.io;

import org.opencompare.api.java.util.Pair;

import java.util.*;

/**
 * Created by smangin on 02/07/15.
 */
public class IOMatrix {

    private String name = "";
    private int maxRow = 0;
    private int maxColumn = 0;
    private Hashtable<Pair<Integer, Integer>, IOCell> cells = new Hashtable<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Pair<Integer, Integer> getPosition(IOCell IOCell) {
        for (Pair<Integer, Integer> pos: cells.keySet()) {
            if (cells.get(pos).equals(IOCell)) {
                return pos;
            }
        }
        return null;
    }

    public void setCell(IOCell IOCell, int row, int column) {
        if (maxRow < row) {
            maxRow = row;
        }
        if (maxColumn < column) {
            maxColumn = column;
        }
        cells.putIfAbsent(new Pair<>(row, column), IOCell);
    }

    public IOCell getCell(int row, int column) {
        return cells.getOrDefault(new Pair<>(row, column), new IOCell("", "", 0, 0));
    }

    public int getNumberOfRows() {
        return maxRow;
    }

    public int getNumberOfColumns() {
        return maxColumn;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < getNumberOfRows(); i++){
            for (int j = 0; j < getNumberOfColumns(); j++) {
                result.append(i + "," + j + ":");

                IOCell IOCell = getCell(i, j);
                if (IOCell != null) {
                    result.append(IOCell.getContent());
                } else {
                    result.append("/!\\ This cell is not publicined /!\\");
                }
                result.append('\n');
            }
        }
        return result.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof IOMatrix) {
            IOMatrix IOMatrix = (IOMatrix) obj;
            for (Pair<Integer, Integer> pos: cells.keySet()) {
                IOCell IOCell = IOMatrix.getCell(pos._1, pos._2);
                if (IOCell == null || !IOCell.equals(cells.get(pos))) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
}
