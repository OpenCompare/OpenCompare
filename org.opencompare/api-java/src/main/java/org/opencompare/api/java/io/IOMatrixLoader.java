package org.opencompare.api.java.io;

import org.opencompare.api.java.*;

import java.util.*;
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

        // Compute content of cells

        // Detect types and information for each cell
        detectTypes(matrix);

        // Detect direction of the matrix
        PCMDirection detectedDirection = direction;
        if (detectedDirection == PCMDirection.UNKNOWN) {
            detectedDirection = detectDirection(matrix);
        }

        // Transpose matrix
        if (detectedDirection == PCMDirection.PRODUCTS_AS_COLUMNS) {
            matrix.transpose();
        }

        // Remove duplicated lines
        matrix.removeDuplicatedLines();

        // Create PCM
        PCM pcm = factory.createPCM();
        PCMMetadata pcmMetadata = new PCMMetadata(pcm);
        PCMContainer pcmContainer = new PCMContainer(pcmMetadata);

        // Set info
        pcm.setName(matrix.getName());

        // Create features
        IONode<String> featureTreeRoot = detectFeatures(matrix, pcmContainer);
        Map<Integer, Feature> positionToFeature = new HashMap<>();
        List<AbstractFeature> topFeatures = createFeatures(featureTreeRoot, positionToFeature);
        topFeatures.forEach(feature -> pcm.addFeature(feature));

        // Set feature positions in metadata
        for (Map.Entry<Integer, Feature> entry : positionToFeature.entrySet()) {
            pcmMetadata.setFeaturePosition(entry.getValue(), entry.getKey());
        }

        // Create products
        createProducts(matrix, pcmContainer, positionToFeature);

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
     * @param pcmContainer
     */
    protected IONode<String> detectFeatures(IOMatrix matrix, PCMContainer pcmContainer) {

        IONode<String> root = new IONode<>(null);
        List<IONode<String>> parents = new ArrayList<>();

        // Init parents
        for (int column = 0; column < matrix.getNumberOfColumns(); column++) {
            parents.add(root);
        }

        // Detect features
        for (int row = 0; row < matrix.getNumberOfRows(); row++) {
            List<IONode<String>> nextParents = new ArrayList<>(parents);

            for (int column = 0; column < matrix.getNumberOfColumns(); column++) {
                IOCell currentCell = matrix.getCell(row, column);

                if (currentCell == null) {
                    currentCell = new IOCell("");
                }

                IONode<String> parent = parents.get(column);

                boolean sameAsParent = currentCell.getContent().equals(parent.getContent());
                boolean sameAsPrevious = false;
                boolean sameParentAsPrevious = true;

                if (column > 0) {
                    IOCell previousCell = matrix.getCell(row, column - 1);

                    if (previousCell == null) {
                        previousCell = new IOCell("");
                    }

                    sameAsPrevious = currentCell.getContent().equals(previousCell.getContent());

                    if (parent.getContent() != null) {
                        sameParentAsPrevious = parent.getContent().equals(parents.get(column - 1).getContent());
                    }
                }

                if (!sameAsParent && (!sameParentAsPrevious || !sameAsPrevious)) {
                    IONode<String> newNode = new IONode<>(currentCell.getContent());
                    newNode.getPositions().add(column);
                    parent.getChildren().add(newNode);
                    nextParents.set(column, newNode);
                } else if (column > 0 && sameParentAsPrevious && sameAsPrevious) {
                    IONode<String> previousNode = nextParents.get(column - 1);
                    previousNode.getPositions().add(column);
                    nextParents.set(column, previousNode);
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
     * @param pcmContainer
     */
    protected void createProducts(IOMatrix matrix, PCMContainer pcmContainer, Map<Integer, Feature> positionToFeature) {

        int featuresDepth = pcmContainer.getPcm().getFeaturesDepth();

        for (int row = featuresDepth; row < matrix.getNumberOfRows(); row++) {
            Product product = factory.createProduct();

            for (int column = 0; column < matrix.getNumberOfColumns(); column++) {
                Cell cell = factory.createCell();
                cell.setFeature(positionToFeature.get(column));

                IOCell ioCell = matrix.getCell(row, column);

                if (ioCell == null) {
                    ioCell = new IOCell("");
                }

                cell.setContent(ioCell.getContent());
                cell.setRawContent(ioCell.getRawContent());

                product.addCell(cell);
            }

            pcmContainer.getPcm().addProduct(product);
            pcmContainer.getMetadata().setProductPosition(product, row);
        }

    }

    protected void setProductsKey(PCMContainer pcmContainer) {
        boolean foundProductsKey = false;

        PCMMetadata metadata = pcmContainer.getMetadata();

        // Find feature that can be the products key
        for (Feature feature : metadata.getSortedFeatures()) {
            int disctintCells = feature.getCells().stream()
                    .map(cell -> cell.getContent())
                    .distinct()
                    .collect(Collectors.toList())
                    .size();

            if (disctintCells == feature.getCells().size()) {
                foundProductsKey = true;
                pcmContainer.getPcm().setProductsKey(feature);
                break;
            }
        }

        // If not feature are products key candidates, create a new feature
        if (!foundProductsKey) {
            Feature productsKey = factory.createFeature();
            productsKey.setName("Products");

            int index = 1;
            for (Product product : metadata.getSortedProducts()) {
                int position = metadata.getProductPosition(product);
                metadata.clearProductPosition(product);

                Cell cell = factory.createCell();
                cell.setContent("P" + index);
                cell.setRawContent(cell.getContent());
                cell.setFeature(productsKey);

                product.addCell(cell);

                metadata.setProductPosition(product, position);

                index++;
            }

            pcmContainer.getPcm().addFeature(productsKey);
            pcmContainer.getPcm().setProductsKey(productsKey);

            for (Feature feature : pcmContainer.getPcm().getConcreteFeatures()) {
                metadata.setFeaturePosition(feature, metadata.getFeaturePosition(feature) + 1);
            }
            metadata.setFeaturePosition(productsKey, 0);
        }
    }

}
