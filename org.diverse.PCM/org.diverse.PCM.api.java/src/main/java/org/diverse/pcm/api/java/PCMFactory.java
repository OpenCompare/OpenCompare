package org.diverse.pcm.api.java;

/**
 * Created by gbecan on 09/10/14.
 */
public interface PCMFactory {

    PCM createPCM();
    Feature createFeature();
    FeatureGroup createFeatureGroup();
    Product createProduct();
    Cell createCell();

    // TODO : add methods for values
}
