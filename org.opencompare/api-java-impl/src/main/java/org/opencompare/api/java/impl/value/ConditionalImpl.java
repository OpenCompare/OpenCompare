package org.opencompare.api.java.impl.value;

import org.opencompare.api.java.Value;
import org.opencompare.api.java.impl.ValueImpl;
import org.opencompare.api.java.util.PCMVisitor;
import org.opencompare.api.java.value.Conditional;

/**
 * Created by gbecan on 14/10/14.
 */
public class ConditionalImpl extends ValueImpl implements Conditional {

    private pcm.Conditional kConditional;

    public ConditionalImpl(pcm.Conditional kConditional) {
        super(kConditional);
        this.kConditional = kConditional;
    }

    public pcm.Conditional getkConditional() {
        return kConditional;
    }

    @Override
    public Value getValue() {
        try {
            return ValueImpl.wrapValue(kConditional.getValue().getResult());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void setValue(Value value) {
        kConditional.setValue(((ValueImpl) value).getkValue());
    }

    @Override
    public Value getCondition() {
        try {
            return ValueImpl.wrapValue(kConditional.getCondition().getResult());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void setCondition(Value condition) {
        kConditional.setCondition(((ValueImpl) condition).getkValue());
    }

    @Override
    public void accept(PCMVisitor visitor) {
        visitor.visit(this);
    }
}
