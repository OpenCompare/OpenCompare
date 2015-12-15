package org.opencompare.api.java.io;

import org.opencompare.api.java.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * Created by gbecan on 08/12/15.
 */
public class ImportMatrix extends IOMatrix<ImportCell> {


    public void removeDuplicatedLines() {
        LinkedHashSet<List<ImportCell>> lines = new LinkedHashSet<>();

        for (int i = 0; i < getNumberOfRows();i++) {
            List<ImportCell> line = new ArrayList<>();
            for (int j = 0; j < getNumberOfColumns(); j++) {
                line.add(cells.get(new Pair<>(i, j)));
            }
            lines.add(line);
        }

        cells = new HashMap<>();

        int i = 0;
        for (List<ImportCell> line : lines) {
            int j = 0;

            for (ImportCell cell : line) {
                cells.put(new Pair<>(i, j), cell);
                j++;
            }

            i++;
        }

        maxRow = lines.size() - 1;

    }

    public void removeEmptyLines() {
        int removedLines = 0;

        for (int row = 0; row < getNumberOfRows(); row++) {

            // Detect empty line
            boolean emptyline = true;
            for (int column = 0; column < getNumberOfColumns(); column++) {
                ImportCell cell = cells.get(new Pair<>(row, column));
                if (cell != null && !cell.content.matches("\\s*")) {
                    emptyline = false;
                    break;
                }
            }

            if (emptyline) {
                removedLines++;
            }

            // Remove empty lines and shift others if necessary
            if (removedLines > 0) {
                for (int column = 0; column < getNumberOfColumns(); column++) {
                    ImportCell cell = cells.get(new Pair<>(row, column));
                    if (cell != null && !emptyline) {
                        cells.put(new Pair<>(row - removedLines, column), cell);
                    }
                    cells.remove(new Pair<>(row, column));
                }
            }
        }

        maxRow = maxRow - removedLines;
    }

}
