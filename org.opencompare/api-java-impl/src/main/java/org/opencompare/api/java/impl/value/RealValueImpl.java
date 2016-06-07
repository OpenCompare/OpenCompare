package org.opencompare.api.java.impl.value;

import org.opencompare.api.java.PCMElement;
import org.opencompare.api.java.PCMFactory;
import org.opencompare.api.java.impl.ValueImpl;
import org.opencompare.api.java.util.PCMVisitor;
import org.opencompare.api.java.value.RealValue;

/**
 * Created by gbecan on 14/10/14.
 */
public class RealValueImpl extends ValueImpl implements RealValue {

    private org.opencompare.model.RealValue kRealValue;

    public RealValueImpl(org.opencompare.model.RealValue kRealValue) {
        super(kRealValue);
        this.kRealValue = kRealValue;
    }

    public org.opencompare.model.RealValue getkRealValue() {
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

    @Override
    public PCMElement clone(PCMFactory factory) {
        RealValue copy = factory.createRealValue();
        copy.setValue(this.getValue());
        return copy;
    }
}
