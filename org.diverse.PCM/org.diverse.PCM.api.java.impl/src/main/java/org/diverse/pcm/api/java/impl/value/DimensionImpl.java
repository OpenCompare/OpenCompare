package org.diverse.pcm.api.java.impl.value;

import org.diverse.pcm.api.java.impl.ValueImpl;
import org.diverse.pcm.api.java.util.PCMVisitor;
import org.diverse.pcm.api.java.value.Dimension;
import pcm.Value;

/**
 * Created by gbecan on 30/01/15.
 */
public class DimensionImpl extends ValueImpl implements Dimension {

    private pcm.Dimension kDimension;

    public DimensionImpl(pcm.Dimension kDimension) {
        super(kDimension);
        this.kDimension = kDimension;
    }

    public pcm.Dimension getkDimension() {
        return kDimension;
    }

    @Override
    public void accept(PCMVisitor visitor) {
        visitor.visit(this);
    }
}
