package org.opencompare.api.java.impl;

import org.opencompare.api.java.impl.value.*;
import org.opencompare.api.java.*;
import org.opencompare.api.java.value.*;
import pcm.PcmModel;
import pcm.PcmUniverse;
import pcm.PcmView;

/**
 * Created by gbecan on 09/10/14.
 */
public class PCMFactoryImpl implements PCMFactory {

    private PcmView view;

    public PCMFactoryImpl() {
        PcmModel model = new PcmModel();
        model.connect();
        PcmUniverse universe = model.newUniverse();
        view = universe.time(0l);
    }



    @Override
    public PCM createPCM() {
        return new PCMImpl(view.createPCM());
    }

    @Override
    public Feature createFeature() {
        return new FeatureImpl(view.createFeature());
    }

    @Override
    public FeatureGroup createFeatureGroup() {
        return new FeatureGroupImpl(view.createFeatureGroup());
    }

    @Override
    public Cell createCell() {
        return new CellImpl(view.createCell());
    }

    @Override
    public Product createProduct() {
        return new ProductImpl(view.createProduct());
    }

    @Override
    public BooleanValue createBooleanValue() {
        return new BooleanValueImpl(view.createBooleanValue());
    }

    @Override
    public IntegerValue createIntegerValue() {
        return new IntegerValueImpl(view.createIntegerValue());
    }

    @Override
    public StringValue createStringValue() {
        return new StringValueImpl(view.createStringValue());
    }

    @Override
    public Conditional createConditional() {
        return new ConditionalImpl(view.createConditional());
    }

    @Override
    public DateValue createDateValue() {
        return new DateValueImpl(view.createDateValue());
    }

    @Override
    public Dimension createDimension() {
        return new DimensionImpl(view.createDimension());
    }

    @Override
    public Multiple createMultiple() {
        return new MultipleImpl(view.createMultiple());
    }

    @Override
    public NotApplicable createNotApplicable() {
        return new NotApplicableImpl(view.createNotApplicable());
    }

    @Override
    public NotAvailable createNotAvailable() {
        return new NotAvailableImpl(view.createNotAvailable());
    }

    @Override
    public Partial createPartial() {
        return new PartialImpl(view.createPartial());
    }

    @Override
    public RealValue createRealValue() {
        return new RealValueImpl(view.createRealValue());
    }

    @Override
    public Unit createUnit() {
        return new UnitImpl(view.createUnit());
    }

    @Override
    public Version createVersion() {
        return new VersionImpl(view.createVersion());
    }

}
