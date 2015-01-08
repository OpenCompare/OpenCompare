package org.diverse.pcm.api.java.impl;

import org.diverse.pcm.api.java.Value;

/**
 * Created by gbecan on 09/10/14.
 */
public abstract class ValueImpl implements Value {

    private pcm.Value kValue;

    protected ValueImpl(pcm.Value kValue) {
        this.kValue = kValue;
    }

    public pcm.Value getkValue() {
        return kValue;
    }


}

