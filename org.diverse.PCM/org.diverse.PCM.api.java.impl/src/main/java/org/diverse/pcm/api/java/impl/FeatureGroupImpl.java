package org.diverse.pcm.api.java.impl;

import org.diverse.pcm.api.java.AbstractFeature;
import org.diverse.pcm.api.java.Feature;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gbecan on 08/10/14.
 */
public class FeatureGroupImpl implements org.diverse.pcm.api.java.FeatureGroup {

    private pcm.FeatureGroup kFeatureGroup;

    public FeatureGroupImpl(pcm.FeatureGroup kFeatureGroup) {
        this.kFeatureGroup = kFeatureGroup;
    }

    public pcm.FeatureGroup getkFeatureGroup() {
        return kFeatureGroup;
    }

    @Override
    public List<AbstractFeature> getFeatures() {
        List<AbstractFeature> features = new ArrayList<AbstractFeature>();
        for (pcm.AbstractFeature kAbstractFeature : kFeatureGroup.getSubFeatures()) {
            if (kAbstractFeature instanceof pcm.Feature) {
                features.add(new FeatureImpl((pcm.Feature) kAbstractFeature));
            } else if (kAbstractFeature instanceof pcm.FeatureGroup) {
                features.add(new FeatureGroupImpl((pcm.FeatureGroup) kAbstractFeature));
            }
        }
        return features;
    }

    @Override
    public void addFeature(AbstractFeature feature) {
        kFeatureGroup.addSubFeatures(((AbstractFeatureImpl) kFeatureGroup).getkAbstractFeature());
    }

    @Override
    public void removeFeature(AbstractFeature feature) {
        kFeatureGroup.removeSubFeatures(((AbstractFeatureImpl) kFeatureGroup).getkAbstractFeature());
    }

    @Override
    public String getName() {
        return kFeatureGroup.getName();
    }

    @Override
    public void setName(String s) {
        kFeatureGroup.setName(s);
    }
}
