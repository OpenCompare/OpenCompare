package org.diverse.pcm.api.java.impl.value;

import org.diverse.pcm.api.java.value.StringValue;

/**
 * Created by gbecan on 09/10/14.
 */
public class StringValueImpl implements StringValue {

    private pcm.StringValue kStringValue;

    public StringValueImpl(pcm.StringValue kStringValue) {
        this.kStringValue = kStringValue;
    }

    public pcm.StringValue getkStringValue() {
        return kStringValue;
    }

    @Override
    public String getValue() {
        return kStringValue.getValue();
    }
}
