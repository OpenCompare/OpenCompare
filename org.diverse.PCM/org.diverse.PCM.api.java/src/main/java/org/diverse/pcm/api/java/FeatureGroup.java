package org.diverse.pcm.api.java;

import java.util.List;

public interface FeatureGroup extends AbstractFeature {

	List<AbstractFeature> getFeatures();
	void addFeature(AbstractFeature feature);
	void removeFeature(AbstractFeature feature);
	
}
