package org.opencompare.api.java;

import org.opencompare.api.java.value.*;

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
    Conditional createConditional();
    DateValue createDateValue();
    Dimension createDimension();
    IntegerValue createIntegerValue();
    Multiple createMultiple();
    NotApplicable createNotApplicable();
    NotAvailable createNotAvailable();
    Partial createPartial();
    RealValue createRealValue();
    StringValue createStringValue();
    Unit createUnit();
    Version createVersion();
}
