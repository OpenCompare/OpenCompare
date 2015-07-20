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
public class IOMatrixLoader {

    private PCMFactory factory;
    private boolean productsAsLines;
    private Map<Integer, AbstractFeature> features;

    public IOMatrixLoader(PCMFactory factory) {
        this(factory, true);
    }

    public IOMatrixLoader(PCMFactory factory, boolean productsAsLines) {
        this.factory = factory;
        this.productsAsLines = productsAsLines;
    }

    public List<PCMContainer> load(IOMatrix matrix) {
        List<PCMContainer> containers = new ArrayList<>();
        MatrixAnalyser detector = new MatrixAnalyser(matrix, new MatrixComparatorEqualityImpl());
        containers.add(load(detector));
        return containers;
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

        createFeatures(detector, container);

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
                cell.setFeature((Feature) features.get(j));
                product.addCell(cell);
            }
        }
        container.getPcm().setName(detector.getMatrix().getName());
        return container;
    }

    private void parseNodes(FeatureGroup parent, List<IONode> nodes, PCMContainer container) {
        for (IONode node : nodes) {
            if (node.isLeaf()) {
                Feature feature = factory.createFeature();
                feature.setName(node.getName());
                if (parent != null) {
                    parent.addFeature(feature);
                } else {
                    // Save features in metadata and PCM with position
                    container.getPcm().addFeature(feature);
                    container.getMetadata().setFeaturePosition(feature, node.getPosition());
                }
                // Save feature indice to allow cell to be linked with the desire concrete feature
                features.put(node.getPosition(), feature);
            } else {
                FeatureGroup featureGroup = factory.createFeatureGroup();
                featureGroup.setName(node.getName());
                if (parent != null) {
                    parent.addFeature(featureGroup);
                } else {
                    // Save features in PCM to allow featureGroups depth calculus
                    container.getPcm().addFeature(featureGroup);
                    container.getMetadata().setFeaturePosition(featureGroup, node.getPosition());
                }
                parseNodes(featureGroup, node.iterable(), container);
            }
        }
    }

    public void createFeatures(MatrixAnalyser detector, PCMContainer container) {
        this.features = new HashMap<>();
        parseNodes(null, detector.getHeaderNode().iterable(), container);
    }
}

