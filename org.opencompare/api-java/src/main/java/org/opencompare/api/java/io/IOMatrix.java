package org.opencompare.api.java.io;

import org.opencompare.api.java.util.Pair;

import java.util.*;

/**
 * Created by smangin on 02/07/15.
 */
public class IOMatrix<T extends IOCell> {

    protected String name = "";
    protected int maxRow = 0;
    protected int maxColumn = 0;
    protected Map<Pair<Integer, Integer>, T> cells = new HashMap<>();

    public IOMatrix() {

    }

    public String getName() {
        return name;
    }

    public IOMatrix setName(String name) {
        this.name = name;
        return this;
    }

    public T getCell(int row, int column) {
        return cells.get(new Pair<>(row, column));
    }

    public IOMatrix<T> setCell(T cell, int row, int column) {
        cells.put(new Pair<>(row, column), cell);
        maxRow = (maxRow < (row + cell.getRowspan() - 1)) ? (row + cell.getRowspan() - 1) : maxRow;
        maxColumn = (maxColumn < (column + cell.getColspan() - 1)) ? (column + cell.getColspan() - 1) : maxColumn;
        return this;
    }

    public int getNumberOfRows() {
        return maxRow + 1;
    }

    public int getNumberOfColumns() {
        return maxColumn + 1;
    }


    public boolean isPositionOccupied(int row, int column) {

        if (getCell(row, column) != null) {
            return true;
        }

        // Check previous cells with rowspan
        for (int i = row; i >= 0; i--) {
            IOCell cell = getCell(i, column);
            if (cell != null && (i + cell.getRowspan() > row)) {
                return true;
            } else if (cell != null) {
                break;
            }
        }

        // Check previous cells with colspan
        for (int j = column; j >= 0; j--) {
            IOCell cell = getCell(row, j);
            if (cell != null && (j + cell.getColspan() > column)) {
                return true;
            } else if (cell != null) {
                break;
            }
        }

        return false;
    }

    public void transpose() {
        Map<Pair<Integer, Integer>, T> transposedCells = new HashMap<>();

        for (Map.Entry<Pair<Integer, Integer>, T> entry : cells.entrySet()) {
            T cell = entry.getValue();
            int tempRowspan = cell.rowspan;
            cell.rowspan = cell.colspan;
            cell.colspan = tempRowspan;
            transposedCells.put(new Pair<>(entry.getKey()._2, entry.getKey()._1), entry.getValue());
        }

        int tempMaxRow = this.maxRow;
        this.maxRow = maxColumn;
        this.maxColumn = tempMaxRow;

        this.cells = transposedCells;
    }

    public void flattenCells() {
        for (int row = 0; row < getNumberOfRows(); row++) {
            for (int column = 0; column < getNumberOfColumns(); column++) {
                T cell = cells.get(new Pair<>(row, column));

                if (cell != null) {
                    // Copy cell
                    for (int rowOffset = 0; rowOffset < cell.getRowspan(); rowOffset++) {
                        for (int columnOffset = 0; columnOffset  < cell.getColspan(); columnOffset ++) {
                            cells.put(new Pair<>(row + rowOffset, column + columnOffset), cell);
                        }
                    }

                    // Reset span
                    cell.setRowspan(1);
                    cell.setColspan(1);
                }

            }
        }
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < getNumberOfRows(); i++){
            for (int j = 0; j < getNumberOfColumns(); j++) {
                result.append(i + "," + j + ":");

                T cell = getCell(i, j);
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
            try {
                IOMatrix<T> matrix = (IOMatrix<T>) obj;

                if (!name.equals(matrix.getName())) {
                    return false;
                }
                for (Pair<Integer, Integer> pos : cells.keySet()) {
                    T cell1 = getCell(pos._1, pos._2);
                    T cell2 = matrix.getCell(pos._1, pos._2);
                    if (!cell1.equals(cell2)) {
                        return false;
                    }
                }
                return true;
            } catch (ClassCastException e) {
                return false;
            }
        }
        return false;
    }

    public List<String[]> toList() {
        List<String[]> matrix = new ArrayList<>(getNumberOfRows());
        for (int i = 0; i < getNumberOfRows();i++) {
            String[] line = new String[getNumberOfColumns()];
            for (int j = 0; j < getNumberOfColumns();j++) {
                 line[j] = getCell(i, j).getContent();
            }
            matrix.add(line);
        }
        return matrix;
    }

}
