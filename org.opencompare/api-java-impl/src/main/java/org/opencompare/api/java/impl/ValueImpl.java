package org.opencompare.api.java.impl;

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
        } else if (kValue instanceof BooleanValue) {
            return new BooleanValueImpl((BooleanValue) kValue);
        } else if (kValue instanceof IntegerValue) {
            return new IntegerValueImpl((IntegerValue) kValue);
        } else if (kValue instanceof StringValue) {
            return new StringValueImpl((StringValue) kValue);
        } else if (kValue instanceof RealValue) {
            return new RealValueImpl((RealValue) kValue);
        } else if (kValue instanceof Multiple) {
            return new MultipleImpl((Multiple) kValue);
        } else if (kValue instanceof NotApplicable) {
            return new NotApplicableImpl((NotApplicable) kValue);
        } else if (kValue instanceof NotAvailable) {
            return new NotAvailableImpl((NotAvailable) kValue);
        } else if (kValue instanceof Conditional) {
            return new ConditionalImpl((Conditional) kValue);
        } else if (kValue instanceof Partial) {
            return new PartialImpl((Partial) kValue);
        } else {
            throw new UnsupportedOperationException(kValue.getClass() + " interpretation type is not yet supported");
        }
        // TODO : continue
    }

}

