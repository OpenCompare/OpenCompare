package org.opencompare.api.java.impl;

import org.opencompare.api.java.AbstractFeature;
import org.opencompare.api.java.FeatureGroup;

/**
 * Created by gbecan on 09/10/14.
 */
public abstract class AbstractFeatureImpl implements AbstractFeature {

    private org.opencompare.model.AbstractFeature kAbstractFeature;

    protected AbstractFeatureImpl(org.opencompare.model.AbstractFeature kAbstractFeature) {
        this.kAbstractFeature = kAbstractFeature;
    }

    public org.opencompare.model.AbstractFeature getkAbstractFeature() {
        return kAbstractFeature;
    }

    @Override
    public FeatureGroup getParentGroup() {
        org.opencompare.model.FeatureGroup kParentGroup = kAbstractFeature.getParentGroup();
        if (kParentGroup != null) {
            return new FeatureGroupImpl(kParentGroup);
        } else {
            return null;
        }

    }
}
