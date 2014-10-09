package org.diverse.pcm.api.java.impl;

import org.diverse.pcm.api.java.*;
import org.diverse.pcm.api.java.impl.value.BooleanValueImpl;
import org.diverse.pcm.api.java.impl.value.IntegerValueImpl;
import org.diverse.pcm.api.java.impl.value.StringValueImpl;
import org.diverse.pcm.api.java.value.BooleanValue;
import org.diverse.pcm.api.java.value.IntegerValue;
import org.diverse.pcm.api.java.value.StringValue;
import pcm.factory.DefaultPcmFactory;
import pcm.factory.PcmFactory;

/**
 * Created by gbecan on 09/10/14.
 */
public class PCMFactoryImpl implements PCMFactory {

    private PcmFactory kFactory = new DefaultPcmFactory();

    @Override
    public PCM createPCM() {
        return new PCMImpl(kFactory.createPCM());
    }

    @Override
    public Feature createFeature() {
        return new FeatureImpl(kFactory.createFeature());
    }

    @Override
    public FeatureGroup createFeatureGroup() {
        return new FeatureGroupImpl(kFactory.createFeatureGroup());
    }

    @Override
    public Cell createCell() {
        return new CellImpl(kFactory.createCell());
    }

    @Override
    public Product createProduct() {
        return new ProductImpl(kFactory.createProduct());
    }

    @Override
    public BooleanValue createBooleanValue() {
        return new BooleanValueImpl(kFactory.createBooleanValue());
    }

    @Override
    public IntegerValue createIntegerValue() {
        return new IntegerValueImpl(kFactory.createIntegerValue());
    }

    @Override
    public StringValue createStringValue() {
        return new StringValueImpl(kFactory.createStringValue());
    }
}
