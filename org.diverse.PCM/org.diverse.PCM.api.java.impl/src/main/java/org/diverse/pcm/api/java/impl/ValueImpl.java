package org.diverse.pcm.api.java.impl;

import org.diverse.pcm.api.java.Value;

/**
 * Created by gbecan on 08/10/14.
 */
public class ValueImpl implements Value {

    private pcm.Value kValue;

    public ValueImpl(pcm.Value kValue) {
        this.kValue = kValue;
    }

    public pcm.Value getkValue() {
        return kValue;
    }

}
