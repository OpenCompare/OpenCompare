package org.opencompare.api.java;

import java.util.List;

public interface FeatureGroup extends AbstractFeature {

	List<AbstractFeature> getFeatures();
	List<Feature> getConcreteFeatures();
	void addFeature(AbstractFeature feature);
	void removeFeature(AbstractFeature feature);
	int getDepth();
}
