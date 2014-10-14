package org.diverse.pcm.api.java;

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
	
}
