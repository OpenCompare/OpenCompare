package org.opencompare.api.java.io;

import com.opencsv.CSVReader;
import org.opencompare.api.java.*;
import org.opencompare.api.java.util.MatrixAnalyser;
import org.opencompare.api.java.util.MatrixComparatorEqualityImpl;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gbecan on 4/2/15.
 */
public class CSVLoader implements PCMLoader {

    private PCMFactory factory;
    private char separator;
    private char quote;
    private boolean productsAsLines;
    private Map<Integer, AbstractFeature> features;

    public CSVLoader(PCMFactory factory) {
        this(factory, ',', '"', true);
    }

    public CSVLoader(PCMFactory factory, char separator) {
        this(factory, separator, '"', true);
    }

    public CSVLoader(PCMFactory factory, char separator, char quote) {
        this(factory, separator, quote, true);
    }

    public CSVLoader(PCMFactory factory, boolean productsAsLines) {
        this(factory, ',', '"', productsAsLines);
    }

    public CSVLoader(PCMFactory factory, char separator, char quote, boolean productsAsLines) {
        this.factory = factory;
        this.separator = separator;
        this.quote = quote;
        this.productsAsLines = productsAsLines;
    }

    public static IOMatrix createMatrix(CSVReader reader) throws IOException {
        List<String[]> csvMatrix = reader.readAll();
        IOMatrix matrix = new IOMatrix();
        for (int i = 0; i < csvMatrix.size();i++) {
            for (int j = 0; j < csvMatrix.get(i).length;j++) {
                String content = csvMatrix.get(i)[j];
                IOCell cell = new IOCell(content);
                matrix.setCell(cell, i, j, 1, 1);
            }
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

    public List<PCMContainer> load(IOMatrix matrix) {
        return new IOMatrixLoader(this.factory, this.productsAsLines).load(matrix);
    }

    public List<PCMContainer> load(Reader reader) throws IOException {
        CSVReader csvReader = new CSVReader(reader, separator, quote);
        IOMatrix matrix = createMatrix(csvReader);
        return load(matrix);
    }
}

