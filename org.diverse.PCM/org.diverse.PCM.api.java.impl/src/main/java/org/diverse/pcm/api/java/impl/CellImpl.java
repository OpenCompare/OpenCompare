package org.diverse.pcm.api.java.impl;

import org.diverse.pcm.api.java.Cell;
import org.diverse.pcm.api.java.Feature;
import org.diverse.pcm.api.java.Product;
import org.diverse.pcm.api.java.Value;
import org.diverse.pcm.api.java.impl.value.*;
import org.diverse.pcm.api.java.util.PCMVisitor;
import org.diverse.pcm.api.java.value.Partial;
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

        if (kInterpretation == null) {
            return null;
        } else if (kInterpretation instanceof BooleanValue) {
            return new BooleanValueImpl((BooleanValue) kInterpretation);
        } else if (kInterpretation instanceof IntegerValue) {
            return new IntegerValueImpl((IntegerValue) kInterpretation);
        } else if (kInterpretation instanceof StringValue) {
            return new StringValueImpl((StringValue) kInterpretation);
        } else if (kInterpretation instanceof RealValue) {
            return new RealValueImpl((RealValue) kInterpretation);
        } else if (kInterpretation instanceof Multiple) {
            return new MultipleImpl((Multiple) kInterpretation);
        } else if (kInterpretation instanceof NotAvailable) {
            return new NotAvailableImpl((NotAvailable) kInterpretation);
        } else if (kInterpretation instanceof Conditional) {
            return new ConditionalImpl((Conditional) kInterpretation);
        } else {
            throw new UnsupportedOperationException(kInterpretation.getClass() + " interpretation type is not yet supported");
        }
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CellImpl cell = (CellImpl) o;

        if (kCell != null ? !kCell.equals(cell.kCell) : cell.kCell != null) {
            return false;
        }

        return true;
    }
}
