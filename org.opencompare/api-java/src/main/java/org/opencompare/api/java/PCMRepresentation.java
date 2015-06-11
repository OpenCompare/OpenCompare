package org.opencompare.api.java;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by gbecan on 6/10/15.
 */
public class PCMRepresentation {

    protected PCM pcm;
    protected Map<Product, Integer> productPositions;
    protected Map<Feature, Integer> featurePositions;

    public PCMRepresentation(PCM pcm) {
        this.pcm = pcm;
        this.productPositions = new HashMap<Product, Integer>();
        this.featurePositions = new HashMap<Feature, Integer>();
    }

    /**
     * Returns the absolute position of the product
     * @param product
     * @return the absolution position of 'product' or null if it is not specified
     */
    public int getProductPosition(Product product) {
        return productPositions.get(product);
    }

    /**
     * Define the absolute position of the product in the PCM
     * @param product
     * @param position
     */
    public void setProductPosition(Product product, int position) {
        productPositions.put(product, position);
    }

    /**
     * Returns the absolute position of the feature
     * @param feature
     * @return the absolution position of 'feature' or null if it is not specified
     */
    public int getFeaturePosition(Feature feature) {
        return featurePositions.get(feature);
    }

    /**
     * Define the absolute position of the feature in the PCM
     * @param feature
     * @param position
     */
    public void setFeaturePosition(Feature feature, int position) {
        featurePositions.put(feature, position);
    }

}
