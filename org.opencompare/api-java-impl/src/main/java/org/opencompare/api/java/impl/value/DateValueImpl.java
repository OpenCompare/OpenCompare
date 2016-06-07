package org.opencompare.api.java.impl.value;

import org.opencompare.api.java.PCMElement;
import org.opencompare.api.java.PCMFactory;
import org.opencompare.api.java.impl.ValueImpl;
import org.opencompare.api.java.util.PCMVisitor;
import org.opencompare.api.java.value.DateValue;

/**
 * Created by gbecan on 30/01/15.
 */
public class DateValueImpl extends ValueImpl implements DateValue {

    private org.opencompare.model.DateValue kDateValue;

    public DateValueImpl(org.opencompare.model.DateValue kDateValue) {
        super(kDateValue);
        this.kDateValue = kDateValue;
    }

    public org.opencompare.model.DateValue getkDateValue() {
        return kDateValue;
    }

    @Override
    public String getValue() {
        return kDateValue.getValue();
    }

    @Override
    public void setValue(String value) {
        kDateValue.setValue(value);
    }

    @Override
    public void accept(PCMVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public PCMElement clone(PCMFactory factory) {
        DateValue copy = factory.createDateValue();
        copy.setValue(this.getValue());
        return copy;
    }
}
