package org.opencompare.api.java.impl;

import org.opencompare.api.java.AbstractFeature;
import org.opencompare.api.java.Feature;
import org.opencompare.api.java.FeatureGroup;
import org.opencompare.api.java.util.PCMVisitor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by gbecan on 08/10/14.
 */
public class FeatureGroupImpl extends AbstractFeatureImpl implements FeatureGroup {

    private pcm.FeatureGroup kFeatureGroup;

    public FeatureGroupImpl(pcm.FeatureGroup kFeatureGroup) {
        super(kFeatureGroup);
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
        kFeatureGroup.addSubFeatures(((AbstractFeatureImpl) feature).getkAbstractFeature());
    }

    @Override
    public void removeFeature(AbstractFeature feature) {
        kFeatureGroup.removeSubFeatures(((AbstractFeatureImpl) feature).getkAbstractFeature());
    }

    @Override
    public int getDepth() {
        try {
            return getRecursiveDepth(1);
        } catch (Exception e) {
            return 1;
        }
    }

    private int getRecursiveDepth(int depth) throws Exception{
        for (AbstractFeature abstractFeature: getFeatures()) {
            if (abstractFeature instanceof FeatureGroup) {
                FeatureGroupImpl featureGroup = (FeatureGroupImpl) abstractFeature;
                featureGroup.getRecursiveDepth(depth + 1);
            } else if (abstractFeature instanceof Feature) {
                return depth + 1;
            } else {
                throw new Exception();
            }
        }
        return depth;
    }

    @Override
    public String getName() {
        return kFeatureGroup.getName();
    }

    @Override
    public void setName(String s) {
        kFeatureGroup.setName(s);
    }

    @Override
    public void accept(PCMVisitor visitor) {
        visitor.visit(this);
    }

    public String toString() {
        return "FeatureGroup(" + getName() + ")";
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getName() + "Group");
    }
}
