package org.opencompare.api.java.impl.value;

import org.opencompare.api.java.PCMElement;
import org.opencompare.api.java.PCMFactory;
import org.opencompare.api.java.impl.ValueImpl;
import org.opencompare.api.java.util.PCMVisitor;
import org.opencompare.api.java.value.Dimension;

/**
 * Created by gbecan on 30/01/15.
 */
public class DimensionImpl extends ValueImpl implements Dimension {

    private org.opencompare.model.Dimension kDimension;

    public DimensionImpl(org.opencompare.model.Dimension kDimension) {
        super(kDimension);
        this.kDimension = kDimension;
    }

    public org.opencompare.model.Dimension getkDimension() {
        return kDimension;
    }

    @Override
    public void accept(PCMVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public PCMElement clone(PCMFactory factory) {
        Dimension copy = factory.createDimension();
        return copy;
    }
}
