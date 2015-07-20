package org.opencompare.api.java.impl;

import org.opencompare.api.java.PCMFactory;
import org.opencompare.api.java.Value;
import org.opencompare.api.java.impl.value.*;

/**
 * Created by gbecan on 09/10/14.
 */
public abstract class ValueImpl implements Value {

    private pcm.Value kValue;

    protected ValueImpl(pcm.Value kValue) {
        this.kValue = kValue;
    }

    public pcm.Value getkValue() {
        return kValue;
    }


    public static ValueImpl wrapValue(pcm.Value kValue) {
        if (kValue == null) {
            return null;
        } else if (kValue instanceof pcm.BooleanValue) {
            return new BooleanValueImpl((pcm.BooleanValue) kValue);
        } else if (kValue instanceof pcm.IntegerValue) {
            return new IntegerValueImpl((pcm.IntegerValue) kValue);
        } else if (kValue instanceof pcm.StringValue) {
            return new StringValueImpl((pcm.StringValue) kValue);
        } else if (kValue instanceof pcm.RealValue) {
            return new RealValueImpl((pcm.RealValue) kValue);
        } else if (kValue instanceof pcm.Multiple) {
            return new MultipleImpl((pcm.Multiple) kValue);
        } else if (kValue instanceof pcm.NotApplicable) {
            return new NotApplicableImpl((pcm.NotApplicable) kValue);
        } else if (kValue instanceof pcm.NotAvailable) {
            return new NotAvailableImpl((pcm.NotAvailable) kValue);
        } else if (kValue instanceof pcm.Conditional) {
            return new ConditionalImpl((pcm.Conditional) kValue);
        } else if (kValue instanceof pcm.Partial) {
            return new PartialImpl((pcm.Partial) kValue);
        } else {
            throw new UnsupportedOperationException(kValue.getClass() + " interpretation type is not yet supported");
        }
        // TODO : continue
    }

}

