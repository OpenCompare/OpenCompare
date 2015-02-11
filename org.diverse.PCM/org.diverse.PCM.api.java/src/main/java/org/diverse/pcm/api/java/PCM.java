package org.diverse.pcm.api.java;

import org.diverse.pcm.api.java.exception.MergeConflictException;

import java.util.List;

public interface PCM extends PCMElement {

	String getName();
	void setName(String name);
	
	List<Product> getProducts();
	void addProduct(Product product);
	void removeProduct(Product product);
	
	List<AbstractFeature> getFeatures();
	void addFeature(AbstractFeature feature);
	void removeFeature(AbstractFeature feature);

    List<Feature> getConcreteFeatures();

    /**
     * Add information from another PCM
     * @param pcm
     */
    void merge(PCM pcm, PCMFactory factory) throws MergeConflictException;

    /**
     * Check if the PCM is well formed
     * @return
     */
    boolean isValid();
	
}
