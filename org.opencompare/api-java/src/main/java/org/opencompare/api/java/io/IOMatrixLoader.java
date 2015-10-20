package org.opencompare.api.java.io;

import org.opencompare.api.java.PCM;
import org.opencompare.api.java.PCMContainer;
import org.opencompare.api.java.PCMFactory;

import java.util.ArrayList;
import java.util.List;

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
        PCMContainer pcmContainer = new PCMContainer(pcm);

        // Set info
        pcm.setName(matrix.getName());

        // Create features and products
        createFeatures(matrix, detectedDirection, pcmContainer);
        createProducts(matrix, detectedDirection, pcmContainer);

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
     * Detect and create features from the information contained in the matrix and the provided direction
     * @param matrix
     * @param detectedDirection
     * @param pcmContainer
     */
    protected void createFeatures(IOMatrix matrix, PCMDirection detectedDirection, PCMContainer pcmContainer) {

    }

    /**
     * Detect and create products from the information contained in the matrix and the provided direction
     * @param matrix
     * @param detectedDirection
     * @param pcmContainer
     */
    protected void createProducts(IOMatrix matrix, PCMDirection detectedDirection, PCMContainer pcmContainer) {

    }

}
