package org.diverse.pcm.api.java.impl.value;

import org.diverse.pcm.api.java.impl.ValueImpl;
import org.diverse.pcm.api.java.util.PCMVisitor;
import org.diverse.pcm.api.java.value.StringValue;

/**
 * Created by gbecan on 09/10/14.
 */
public class StringValueImpl extends ValueImpl implements StringValue {

    private pcm.StringValue kStringValue;

    public StringValueImpl(pcm.StringValue kStringValue) {
        super(kStringValue);
        this.kStringValue = kStringValue;
    }

    public pcm.StringValue getkStringValue() {
        return kStringValue;
    }

    @Override
    public String getValue() {
        return kStringValue.getValue();
    }

    @Override
    public void accept(PCMVisitor visitor) {
        visitor.visit(this);
    }
}
