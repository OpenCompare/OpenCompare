package org.opencompare.api.java.io;

import org.opencompare.api.java.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by smangin on 02/07/15.
 */
public class IOMatrix {

    private String name = "";
    private int maxRow = 0;
    private int maxColumn = 0;
    private Map<Pair<Integer, Integer>, IOCell> cells = new HashMap<>();

    public IOMatrix() {
    }

    public IOMatrix(List<String[]> lines) {
        for(int lineIndex = 0; lineIndex < lines.size(); lineIndex++){
            int lineLength = lines.get(lineIndex).length;
            for(int colIndex = 0; colIndex < lineLength; colIndex++){
                getOrCreateCell(lineIndex, colIndex).setContent(lines.get(lineIndex)[colIndex]);
            }
        }
    }

    public String getName() {
        return name;
    }

    public IOMatrix setName(String name) {
        this.name = name;
        return this;
    }

    public IOCell getCell(int row, int column) {
        return cells.get(new Pair<>(row, column));
    }

    public IOMatrix setCell(IOCell cell, int row, int column) {
        return setCell(cell, row, column, 1, 1);
    }

    public IOMatrix setCell(IOCell cell, int row, int column, int rowspan, int colspan) {
        maxRow = (maxRow < (row + rowspan - 1)) ? (row + rowspan - 1) : maxRow;
        maxColumn = (maxColumn < (column + colspan - 1)) ? (column + colspan - 1) : maxColumn;
        for (int i = 0; i < rowspan;i++) {
            for (int j = 0; j < colspan;j++) {
                cells.put(new Pair<>(row + i, column + j), cell);
            }
        }
        return this;
    }

    public IOCell getOrCreateCell(int row, int column) {
        if (cells.containsKey(new Pair<>(row, column))) {
            return cells.get(new Pair<>(row, column));
        } else {
            IOCell cell = new IOCell("");
            setCell(cell, row, column, 1, 1);
            return cell;
        }
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
        Map<Pair<Integer, Integer>, IOCell> transposedCells = new HashMap<>();

        for (Map.Entry<Pair<Integer, Integer>, IOCell> entry : cells.entrySet()) {
            transposedCells.put(new Pair<>(entry.getKey()._2, entry.getKey()._1), entry.getValue());
        }

        int tempMaxRow = this.maxRow;
        this.maxRow = maxColumn;
        this.maxColumn = tempMaxRow;

        this.cells = transposedCells;
    }

}
