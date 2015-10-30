package org.opencompare.api.java.io;

import org.opencompare.api.java.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by gbecan on 10/16/15.
 */
public class IOMatrixLoader {

    private PCMFactory factory;
    private PCMDirection direction;


    public IOMatrixLoader(PCMFactory factory, PCMDirection direction) {
        this.factory = factory;
        this.direction = direction;
    }


    public PCMContainer load(IOMatrix matrix) {

        // Detect types and information for each cell
        detectTypes(matrix);

        // Detect direction of the matrix
        PCMDirection detectedDirection = direction;
        if (detectedDirection == PCMDirection.UNKNOWN) {
            detectedDirection = detectDirection(matrix);
        }

        // Create PCM
        PCM pcm = factory.createPCM();
        PCMMetadata pcmMetadata = new PCMMetadata(pcm);
        PCMContainer pcmContainer = new PCMContainer(pcmMetadata);

        // Set info
        pcm.setName(matrix.getName());

        // Create features
        IONode<String> featureTreeRoot = detectFeatures(matrix, detectedDirection, pcmContainer);
        Map<Integer, Feature> positionToFeature = new HashMap<>();
        List<AbstractFeature> topFeatures = createFeatures(featureTreeRoot, positionToFeature, pcmMetadata);
        topFeatures.forEach(feature -> pcm.addFeature(feature));


        // Create products
        createProducts(matrix, detectedDirection, pcmContainer, positionToFeature);

        // Set products key
        setProductsKey(pcmContainer);

        return pcmContainer;
    }

    /**
     * Detect types of each cell of the matrix
     * @param matrix
     */
    protected void detectTypes(IOMatrix matrix) {

    }

    /**
     * Detect if products are represented by a line or a column
     * @param matrix
     * @return
     */
    protected PCMDirection detectDirection(IOMatrix matrix) {
        return PCMDirection.PRODUCTS_AS_LINES;
    }


    /**
     * Detect features from the information contained in the matrix and the provided direction
     * @param matrix
     * @param detectedDirection
     * @param pcmContainer
     */
    protected IONode<String> detectFeatures(IOMatrix matrix, PCMDirection detectedDirection, PCMContainer pcmContainer) {

        IONode<String> root = new IONode<>("");
        List<IONode<String>> parents = new ArrayList<>();

        if (detectedDirection == PCMDirection.PRODUCTS_AS_LINES) {

            // Init parents
            for (int r = 0; r < matrix.getNumberOfColumns(); r++) {
                parents.add(root);
            }

            for (int r = 0; r < matrix.getNumberOfRows(); r++) {
                for (int c = 0; c < matrix.getNumberOfColumns(); c++) {
                    IOCell currentCell = matrix.getCell(r, c);
                    IONode<String> parent = parents.get(c);



                    boolean sameAsParent = parent.getContent().equals(currentCell.getContent());
                    boolean sameAsPrevious = false;
                    boolean sameParentAsPrevious = false;
                    if (c > 0) {
                        IOCell previousCell = matrix.getCell(r, c - 1);
                        sameAsPrevious = previousCell.getContent().equals(currentCell.getContent());
                        sameParentAsPrevious = parents.get(c - 1).getContent().equals(parent.getContent());
                    }

                    if (!sameAsParent && (!sameParentAsPrevious || (sameParentAsPrevious && !sameAsPrevious))) {
                        IONode<String> newNode = new IONode<>(currentCell.getContent());
                        newNode.getPositions().add(c);
                        parent.getChildren().add(newNode);
                        parents.set(c, newNode);
                    }

                }

                // If number of getLeaves == number of rows : break;
                if (root.getLeaves().size() == matrix.getNumberOfColumns()) {
                    break;
                }
            }
        } else {
            throw new UnsupportedOperationException("product as columns");
        }

        return root;
    }

    protected List<AbstractFeature> createFeatures(IONode<String> parent, Map<Integer, Feature> positionToFeature, PCMMetadata pcmMetadata) {
        List<AbstractFeature> result = new ArrayList<>();

        for (IONode<String> child : parent.getChildren()) {
            if (child.isLeaf()) {
                Feature feature = factory.createFeature();
                feature.setName(child.getContent());
                result.add(feature);

                for (Integer position : child.getPositions()) {
                    positionToFeature.put(position, feature);
                    pcmMetadata.setFeaturePosition(feature, position);
                }

            } else {
                FeatureGroup featureGroup = factory.createFeatureGroup();
                featureGroup.setName(child.getContent());

                List<AbstractFeature> subFeatures = createFeatures(child, positionToFeature, pcmMetadata);
                subFeatures.forEach(subFeature -> featureGroup.addFeature(subFeature));

                result.add(featureGroup);
            }
        }

        return result;
    }

    /**
     * Detect and create products from the information contained in the matrix and the provided direction
     * @param matrix
     * @param detectedDirection
     * @param pcmContainer
     */
    protected void createProducts(IOMatrix matrix, PCMDirection detectedDirection, PCMContainer pcmContainer, Map<Integer, Feature> positionToFeature) {

        int featuresDepth = pcmContainer.getPcm().getFeaturesDepth();

        int initX, initY, maxX, maxY;

        if (detectedDirection == PCMDirection.PRODUCTS_AS_LINES) {
            initX = featuresDepth;
            maxX = matrix.getNumberOfRows();
            initY = 0;
            maxY = matrix.getNumberOfColumns();
        } else {
            initX = featuresDepth;
            maxX = matrix.getNumberOfColumns();
            initY = 0;
            maxY = matrix.getNumberOfRows();
        }

        for (int x = initX; x < maxX; x++) {
            Product product = factory.createProduct();
            pcmContainer.getMetadata().setProductPosition(product, x);

            for (int y = initY; y < maxY; y++) {
                Cell cell = factory.createCell();

                IOCell ioCell;
                if (detectedDirection == PCMDirection.PRODUCTS_AS_LINES) {
                    ioCell = matrix.getCell(x, y);
                    cell.setFeature(positionToFeature.get(y));
                } else {
                    ioCell = matrix.getCell(y, x);
                    cell.setFeature(positionToFeature.get(x));
                }

                cell.setContent(ioCell.getContent());
                cell.setRawContent(ioCell.getRawContent());

                product.addCell(cell);
            }

            pcmContainer.getPcm().addProduct(product);
        }

    }

    protected void setProductsKey(PCMContainer pcmContainer) {
        for (Feature feature : pcmContainer.getMetadata().getSortedFeatures()) {
            int disctintCells = feature.getCells().stream()
                    .map(cell -> cell.getContent())
                    .distinct()
                    .collect(Collectors.toList())
                    .size();
            if (disctintCells == feature.getCells().size()) {
                pcmContainer.getPcm().setProductsKey(feature);
                break;
            }
        }
    }

}
