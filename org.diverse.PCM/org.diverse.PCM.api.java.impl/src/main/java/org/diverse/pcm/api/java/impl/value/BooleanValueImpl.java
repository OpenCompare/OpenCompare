package org.diverse.pcm.api.java.impl.value;

import org.diverse.pcm.api.java.value.BooleanValue;

/**
 * Created by gbecan on 09/10/14.
 */
public class BooleanValueImpl implements BooleanValue {

    private pcm.BooleanValue kBooleanValue;

    public BooleanValueImpl(pcm.BooleanValue kBooleanValue) {
        this.kBooleanValue = kBooleanValue;
    }

    public pcm.BooleanValue getkBooleanValue() {
        return kBooleanValue;
    }

    @Override
    public boolean getValue() {
        return kBooleanValue.getValue();
    }
}
