package org.opencompare.api.java.impl.value;

import org.opencompare.api.java.PCMElement;
import org.opencompare.api.java.PCMFactory;
import org.opencompare.api.java.impl.ValueImpl;
import org.opencompare.api.java.util.PCMVisitor;
import org.opencompare.api.java.value.StringValue;

/**
 * Created by gbecan on 09/10/14.
 */
public class StringValueImpl extends ValueImpl implements StringValue {

    private org.opencompare.model.StringValue kStringValue;

    public StringValueImpl(org.opencompare.model.StringValue kStringValue) {
        super(kStringValue);
        this.kStringValue = kStringValue;
    }

    public org.opencompare.model.StringValue getkStringValue() {
        return kStringValue;
    }

    @Override
    public String getValue() {
        return kStringValue.getValue();
    }

    @Override
    public void setValue(String value) {
        kStringValue.setValue(value);
    }

    @Override
    public void accept(PCMVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public PCMElement clone(PCMFactory factory) {
        StringValue copy = factory.createStringValue();
        copy.setValue(this.getValue());
        return copy;
    }
}
