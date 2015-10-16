package org.opencompare.api.java.impl.value;

import org.opencompare.api.java.PCMElement;
import org.opencompare.api.java.PCMFactory;
import org.opencompare.api.java.Value;
import org.opencompare.api.java.impl.ValueImpl;
import org.opencompare.api.java.util.PCMVisitor;
import org.opencompare.api.java.value.Partial;

/**
 * Created by gbecan on 14/10/14.
 */
public class PartialImpl extends ValueImpl implements Partial {

    private org.opencompare.model.Partial kPartial;

    public PartialImpl(org.opencompare.model.Partial kPartial) {
        super(kPartial);
        this.kPartial = kPartial;
    }

    public org.opencompare.model.Partial getkPartial() {
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

    @Override
    public PCMElement clone(PCMFactory factory) {
        Partial copy = factory.createPartial();
        copy.setValue((Value) this.getValue().clone(factory));
        return copy;
    }
}
