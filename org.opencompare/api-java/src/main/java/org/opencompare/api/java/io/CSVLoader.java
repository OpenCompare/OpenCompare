package org.opencompare.api.java.io;

import com.opencsv.CSVReader;
import org.opencompare.api.java.AbstractFeature;
import org.opencompare.api.java.PCMContainer;
import org.opencompare.api.java.PCMFactory;
import org.opencompare.api.java.interpreter.CellContentInterpreter;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by gbecan on 4/2/15.
 */
public class CSVLoader implements PCMLoader {

    private PCMFactory factory;
    private char separator;
    private char quote;
    private ImportMatrixLoader importMatrixLoader;

    public CSVLoader(PCMFactory factory, CellContentInterpreter cellContentInterpreter) {
        this(factory, cellContentInterpreter, ',');
    }

    public CSVLoader(PCMFactory factory, CellContentInterpreter cellContentInterpreter, char separator) {
        this(factory, cellContentInterpreter, separator, '"');
    }

    public CSVLoader(PCMFactory factory, CellContentInterpreter cellContentInterpreter, char separator, char quote) {
        this(factory, cellContentInterpreter, separator, quote, PCMDirection.UNKNOWN);
    }

    public CSVLoader(PCMFactory factory, CellContentInterpreter cellContentInterpreter, PCMDirection pcmDirection) {
        this(factory, cellContentInterpreter, ',', '"', pcmDirection);
    }

    public CSVLoader(PCMFactory factory, CellContentInterpreter cellContentInterpreter, char separator, char quote, PCMDirection pcmDirection) {
        this.factory = factory;
        this.separator = separator;
        this.quote = quote;

        importMatrixLoader = new ImportMatrixLoader(this.factory, cellContentInterpreter, pcmDirection);
    }

    private ImportMatrix createMatrix(CSVReader reader) throws IOException {
        ImportMatrix matrix = new ImportMatrix();

        int row = 0;

        String[] line = reader.readNext();
        while (line != null) {
            for (int column = 0; column < line.length; column++) {
                matrix.setCell(new ImportCell(line[column]), row, column);
            }
            row++;
            line = reader.readNext();
        }

        return matrix;
    }

    @Override
    public List<PCMContainer> load(String pcm) {
        List<PCMContainer> containers = new ArrayList<>();
        try {
            containers = load(new StringReader(pcm));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return containers;
    }

    @Override
    public List<PCMContainer> load(File file) throws IOException {
        List<PCMContainer> containers = new ArrayList<>();
        try {
            containers = load(new FileReader(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return containers;
    }

    public List<PCMContainer> load(ImportMatrix matrix) {
        List<PCMContainer> result = new ArrayList<>();
        result.add(importMatrixLoader.load(matrix));
        return result;
    }

    public List<PCMContainer> load(Reader reader) throws IOException {
        CSVReader csvReader = new CSVReader(reader, separator, quote);
        ImportMatrix matrix = createMatrix(csvReader);
        return load(matrix);
    }
}

