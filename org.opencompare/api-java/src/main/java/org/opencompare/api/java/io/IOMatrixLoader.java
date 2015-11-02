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
        List<AbstractFeature> topFeatures = createFeatures(featureTreeRoot, positionToFeature);
        topFeatures.forEach(feature -> pcm.addFeature(feature));

        // Set feature positions in metadata
        for (Map.Entry<Integer, Feature> entry : positionToFeature.entrySet()) {
            pcmMetadata.setFeaturePosition(entry.getValue(), entry.getKey());
        }

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

        IONode<String> root = new IONode<>(null);
        List<IONode<String>> parents = new ArrayList<>();


        int maxX, maxY;

        if (detectedDirection == PCMDirection.PRODUCTS_AS_LINES) {
            // X are rows
            maxX = matrix.getNumberOfRows();
            // Y are columns
            maxY = matrix.getNumberOfColumns();
        } else {
            // X are columns
            maxX = matrix.getNumberOfColumns();
            // Y are rows
            maxY = matrix.getNumberOfRows();
        }


        // Init parents
        for (int y = 0; y < maxY; y++) {
            parents.add(root);
        }

        // Detect features
        for (int x = 0; x < maxX; x++) {
            List<IONode<String>> nextParents = new ArrayList<>(parents);

            for (int y = 0; y < maxY; y++) {
                IOCell currentCell;
                if (detectedDirection == PCMDirection.PRODUCTS_AS_LINES) {
                    currentCell = matrix.getCell(x, y);
                } else {
                    currentCell = matrix.getCell(y, x);
                }

                if (currentCell == null) {
                    currentCell = new IOCell("");
                }

                IONode<String> parent = parents.get(y);

                boolean sameAsParent = currentCell.getContent().equals(parent.getContent());
                boolean sameAsPrevious = false;
                boolean sameParentAsPrevious = true;

                if (y > 0) {
                    IOCell previousCell;
                    if (detectedDirection == PCMDirection.PRODUCTS_AS_LINES) {
                        previousCell = matrix.getCell(x, y - 1);
                    } else {
                        previousCell = matrix.getCell(y - 1, x);
                    }

                    if (previousCell == null) {
                        previousCell = new IOCell("");
                    }

                    sameAsPrevious = currentCell.getContent().equals(previousCell.getContent());

                    if (parent.getContent() != null) {
                        sameParentAsPrevious = parent.getContent().equals(parents.get(y - 1).getContent());
                    }
                }

                if (!sameAsParent && (!sameParentAsPrevious || !sameAsPrevious)) {
                    IONode<String> newNode = new IONode<>(currentCell.getContent());
                    newNode.getPositions().add(y);
                    parent.getChildren().add(newNode);
                    nextParents.set(y, newNode);
                } else if (y > 0 && sameParentAsPrevious && sameAsPrevious) {
                    IONode<String> previousNode = nextParents.get(y - 1);
                    previousNode.getPositions().add(y);
                    nextParents.set(y, previousNode);
                }

            }

            parents = nextParents;

            // If number of getLeaves == number of rows : break;
            if (root.getLeaves().size() == matrix.getNumberOfColumns()) {
                break;
            }
        }

        return root;
    }

    protected List<AbstractFeature> createFeatures(IONode<String> parent, Map<Integer, Feature> positionToFeature) {
        List<AbstractFeature> result = new ArrayList<>();

        for (IONode<String> child : parent.getChildren()) {
            if (child.isLeaf()) {
                Feature feature = factory.createFeature();
                feature.setName(child.getContent());
                result.add(feature);

                for (Integer position : child.getPositions()) {
                    positionToFeature.put(position, feature);
                }

            } else {
                FeatureGroup featureGroup = factory.createFeatureGroup();
                featureGroup.setName(child.getContent());

                List<AbstractFeature> subFeatures = createFeatures(child, positionToFeature);
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
                    cell.setFeature(positionToFeature.get(y));
                }

                if (ioCell == null) {
                    ioCell = new IOCell("");
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
