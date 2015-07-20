package org.opencompare.api.java.impl;

import org.opencompare.api.java.AbstractFeature;
import org.opencompare.api.java.FeatureGroup;

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

    @Override
    public FeatureGroup getParentGroup() {
        return new FeatureGroupImpl(kAbstractFeature.getParentGroup());
    }
}
