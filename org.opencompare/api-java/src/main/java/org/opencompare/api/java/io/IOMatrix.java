package org.opencompare.api.java.io;

import com.opencsv.CSVReader;
import org.opencompare.api.java.PCM;
import org.opencompare.api.java.util.Pair;
import org.opencompare.api.java.util.PrettyPrinter;

import java.io.IOException;
import java.util.*;

/**
 * Created by smangin on 02/07/15.
 */
public class IOMatrix implements Cloneable, Observer {

    private String name = "";
    private int maxRow = 0;
    private int maxColumn = 0;
    private HashMap<Pair<Integer, Integer>, IOCell> cells = new HashMap<>();

    public String getName() {
        return name;
    }

    public IOMatrix setName(String name) {
        this.name = name;
        return this;
    }

    public IOMatrix setCell(IOCell cell, int row, int column) {
        int cellMaxRow = row + (cell.getRowspan() - 1);
        int cellMaxColumn = column + (cell.getColspan() - 1);
        if (maxRow < cellMaxRow) {
            maxRow = cellMaxRow;
        }
        if (maxColumn < cellMaxColumn) {
            maxColumn = cellMaxColumn;
        }
        cell.setRow(row);
        cell.setColumn(column);
        cells.put(new Pair<>(row, column), cell);
        cell.addObserver(this);
        return this;
    }

    public IOCell getCell(int row, int column) {
        return cells.get(new Pair<>(row, column));
    }

    public IOCell getOrCreateCell(int row, int column) {
        IOCell cell = new IOCell("", "");
        setCell(cell, row, column);
        return cells.getOrDefault(new Pair<>(row, column), cell);
    }

    public int getNumberOfRows() {
        return maxRow + 1;
    }

    public int getNumberOfColumns() {
        return maxColumn + 1;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < getNumberOfRows(); i++){
            for (int j = 0; j < getNumberOfColumns(); j++) {
                result.append(i + "," + j + ":");

                IOCell cell = getCell(i, j);
                if (cell != null) {
                    result.append(cell.getContent());
                } else {
                    result.append("");
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
        if (obj != null && obj instanceof IOMatrix) {
            IOMatrix matrix = (IOMatrix) obj;
            if (!name.equals(matrix.getName())) {
                return false;
            }
            for (Pair<Integer, Integer> pos : cells.keySet()) {
                IOCell cell1 = getCell(pos._1, pos._2);
                IOCell cell2 = matrix.getCell(pos._1, pos._2);
                if (!cell1.equals(cell2)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public String[][] toStringArray() {
        String[][] matrix = new String[getNumberOfRows()][getNumberOfColumns()];
        for (int i = 0; i < getNumberOfRows();i++) {
            for (int j = 0; j < getNumberOfColumns();j++) {
                matrix[i][j] = getCell(i, j).getContent();
            }
        }
        return matrix;
    }

    public IOMatrix clone() throws CloneNotSupportedException {
        IOMatrix matrix = (IOMatrix) super.clone();
        matrix.cells = (HashMap) matrix.cells.clone();
        for (IOCell cell : matrix.cells.values()) {
            matrix.setCell(cell.clone(), cell.getRow(), cell.getColumn());
        }
        return matrix;
    }

    @Override
    public void update(Observable observable, Object o) {
        if (o instanceof IOCell) {
            IOCell cell = (IOCell) o;
            if (cell.hasChanged()) {
                setCell(cell, cell.getRow(), cell.getColumn());
            }
        }
    }

}
