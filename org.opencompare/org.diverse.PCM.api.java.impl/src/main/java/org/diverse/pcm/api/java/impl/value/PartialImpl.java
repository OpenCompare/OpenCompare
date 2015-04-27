package org.diverse.pcm.api.java.impl.value;

import org.diverse.pcm.api.java.Value;
import org.diverse.pcm.api.java.impl.ValueImpl;
import org.diverse.pcm.api.java.util.PCMVisitor;
import org.diverse.pcm.api.java.value.Partial;

/**
 * Created by gbecan on 14/10/14.
 */
public class PartialImpl extends ValueImpl implements Partial {

    private pcm.Partial kPartial;

    public PartialImpl(pcm.Partial kPartial) {
        super(kPartial);
        this.kPartial = kPartial;
    }

    public pcm.Partial getkPartial() {
        return kPartial;
    }

    @Override
    public Value getValue() {
        return ValueImpl.wrapValue(kPartial.getValue());
    }

    @Override
    public void setValue(Value value) {
        kPartial.setValue(((ValueImpl) value).getkValue());
    }

    @Override
    public void accept(PCMVisitor visitor) {
        visitor.visit(this);
    }
}
