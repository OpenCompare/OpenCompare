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

}
