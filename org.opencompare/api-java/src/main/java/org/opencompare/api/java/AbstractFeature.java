package org.opencompare.api.java;

public interface AbstractFeature extends PCMElement {

    String getName();
    void setName(String name);
    FeatureGroup getParentGroup();

}
