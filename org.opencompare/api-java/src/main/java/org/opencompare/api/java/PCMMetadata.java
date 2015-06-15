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
        return Collections.max(productPositions.values());
    }
    /**
     * Return the last feature index used
     * @return a integer as index
     */
    public Integer getLastFeatureIndex() {
        return Collections.max(featurePositions.values());
    }

    /**
     * Returns the absolute position of the product or create if not exists
     * @param product
     * @return the absolution position of 'product' or -1 if it is not specified
     */
    public int getProductPosition(Product product) {
        if (!productPositions.containsKey(product)) {
            return -1;
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
     * @return the absolution position of 'feature' or -1 if it is not specified
     */
    public int getFeaturePosition(Feature feature) {
        if (!featurePositions.containsKey(feature)) {
            return -1;
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
        ArrayList<Product> result = new ArrayList<>(pcm.getProducts());
        Collections.sort(result, new Comparator<Product>() {
            @Override
            public int compare(Product o1, Product o2) {
                Integer op1 = getProductPosition(o1);
                Integer op2 = getProductPosition(o2);
                return op1.compareTo(op2);
            }
        });
        return result;
    }

    /**
     * Return the sorted features concordingly with metadata
     * @return an ordered list of features
     */
    public List<Feature> getSortedFeatures() {
        ArrayList<Feature> result = new ArrayList<>(pcm.getConcreteFeatures());
        Collections.sort(result, new Comparator<Feature>() {
            @Override
            public int compare(Feature f1, Feature f2) {
                Integer fp1 = getFeaturePosition(f1);
                Integer fp2 = getFeaturePosition(f2);
                return fp1.compareTo(fp2);
            }
        });
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj){ return true;}
        if (obj instanceof PCMMetadata) {
            PCMMetadata metadata = (PCMMetadata) obj;
            if (metadata.pcm == null || this.pcm == null) {
                return false;
            }
            if (metadata.pcm.equals(this.pcm)) {
                for (Product product : this.pcm.getProducts()) {
                    if (getProductPosition(product) != metadata.getProductPosition(product)) {
                        System.out.println(product.toString());
                        System.out.println(getProductPosition(product));
                        System.out.println(metadata.getProductPosition(product));
                        System.out.println(metadata.toString());
                        return false;
                    }
                }
                for (Feature feature : this.pcm.getConcreteFeatures()) {
                    if (getFeaturePosition(feature) != metadata.getFeaturePosition(feature)) {
                        return false;
                    }
                }
                return true;
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
