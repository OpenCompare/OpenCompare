package org.diverse.pcm.api.java.impl;

/**
 * Created by gbecan on 08/10/14.
 */
public abstract class AbstractFeatureImpl implements org.diverse.pcm.api.java.AbstractFeature {

    private pcm.AbstractFeature kAbstractFeature;

    public AbstractFeatureImpl(pcm.AbstractFeature kAbstractFeature) {
        this.kAbstractFeature = kAbstractFeature;
    }

    public pcm.AbstractFeature getkAbstractFeature() {
        return kAbstractFeature;
    }

}
