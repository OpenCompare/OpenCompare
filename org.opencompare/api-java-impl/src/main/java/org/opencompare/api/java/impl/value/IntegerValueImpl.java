package org.opencompare.api.java.impl.value;

import org.opencompare.api.java.PCMElement;
import org.opencompare.api.java.PCMFactory;
import org.opencompare.api.java.impl.ValueImpl;
import org.opencompare.api.java.util.PCMVisitor;
import org.opencompare.api.java.value.IntegerValue;

/**
 * Created by gbecan on 09/10/14.
 */
public class IntegerValueImpl extends ValueImpl implements IntegerValue {

    private org.opencompare.model.IntegerValue kIntegerValue;

    public IntegerValueImpl(org.opencompare.model.IntegerValue kIntegerValue) {
        super(kIntegerValue);
        this.kIntegerValue = kIntegerValue;
    }

    public org.opencompare.model.IntegerValue getkIntegerValue() {
        return kIntegerValue;
    }

    @Override
    public int getValue() {
        return kIntegerValue.getValue();
    }

    @Override
    public void setValue(int value) {
        kIntegerValue.setValue(value);
    }

    @Override
    public void accept(PCMVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public PCMElement clone(PCMFactory factory) {
        IntegerValue copy = factory.createIntegerValue();
        copy.setValue(copy.getValue());
        return copy;
    }

}
