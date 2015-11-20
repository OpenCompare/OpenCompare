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
    private IOMatrixLoader ioMatrixLoader;

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

        if (this.productsAsLines) {
            ioMatrixLoader = new IOMatrixLoader(this.factory, PCMDirection.PRODUCTS_AS_LINES);
        } else {
            ioMatrixLoader = new IOMatrixLoader(this.factory, PCMDirection.PRODUCTS_AS_COLUMNS);
        }

    }

    private IOMatrix createMatrix(CSVReader reader) throws IOException {
        IOMatrix matrix = new IOMatrix();

        int row = 0;

        String[] line = reader.readNext();
        while (line != null) {
            for (int column = 0; column < line.length; column++) {
                matrix.setCell(new IOCell(line[column]), row, column);
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

    public List<PCMContainer> load(IOMatrix matrix) {
        List<PCMContainer> result = new ArrayList<>();
        result.add(ioMatrixLoader.load(matrix));
        return result;
    }

    public List<PCMContainer> load(Reader reader) throws IOException {
        CSVReader csvReader = new CSVReader(reader, separator, quote);
        IOMatrix matrix = createMatrix(csvReader);
        return load(matrix);
    }
}

