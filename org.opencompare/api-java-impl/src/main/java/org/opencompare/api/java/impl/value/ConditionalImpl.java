package org.opencompare.api.java.impl.value;

import org.opencompare.api.java.PCMElement;
import org.opencompare.api.java.PCMFactory;
import org.opencompare.api.java.Value;
import org.opencompare.api.java.impl.ValueImpl;
import org.opencompare.api.java.util.PCMVisitor;
import org.opencompare.api.java.value.Conditional;

/**
 * Created by gbecan on 14/10/14.
 */
public class ConditionalImpl extends ValueImpl implements Conditional {

    private org.opencompare.model.Conditional kConditional;

    public ConditionalImpl(org.opencompare.model.Conditional kConditional) {
        super(kConditional);
        this.kConditional = kConditional;
    }

    public org.opencompare.model.Conditional getkConditional() {
        return kConditional;
    }

    @Override
    public Value getValue() {
        return ValueImpl.wrapValue(kConditional.getValue());
    }

    @Override
    public void setValue(Value value) {
        kConditional.setValue(((ValueImpl) value).getkValue());
    }

    @Override
    public Value getCondition() {
        return ValueImpl.wrapValue(kConditional.getCondition());
    }

    @Override
    public void setCondition(Value condition) {
        kConditional.setCondition(((ValueImpl) condition).getkValue());
    }

    @Override
    public void accept(PCMVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public PCMElement clone(PCMFactory factory) {
        Conditional copy = factory.createConditional();
        copy.setValue((Value) this.getValue().clone(factory));
        copy.setCondition((Value) this.getCondition().clone(factory));
        return copy;
    }
}
