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
        return setCell(cell, row, column, 1, 1);
    }

    public IOMatrix<T> setCell(T cell, int row, int column, int rowspan, int colspan) {
        maxRow = (maxRow < (row + rowspan - 1)) ? (row + rowspan - 1) : maxRow;
        maxColumn = (maxColumn < (column + colspan - 1)) ? (column + colspan - 1) : maxColumn;
        for (int i = 0; i < rowspan;i++) {
            for (int j = 0; j < colspan;j++) {
                cells.put(new Pair<>(row + i, column + j), cell);
            }
        }
        return this;
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

    public void transpose() {
        Map<Pair<Integer, Integer>, T> transposedCells = new HashMap<>();

        for (Map.Entry<Pair<Integer, Integer>, T> entry : cells.entrySet()) {
            transposedCells.put(new Pair<>(entry.getKey()._2, entry.getKey()._1), entry.getValue());
        }

        int tempMaxRow = this.maxRow;
        this.maxRow = maxColumn;
        this.maxColumn = tempMaxRow;

        this.cells = transposedCells;
    }

}
