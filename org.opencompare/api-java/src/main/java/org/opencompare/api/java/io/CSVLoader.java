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
    private Map<Integer, Feature> features;

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

        }
        return containers;
    }

    @Override
    public List<PCMContainer> load(File file) throws IOException {
        List<PCMContainer> containers = new ArrayList<>();
        try {
            containers = load(new FileReader(file));
        } catch (IOException e) {

        }
        return containers;
    }

    public List<PCMContainer> load(IOMatrix matrix) {
        List<PCMContainer> containers = new ArrayList<>();
        MatrixAnalyser detector = new MatrixAnalyser(matrix, new MatrixComparatorEqualityImpl());
        containers.add(load(detector));
        return containers;
    }

    public List<PCMContainer> load(Reader reader) throws IOException {
        CSVReader csvReader = new CSVReader(reader, separator, quote);
        IOMatrix matrix = createMatrix(csvReader);
        return load(matrix);
    }

    private PCMContainer load(MatrixAnalyser detector) {
        PCM pcm = factory.createPCM();
        PCMMetadata metadata = new PCMMetadata(pcm);
        metadata.setProductAsLines(this.productsAsLines);
        PCMContainer container = new PCMContainer(metadata);
        int headerLength = detector.getHeaderHeight();
        int matrixHeight = detector.getHeight();
        int matrixWidth = detector.getWidth();
        int headerColumnStart = detector.getHeaderColumnOffset();

        createFeatures(detector);

        for (int i = headerLength; i < matrixHeight; i++) {
            // Products
            Product product = factory.createProduct();
            product.setName(detector.get(i, 0).getContent());
            pcm.addProduct(product);
            // And keep the order in metadata
            metadata.setProductPosition(product, i);

            // Cells
            for (int j = headerColumnStart; j < matrixWidth; j++) {
                Cell cell = factory.createCell();
                IOCell ioCell = detector.get(i, j);
                cell.setContent(ioCell.getContent());
                cell.setFeature(features.get(j));
                product.addCell(cell);
            }
        }
        container.getPcm().setName(detector.getMatrix().getName());
        return container;
    }

    private void parseNodes(FeatureGroup parent, List<IONode> nodes, String tabulation) {
        for (IONode node: nodes) {
            if (node.isLeaf()) {
                Feature feature = factory.createFeature();
                feature.setName(node.getName());
                if (parent != null) {
                    parent.addFeature(feature);
                }
                features.put(node.getPosition(), feature);
            } else {
                FeatureGroup featureGroup = factory.createFeatureGroup();
                featureGroup.setName(node.getName());
                if (parent != null) {
                    parent.addFeature(featureGroup);
                }
                parseNodes(featureGroup, node.iterable(), tabulation + "\t");
            }
        }
    }

    public void createFeatures(MatrixAnalyser detector) {
        this.features = new HashMap<>();
        parseNodes(null, detector.getHeaderNode().iterable(), "");
    }
}

