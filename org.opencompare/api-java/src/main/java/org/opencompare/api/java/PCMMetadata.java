package org.opencompare.api.java;

import java.util.*;

/**
 * Created by gbecan on 6/10/15.
 */
public class PCMMetadata {

    protected Map<Product, Integer> productPositions;
    protected Map<Feature, Integer> featurePositions;

    public PCMMetadata(PCM pcm) {
        this.productPositions = new HashMap<Product, Integer>();
        this.featurePositions = new HashMap<Feature, Integer>();
    }

    public static <K, V extends Comparable<? super V>> Map<K, V>  sortByValue( Map<K, V> map )
    {
        List<Map.Entry<K, V>> list =
            new LinkedList<>( map.entrySet() );
        Collections.sort( list, new Comparator<Map.Entry<K, V>>()
        {
            @Override
            public int compare( Map.Entry<K, V> o1, Map.Entry<K, V> o2 )
            {
                return (o1.getValue()).compareTo( o2.getValue() );
            }
        } );

        Map<K, V> result = new LinkedHashMap<>();
        for (Map.Entry<K, V> entry : list)
        {
            result.put( entry.getKey(), entry.getValue() );
        }
        return result;
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

    public Set<Product> getSortedProducts() {
        return sortByValue(productPositions).keySet();
    }

    public Set<Feature> getSortedFeatures() {
        return sortByValue(featurePositions).keySet();
    }

}
