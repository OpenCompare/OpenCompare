package org.opencompare.api.java.io;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by smangin on 02/07/15.
 */
public class Matrix {

    public String name = "";
    public Map<Map<Integer, Integer>, Cell> cells = new HashMap<>();

    public Matrix(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCell(Cell cell, int row, int column) {
        cells.put(new HashMap<Integer, Integer>(row, column), cell);
    }

    public Cell getCell(int row, int column) {
        return cells.get(new HashMap<Integer, Integer>(row, column));
    }

    public int getNumberOfRows() {
        if (!cells.isEmpty()) {
            return cells.keySet().size();
        }
        return 0;
    }

    public int getNumberOfColumns() {
        if (!cells.isEmpty()) {
            return cells.keySet().size();
        }
        return 0;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < getNumberOfRows(); i++){
            for (int j = 0; j < getNumberOfColumns(); j++) {
                result.append(i + "," + j + ":");

                Cell cell = cells.get(new HashMap<Integer, Integer>(i, j));
                if (cell != null) {
                    result.append(cell.getContent());
                } else {
                    result.append("/!\\ This cell is not publicined /!\\");
                }
                result.append('\n');
            }
        }
        return result.toString();
    }


    public boolean comparePosition(Cell c1, Cell c2) {
        return (c1.getRow() < c2.getRow()) || (c1.getRow() == c2.getRow() && c1.getColumn() < c2.getColumn());
    }

}
