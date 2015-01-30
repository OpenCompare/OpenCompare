package org.diverse.pcm.api.java.impl.value;

import org.diverse.pcm.api.java.impl.ValueImpl;
import org.diverse.pcm.api.java.util.PCMVisitor;
import org.diverse.pcm.api.java.value.NotApplicable;

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
}
