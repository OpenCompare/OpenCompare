package org.opencompare.api.java.impl;

import org.opencompare.api.java.Feature;
import org.opencompare.api.java.util.PCMVisitor;
import pcm.AbstractFeature;

/**
 * Created by gbecan on 08/10/14.
 */
public class FeatureImpl extends AbstractFeatureImpl implements Feature {

    private pcm.Feature kFeature;

    public FeatureImpl(pcm.Feature kFeature) {
        super(kFeature);
        this.kFeature = kFeature;
    }

    public pcm.Feature getkFeature() {
        return kFeature;
    }

    @Override
    public String getName() {
        return kFeature.getName();
    }

    @Override
    public void setName(String s) {
        kFeature.setName(s);
    }

    @Override
    public void accept(PCMVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FeatureImpl feature = (FeatureImpl) o;

        if (kFeature != null ? !kFeature.equals(feature.kFeature) : feature.kFeature != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return kFeature != null ? kFeature.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Feature(" + getName() + ")";
    }
}
