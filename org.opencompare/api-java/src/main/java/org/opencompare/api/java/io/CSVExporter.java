package org.opencompare.api.java.io;

import com.opencsv.CSVWriter;
import org.opencompare.api.java.*;
import org.opencompare.api.java.util.PCMVisitor;
import org.opencompare.api.java.value.*;

import java.io.IOException;
import java.io.StringWriter;
import java.util.*;

/**
 * Created by gbecan on 3/19/15.
 */
public class CSVExporter implements PCMExporter {

    private ExportMatrixExporter exportMatrixExporter = new ExportMatrixExporter();

    @Override
    public String export(PCMContainer container) {
        return export(container, ',', '"');

    }

    public String export(PCMContainer container, char separator, char quote) {

        PCM pcm = container.getPcm();
        PCMMetadata metadata = container.getMetadata();
        if (metadata == null) {
            metadata = new PCMMetadata(pcm);
        }

        StringWriter stringWriter = new StringWriter();
        CSVWriter csvWriter = new CSVWriter(stringWriter, separator, quote);

        ExportMatrix exportMatrix = exportMatrixExporter.export(container);
        exportMatrix.flattenCells();

        for (int row = 0; row < exportMatrix.getNumberOfRows(); row++) {
            String[] line = new String[exportMatrix.getNumberOfColumns()];

            for (int column = 0; column < exportMatrix.getNumberOfColumns(); column++) {
                ExportCell cell = exportMatrix.getCell(row, column);
                if (cell != null) {
                    line[column] = cell.getContent();
                } else {
                    line[column] = "";
                }
            }

            csvWriter.writeNext(line);
        }

        try {
            csvWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringWriter.toString();

    }

}
