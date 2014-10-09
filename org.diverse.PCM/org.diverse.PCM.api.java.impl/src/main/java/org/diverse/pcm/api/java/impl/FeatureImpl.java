package org.diverse.pcm.api.java.impl;

import pcm.AbstractFeature;

/**
 * Created by gbecan on 08/10/14.
 */
public class FeatureImpl extends AbstractFeatureImpl implements org.diverse.pcm.api.java.Feature {

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
}
