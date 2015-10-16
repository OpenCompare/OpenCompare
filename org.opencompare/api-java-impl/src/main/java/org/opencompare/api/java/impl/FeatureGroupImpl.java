package org.opencompare.api.java.impl;

import org.opencompare.api.java.AbstractFeature;
import org.opencompare.api.java.Feature;
import org.opencompare.api.java.FeatureGroup;
import org.opencompare.api.java.PCMElement;
import org.opencompare.api.java.PCMFactory;
import org.opencompare.api.java.util.PCMVisitor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by gbecan on 08/10/14.
 */
public class FeatureGroupImpl extends AbstractFeatureImpl implements FeatureGroup {

    private org.opencompare.model.FeatureGroup kFeatureGroup;

    public FeatureGroupImpl(org.opencompare.model.FeatureGroup kFeatureGroup) {
        super(kFeatureGroup);
        this.kFeatureGroup = kFeatureGroup;
    }

    public org.opencompare.model.FeatureGroup getkFeatureGroup() {
        return kFeatureGroup;
    }

    @Override
    public List<AbstractFeature> getFeatures() {
        List<AbstractFeature> features = new ArrayList<AbstractFeature>();
        for (org.opencompare.model.AbstractFeature kAbstractFeature : kFeatureGroup.getSubFeatures()) {
            if (kAbstractFeature instanceof org.opencompare.model.Feature) {
                features.add(new FeatureImpl((org.opencompare.model.Feature) kAbstractFeature));
            } else if (kAbstractFeature instanceof org.opencompare.model.FeatureGroup) {
                features.add(new FeatureGroupImpl((org.opencompare.model.FeatureGroup) kAbstractFeature));
            }
        }
        return features;
    }

    @Override
    public List<Feature> getConcreteFeatures() {
        List<Feature> features = new ArrayList<>();
        for (AbstractFeature feature : getFeatures()) {
            if (feature instanceof Feature) {
                features.add((Feature) feature);
            } else if (feature instanceof FeatureGroup) {
                features.addAll(((FeatureGroup) feature).getConcreteFeatures());
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

    @Override
    public PCMElement clone(PCMFactory factory) {
        FeatureGroup copy = factory.createFeatureGroup();
        for (AbstractFeature feature : this.getFeatures()) {
            copy.addFeature((AbstractFeature) feature.clone(factory));
        }
        copy.setName(this.getName());
        return copy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FeatureGroupImpl featureGroup = (FeatureGroupImpl) o;

        if (this.getName() == null && featureGroup.getName() != null) {
            return false;
        }

        if (this.getName() != null && !this.getName().equals(featureGroup.getName())) {
            return false;
        }

        if (this.getParentGroup() == null && featureGroup.getParentGroup() != null) {
            return false;
        }

        if (this.getParentGroup() != null && !this.getParentGroup().equals(featureGroup.getParentGroup())) {
            return false;
        }

        return true;
    }

    public String toString() {
        return "FeatureGroup(" + getName() + ")." + this.getParentGroup();
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getName(), this.getParentGroup());
    }

}
