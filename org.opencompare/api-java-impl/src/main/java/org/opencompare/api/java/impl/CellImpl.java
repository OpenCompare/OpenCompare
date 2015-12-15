package org.opencompare.api.java.impl;

import org.opencompare.api.java.*;
import org.opencompare.api.java.util.PCMVisitor;

import java.util.Objects;

/**
 * Created by gbecan on 08/10/14.
 */
public class CellImpl extends PCMElementImpl implements Cell {

    private org.opencompare.model.Cell kCell;

    public CellImpl(org.opencompare.model.Cell kCell) {
        this.kCell = kCell;
    }

    public org.opencompare.model.Cell getkCell() {
        return kCell;
    }

    @Override
    public String getContent() {
        return kCell.getContent();
    }

    @Override
    public void setContent(String s) {
        kCell.setContent(s);
    }

    @Override
    public String getRawContent() {
        return kCell.getRawContent();
    }

    @Override
    public void setRawContent(String s) {
        kCell.setRawContent(s);
    }

    @Override
    public Value getInterpretation() {
        org.opencompare.model.Value kInterpretation = kCell.getInterpretation();

        return ValueImpl.wrapValue(kInterpretation);
    }

    @Override
    public void setInterpretation(Value value) {
        if (value != null) {
            kCell.setInterpretation(((ValueImpl) value).getkValue());
        }

    }

    @Override
    public Feature getFeature() {
        org.opencompare.model.Feature kFeature = kCell.getFeature();
        if (kFeature != null) {
            return new FeatureImpl(kFeature);
        } else {
            return null;
        }
    }

    @Override
    public void setFeature(Feature feature) {
        FeatureImpl featureImpl = (FeatureImpl) feature;
        if (featureImpl != null) {
            kCell.setFeature(featureImpl.getkFeature());
        }
    }

    @Override
    public void accept(PCMVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return "Cell('" + kCell.getContent() + "', '" + kCell.getRawContent() + "', " + this.getFeature() + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CellImpl cell = (CellImpl) o;

        if (this.getContent() == null) {
            return cell.getContent() == null;
        }

        if (!this.getContent().equals(cell.getContent())) {
            return false;
        }

        // TODO : raw content
        // TODO : interpretation

        return true;

    }

    @Override
    public int hashCode() {
        // TODO : interpretation
        return Objects.hash(this.getContent());
    }

    @Override
    public PCMElement clone(PCMFactory factory) {
        Cell copy = factory.createCell();
        copy.setContent(this.getContent());
        copy.setRawContent(this.getRawContent());
        copy.setInterpretation((Value) this.getInterpretation().clone(factory));
        copy.setFeature((Feature) this.getFeature().clone(factory)); // I don't know if this is a good idea. I am almost sure it is not a good idea.
        return copy;
    }

    @Override
    public Product getProduct() {
        return new ProductImpl(kCell.getProduct());
    }
}
