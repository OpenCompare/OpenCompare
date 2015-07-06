package org.opencompare.api.java.io;

import com.opencsv.CSVReader;
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
    private Hashtable<Pair<Integer, Integer>, IOCell> cells = new Hashtable<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCell(IOCell cell, int row, int column) {
        int cellMaxRow = row + (cell.getRowspan() - 1);
        int cellMaxColumn = column + (cell.getColspan() - 1);
        if (maxRow < cellMaxRow) {
            maxRow = cellMaxRow + 1;
        }
        if (maxColumn < cellMaxColumn) {
            maxColumn = cellMaxColumn + 1;
        }
        cell.setRow(row);
        cell.setColumn(column);
        cells.put(new Pair<>(row, column), cell);
        cell.addObserver(this);
    }

    public IOCell getCell(int row, int column) {
        if (row >= maxRow | column >= maxColumn) {
            System.out.println(getName());
            System.out.println("asked size");
            System.out.println(row);
            System.out.println(column);
            System.out.println("current size");
            System.out.println(maxRow);
            System.out.println(maxColumn);
            throw new ArrayIndexOutOfBoundsException();
        }
        IOCell cell = cells.get(new Pair<>(row, column));
        if (cell == null) { // dynamic normalisation
            cell = new IOCell("", "");
            setCell(cell, row, column);
        }
        return cell;
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

    public boolean isEqual(IOMatrix matrix) {
        System.out.println(matrix.getNumberOfRows());
        System.out.println(matrix.getNumberOfColumns());
        System.out.println(getNumberOfRows());
        System.out.println(getNumberOfColumns());
        if (matrix == this) {
            return true;
        }
        if (matrix != null) {
            if (!name.equals(matrix.getName())) {
                return false;
            }
            for (Pair<Integer, Integer> pos : cells.keySet()) {
                IOCell cell1 = getCell(pos._1, pos._2);
                IOCell cell2 = matrix.getCell(pos._1, pos._2);
                if (!cell1.isEqual(cell2)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public IOMatrix loadFromCsv(CSVReader reader) throws IOException {
        List<String[]> matrix = reader.readAll();
        for (int i = 0; i < matrix.size();i++) {
            for (int j = 0; j < matrix.get(i).length;j++) {
                String content = matrix.get(i)[j];
                IOCell cell = new IOCell(content, content);
                setCell(cell, i, j);
            }
        }
        return this;
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
        for (int i = 0; i < getNumberOfRows();i++) {
            for (int j = 0; j < getNumberOfColumns(); j++) {
                IOCell cell = getCell(i, j);
                matrix.setCell(cell.clone(), i, j);
            }
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
