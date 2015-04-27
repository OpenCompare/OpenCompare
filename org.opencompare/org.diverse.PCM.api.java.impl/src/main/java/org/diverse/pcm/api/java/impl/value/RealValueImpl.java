package org.diverse.pcm.api.java.impl.value;

import org.diverse.pcm.api.java.impl.ValueImpl;
import org.diverse.pcm.api.java.util.PCMVisitor;
import org.diverse.pcm.api.java.value.RealValue;

/**
 * Created by gbecan on 14/10/14.
 */
public class RealValueImpl extends ValueImpl implements RealValue {

    private pcm.RealValue kRealValue;

    public RealValueImpl(pcm.RealValue kRealValue) {
        super(kRealValue);
        this.kRealValue = kRealValue;
    }

    public pcm.RealValue getkRealValue() {
        return kRealValue;
    }

    @Override
    public double getValue() {
        return kRealValue.getValue();
    }

    @Override
    public void setValue(double value) {
        kRealValue.setValue(value);
    }

    @Override
    public void accept(PCMVisitor visitor) {
        visitor.visit(this);
    }
}
