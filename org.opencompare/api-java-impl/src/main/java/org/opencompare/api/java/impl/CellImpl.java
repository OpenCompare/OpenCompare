package org.opencompare.api.java.impl;

import org.opencompare.api.java.*;
import org.opencompare.api.java.util.PCMVisitor;

import java.util.Objects;

/**
 * Created by gbecan on 08/10/14.
 */
public class CellImpl extends PCMElementImpl implements Cell {

    private pcm.Cell kCell;

    public CellImpl(pcm.Cell kCell) {
        this.kCell = kCell;
    }

    public pcm.Cell getkCell() {
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
        pcm.Value kInterpretation = kCell.getInterpretation();

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
        return new FeatureImpl(kCell.getFeature());
    }

    @Override
    public void setFeature(Feature feature) {
        kCell.setFeature(((FeatureImpl) feature).getkFeature());
    }

    @Override
    public void accept(PCMVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return "Cell(" + kCell.getContent() + ")";
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
        copy.setFeature((Feature) this.getFeature().clone(factory)); // I don't know if this is a good idea
        return copy;
    }

    @Override
    public Product getProduct() {
        return new ProductImpl(kCell.getProduct());
    }
}
