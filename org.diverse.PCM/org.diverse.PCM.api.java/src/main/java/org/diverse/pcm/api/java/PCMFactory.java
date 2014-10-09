package org.diverse.pcm.api.java;

import org.diverse.pcm.api.java.value.BooleanValue;
import org.diverse.pcm.api.java.value.IntegerValue;
import org.diverse.pcm.api.java.value.StringValue;

/**
 * Created by gbecan on 09/10/14.
 */
public interface PCMFactory {

    PCM createPCM();
    Feature createFeature();
    FeatureGroup createFeatureGroup();
    Product createProduct();
    Cell createCell();

    BooleanValue createBooleanValue();
    IntegerValue createIntegerValue();
    StringValue createStringValue();
    // TODO : add methods for other values
}
