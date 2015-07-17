package org.opencompare.api.java.impl.value;

import org.opencompare.api.java.PCMElement;
import org.opencompare.api.java.PCMFactory;
import org.opencompare.api.java.impl.ValueImpl;
import org.opencompare.api.java.util.PCMVisitor;
import org.opencompare.api.java.value.NotApplicable;

/**
 * Created by gbecan on 28/01/15.
 */
public class NotApplicableImpl extends ValueImpl implements NotApplicable {

    private pcm.NotApplicable kNotApplicable;

    public NotApplicableImpl(pcm.NotApplicable kNotApplicable) {
        super(kNotApplicable);
        this.kNotApplicable = kNotApplicable;
    }

    public pcm.NotApplicable getkNotApplicable() {
        return kNotApplicable;
    }

    @Override
    public void accept(PCMVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public PCMElement clone(PCMFactory factory) {
        NotApplicable copy = factory.createNotApplicable();
        return copy;
    }
}
