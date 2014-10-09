package org.diverse.pcm.api.java.impl;

import org.diverse.pcm.api.java.AbstractFeature;

/**
 * Created by gbecan on 09/10/14.
 */
public abstract class AbstractFeatureImpl implements AbstractFeature {

    private pcm.AbstractFeature kAbstractFeature;

    protected AbstractFeatureImpl(pcm.AbstractFeature kAbstractFeature) {
        this.kAbstractFeature = kAbstractFeature;
    }

    public pcm.AbstractFeature getkAbstractFeature() {
        return kAbstractFeature;
    }

}
