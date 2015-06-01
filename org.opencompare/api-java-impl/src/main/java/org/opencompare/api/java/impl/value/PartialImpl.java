package org.opencompare.api.java.impl.value;

import org.opencompare.api.java.Value;
import org.opencompare.api.java.impl.ValueImpl;
import org.opencompare.api.java.util.PCMVisitor;
import org.opencompare.api.java.value.Partial;

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
        try {
            return ValueImpl.wrapValue(kPartial.getValue().getResult());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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
