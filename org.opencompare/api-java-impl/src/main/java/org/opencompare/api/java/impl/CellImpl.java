package org.opencompare.api.java.impl;

import org.opencompare.api.java.Cell;
import org.opencompare.api.java.Feature;
import org.opencompare.api.java.Value;
import org.opencompare.api.java.util.PCMVisitor;

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
}
