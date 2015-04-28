package org.opencompare.api.java.impl.value;

import org.opencompare.api.java.impl.ValueImpl;
import org.opencompare.api.java.util.PCMVisitor;
import org.opencompare.api.java.value.BooleanValue;

/**
 * Created by gbecan on 09/10/14.
 */
public class BooleanValueImpl extends ValueImpl implements BooleanValue {

    private pcm.BooleanValue kBooleanValue;

    public BooleanValueImpl(pcm.BooleanValue kBooleanValue) {
        super(kBooleanValue);
        this.kBooleanValue = kBooleanValue;
    }

    public pcm.BooleanValue getkBooleanValue() {
        return kBooleanValue;
    }

    @Override
    public boolean getValue() {
        return kBooleanValue.getValue();
    }

    @Override
    public void setValue(boolean b) {
        kBooleanValue.setValue(b);
    }

    @Override
    public void accept(PCMVisitor visitor) {
        visitor.visit(this);
    }
}
