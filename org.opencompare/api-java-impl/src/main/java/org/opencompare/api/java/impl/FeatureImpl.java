package org.opencompare.api.java.impl;

import org.opencompare.api.java.Cell;
import org.opencompare.api.java.Feature;
import org.opencompare.api.java.PCMElement;
import org.opencompare.api.java.PCMFactory;
import org.opencompare.api.java.util.PCMVisitor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by gbecan on 08/10/14.
 */
public class FeatureImpl extends AbstractFeatureImpl implements Feature {

    private org.opencompare.model.Feature kFeature;

    public FeatureImpl(org.opencompare.model.Feature kFeature) {
        super(kFeature);
        this.kFeature = kFeature;
    }

    public org.opencompare.model.Feature getkFeature() {
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
    public List<Cell> getCells() {
        List<Cell> cells = new ArrayList<Cell>();
        for (org.opencompare.model.Cell kCell : kFeature.getCells()) {
            cells.add(new CellImpl(kCell));
        }
        return cells;
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

        if (this.getName() == null && feature.getName() != null) {
            return false;
        }

        if (this.getName() != null && !this.getName().equals(feature.getName())) {
            return false;
        }

        if (this.getParentGroup() == null && feature.getParentGroup() != null) {
            return false;
        }

        if (this.getParentGroup() != null && !this.getParentGroup().equals(feature.getParentGroup())) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getName(), this.getParentGroup());
    }

    @Override
    public String toString() {
        return "Feature(" + getName() + ")." + this.getParentGroup();
    }

    @Override
    public PCMElement clone(PCMFactory factory) {
        Feature copy = factory.createFeature();
        copy.setName(this.getName());
        // FIXME : how to handle the cells?
        return copy;
    }
}
