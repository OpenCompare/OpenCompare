package org.diverse.pcm.api.java.impl;

import org.diverse.pcm.api.java.Cell;
import org.diverse.pcm.api.java.Feature;
import org.diverse.pcm.api.java.Value;
import org.diverse.pcm.api.java.impl.value.*;
import org.diverse.pcm.api.java.util.PCMVisitor;
import pcm.*;

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
    public Value getInterpretation() {
        pcm.Value kInterpretation = kCell.getInterpretation();

        return ValueImpl.wrapValue(kInterpretation);
    }

    @Override
    public void setInterpretation(Value value) {
       kCell.setInterpretation(((ValueImpl) value).getkValue());
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
}
