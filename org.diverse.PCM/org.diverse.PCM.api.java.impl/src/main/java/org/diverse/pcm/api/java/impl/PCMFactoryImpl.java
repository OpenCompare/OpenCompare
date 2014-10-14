package org.diverse.pcm.api.java.impl;

import org.diverse.pcm.api.java.*;
import org.diverse.pcm.api.java.impl.value.*;
import org.diverse.pcm.api.java.value.*;
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

    @Override
    public Conditional createConditional() {
        return new ConditionalImpl(kFactory.createConditional());
    }

    @Override
    public DateValue createDateValue() {
        return null;
    }

    @Override
    public Dimension createDimension() {
        return null;
    }

    @Override
    public Multiple createMultiple() {
        return new MultipleImpl(kFactory.createMultiple());
    }

    @Override
    public NotApplicable createNotApplicable() {
        return null;
    }

    @Override
    public NotAvailable createNotAvailable() {
        return new NotAvailableImpl(kFactory.createNotAvailable());
    }

    @Override
    public Partial createPartial() {
        return null;
    }

    @Override
    public RealValue createRealValue() {
        return new RealValueImpl(kFactory.createRealValue());
    }

    @Override
    public Unit createUnit() {
        return null;
    }

    @Override
    public Version createVersion() {
        return null;
    }

}
