package org.opencompare.api.java.impl.value;

import org.opencompare.api.java.PCMElement;
import org.opencompare.api.java.PCMFactory;
import org.opencompare.api.java.Value;
import org.opencompare.api.java.impl.ValueImpl;
import org.opencompare.api.java.util.PCMVisitor;
import org.opencompare.api.java.value.Multiple;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gbecan on 14/10/14.
 */
public class MultipleImpl extends ValueImpl implements Multiple {

    private org.opencompare.model.Multiple kMultiple;

    public MultipleImpl(org.opencompare.model.Multiple kMultiple) {
        super(kMultiple);
        this.kMultiple = kMultiple;
    }

    public org.opencompare.model.Multiple getkMultiple() {
        return kMultiple;
    }

    @Override
    public List<Value> getSubValues() {
        List<Value> subValues = new ArrayList<Value>();
        for (org.opencompare.model.Value kValue : kMultiple.getSubvalues()) {
            subValues.add(ValueImpl.wrapValue(kValue));
        }
        return subValues;
    }

    @Override
    public void addSubValue(Value value) {
        kMultiple.addSubvalues(((ValueImpl) value).getkValue());
    }

    @Override
    public void removeSubValue(Value value) {
        kMultiple.removeSubvalues(((ValueImpl) value).getkValue());
    }

    @Override
    public void accept(PCMVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public PCMElement clone(PCMFactory factory) {
        Multiple copy = factory.createMultiple();
        for (Value subValue : this.getSubValues()) {
            copy.addSubValue((Value) subValue.clone(factory));
        }
        return copy;
    }
}
