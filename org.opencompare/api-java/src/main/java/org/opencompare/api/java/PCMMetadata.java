package org.opencompare.api.java;

import org.opencompare.api.java.util.DiffResult;

import java.util.*;

/**
 * Created by gbecan on 6/10/15.
 */
public class PCMMetadata {

    protected PCM pcm;
    protected Map<Product, Integer> productPositions;
    protected Map<Feature, Integer> featurePositions;

    public PCMMetadata(PCM pcm) {
        this.pcm = pcm;
        this.productPositions = new HashMap<Product, Integer>();
        this.featurePositions = new HashMap<Feature, Integer>();
    }

    /**
     * Return the last product index used
     * @return a integer as index
     */
    public Integer getLastProductIndex() {
        Integer result = 0;
        for (Product product : productPositions.keySet()) {
            Integer index = getProductPosition(product);
            if (result < index) {
                result = index;
            }
        }
        return result;
    }
    /**
     * Return the last feature index used
     * @return a integer as index
     */
    public Integer getLastFeatureIndex() {
        Integer result = 0;
        for (Feature feature : featurePositions.keySet()) {
            Integer index = getFeaturePosition(feature);
            if (result < index) {
                result = index;
            }
        }
        return result;
    }

    /**
     * Returns the absolute position of the product or create if not exists
     * @param product
     * @return the absolution position of 'product' or null if it is not specified
     */
    public int getProductPosition(Product product) {
        if (!productPositions.containsKey(product)) {
            Integer index = getLastProductIndex() + 1;
            setProductPosition(product, index);
            return index;
        }
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
     * Returns the absolute position of the feature or create if not exists
     * @param feature
     * @return the absolution position of 'feature' or null if it is not specified
     */
    public int getFeaturePosition(Feature feature) {
        if (!featurePositions.containsKey(feature)) {
            Integer index = getLastFeatureIndex() + 1;
            setFeaturePosition(feature, index);
            return index;
        }
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

    /**
     * Return the sorted products concordingly with metadata
     * @return an ordered list of products
     */
    public List<Product> getSortedProducts() {
        ArrayList<Product> result = new ArrayList<>();
        for (Product product : pcm.getProducts()) {
            result.add(getProductPosition(product) - 1, product);
        }
        return result;
    }

    /**
     * Return the sorted features concordingly with metadata
     * @return an ordered list of features
     */
    public List<Feature> getSortedFeatures() {
        ArrayList<Feature> result = new ArrayList<>();
        for (Feature feature : pcm.getConcreteFeatures()) {
            result.add(getFeaturePosition(feature) - 1, feature);
        }
        return result;
    }

    /**
     * Compare with an other metadata instance
     * @param metadata
     * @return false if metadata given as parameter not equal to this
     */
    public Boolean hasDifferences(PCMMetadata metadata) {
        if (!getSortedProducts().retainAll(metadata.getSortedProducts())) {
            return true;
        }
        if (!getSortedFeatures().retainAll(metadata.getSortedFeatures())) {
            return true;
        }
        for (Product product : productPositions.keySet()){
            for (Product metaProd : metadata.getSortedProducts()) {
                if (product.getName().equals(metaProd.getName())) {
                    if (productPositions.get(product) != metadata.getProductPosition(metaProd)) {
                        return true;
                    }
                }
            }
        }
        for (Feature feature : featurePositions.keySet()){
            for (Feature metaFeat : metadata.getSortedFeatures()) {
                if (feature.getName().equals(metaFeat.getName())) {
                    if (featurePositions.get(feature) != metadata.getFeaturePosition(metaFeat)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public String toString() {
        String result = "PCMMetadata(";
        result += productPositions.toString() + ", ";
        result += featurePositions.toString() + ")";
        return result;
    }
}
