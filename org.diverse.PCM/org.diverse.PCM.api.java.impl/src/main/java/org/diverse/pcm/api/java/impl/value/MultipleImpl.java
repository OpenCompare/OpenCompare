package org.diverse.pcm.api.java.impl.value;

import org.diverse.pcm.api.java.Value;
import org.diverse.pcm.api.java.impl.ValueImpl;
import org.diverse.pcm.api.java.util.PCMVisitor;
import org.diverse.pcm.api.java.value.Multiple;

import java.util.List;

/**
 * Created by gbecan on 14/10/14.
 */
public class MultipleImpl extends ValueImpl implements Multiple {

    private pcm.Multiple kMultiple;

    public MultipleImpl(pcm.Multiple kMultiple) {
        super(kMultiple);
        this.kMultiple = kMultiple;
    }

    public pcm.Multiple getkMultiple() {
        return kMultiple;
    }

    @Override
    public List<Value> getSubValues() {
        throw new UnsupportedOperationException(); // TODO
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
}
