package org.opencompare.api.java.impl;

import org.opencompare.api.java.PCMFactory;
import org.opencompare.api.java.Value;
import org.opencompare.api.java.impl.value.*;

/**
 * Created by gbecan on 09/10/14.
 */
public abstract class ValueImpl implements Value {

    private org.opencompare.model.Value kValue;

    protected ValueImpl(org.opencompare.model.Value kValue) {
        this.kValue = kValue;
    }

    public org.opencompare.model.Value getkValue() {
        return kValue;
    }


    public static ValueImpl wrapValue(org.opencompare.model.Value kValue) {
        if (kValue == null) {
            return null;
        } else if (kValue instanceof org.opencompare.model.BooleanValue) {
            return new BooleanValueImpl((org.opencompare.model.BooleanValue) kValue);
        } else if (kValue instanceof org.opencompare.model.IntegerValue) {
            return new IntegerValueImpl((org.opencompare.model.IntegerValue) kValue);
        } else if (kValue instanceof org.opencompare.model.StringValue) {
            return new StringValueImpl((org.opencompare.model.StringValue) kValue);
        } else if (kValue instanceof org.opencompare.model.RealValue) {
            return new RealValueImpl((org.opencompare.model.RealValue) kValue);
        } else if (kValue instanceof org.opencompare.model.Multiple) {
            return new MultipleImpl((org.opencompare.model.Multiple) kValue);
        } else if (kValue instanceof org.opencompare.model.NotApplicable) {
            return new NotApplicableImpl((org.opencompare.model.NotApplicable) kValue);
        } else if (kValue instanceof org.opencompare.model.NotAvailable) {
            return new NotAvailableImpl((org.opencompare.model.NotAvailable) kValue);
        } else if (kValue instanceof org.opencompare.model.Conditional) {
            return new ConditionalImpl((org.opencompare.model.Conditional) kValue);
        } else if (kValue instanceof org.opencompare.model.Partial) {
            return new PartialImpl((org.opencompare.model.Partial) kValue);
        } else {
            throw new UnsupportedOperationException(kValue.getClass() + " interpretation type is not yet supported");
        }
        // TODO : continue
    }

}

