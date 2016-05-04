package org.opencompare.api.java;

import org.opencompare.api.java.exception.MergeConflictException;
import org.opencompare.api.java.util.DiffResult;
import org.opencompare.api.java.util.PCMElementComparator;

import java.util.List;

public interface PCM extends PCMElement {

	String getName();
	void setName(String name);
	
	List<Product> getProducts();
	void addProduct(Product product);
	void removeProduct(Product product);

    Feature getProductsKey();
    void setProductsKey(Feature feature);
	
	List<AbstractFeature> getFeatures();
	void addFeature(AbstractFeature feature);
	void removeFeature(AbstractFeature feature);

    /**
     * List all the features of the PCM (no feature group is returned)
     * @return features of the PCM
     */
    List<Feature> getConcreteFeatures();

    /**
     * Retrieve an existing feature
     * If the feature does not exist, we create and add it to the PCM
     * @param name name of the feature to search
     * @param factory : PCM factory
     * @return searched feature
     */
    Feature getOrCreateFeature(String name, PCMFactory factory);

    /**
     * Retrieve an existing product
     * If the product does not exist, we create and add it to the PCM
     * @param name name of the product to search
     * @param factory PCM factory
     * @return searched product
     */
    Product getOrCreateProduct(String name, PCMFactory factory);

    /**
     * Add information from another PCM
     * @param pcm PCM to merge
     * @param factory PCM factory
     */
    void merge(PCM pcm, PCMFactory factory) throws MergeConflictException;

    /**
     * Check if the PCM is well formed
     * @return the PCM is well formed
     */
    boolean isValid();

    /**
     * Fill missing cells with not available cells
     * @param factory PCM factory
     */
    void normalize(PCMFactory factory);

    /**
     * Compute the difference with another PCM
     * @param pcm PCM to compare with
     * @param pcmElementComparator comparator to use to compute the difference between the elements of the PCM
     * @return difference between the two PCMs
     */
    DiffResult diff(PCM pcm, PCMElementComparator pcmElementComparator);

    /**
     * Invert the matrix (products become features and vice versa)
     * @param factory PCM factory
     */
    void invert(PCMFactory factory);

    int getFeaturesDepth();
}
