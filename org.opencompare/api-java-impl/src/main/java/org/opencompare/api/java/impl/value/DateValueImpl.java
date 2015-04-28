package org.opencompare.api.java.impl.value;

import org.opencompare.api.java.impl.ValueImpl;
import org.opencompare.api.java.util.PCMVisitor;
import org.opencompare.api.java.value.DateValue;
import pcm.Value;

/**
 * Created by gbecan on 30/01/15.
 */
public class DateValueImpl extends ValueImpl implements DateValue {

    private pcm.DateValue kDateValue;

    public DateValueImpl(pcm.DateValue kDateValue) {
        super(kDateValue);
        this.kDateValue = kDateValue;
    }

    public pcm.DateValue getkDateValue() {
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
}
